package com.tw.api;

import com.tw.api.util.BodyParser;
import com.tw.api.util.Routing;
import com.tw.api.util.Validation;
import com.tw.domain.Evaluation;
import com.tw.domain.Project;
import com.tw.domain.User;
import com.tw.mapper.EvaluationMapper;
import com.tw.mapper.QualificationMapper;
import com.tw.session.core.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.stream.Collectors;

public class EvaluationApi {
    private Project project;
    private User user;
    private Evaluation evaluation;

    public EvaluationApi(Project project, User user, Evaluation evaluation) {
        this.project = project;
        this.user = user;

        this.evaluation = evaluation;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvaluation(@PathParam("evaluationId") long evaluationId,
                                  @Context EvaluationMapper evaluationMapper) {
        Evaluation evaluation = evaluationMapper.findById(project.getId(), user.getId(), evaluationId);
        if (evaluation == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(evaluation.toJson()).build();
    }

    @GET
    @Path("instructions")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getInstructions(@Context UriInfo uriInfo, @QueryParam("token") String accessKey) {
        if (accessKey == null || !accessKey.equals(evaluation.getAccessKey())) {
            return Response.status(403).build();
        }
        return Response.ok(generateInstruction(getHost(uriInfo))).build();
    }


    @POST
    @Path("result")
    public Response createResult(Form form,
                                 @Context Session session,
                                 @Context EvaluationMapper evaluationMapper,
                                 @Context QualificationMapper qualificationMapper) {
        Map<String, Object> map = BodyParser.parse(form.asMap());
        if (!Validation.validateEvaluationResult(map)) {
            return Response.status(400).build();
        }

        final int result = evaluationMapper.saveResult(project.getId(), user.getId(), evaluation.getId(), map);
        if (result != 1) {
            return Response.status(400).build();
        }

        if (map.get("status").toString().toUpperCase().equals("PASSED")) {
            Map<String, Object> qualificationMap = new HashMap<>();
            qualificationMap.put("capability_id", evaluation.getCapability().getId());
            qualificationMap.put("score", map.get("score"));
            qualificationMapper.save(user.getId(), qualificationMap);
        }
        return Response.ok().build();
    }

    public String generateInstruction(String entryPoint) {
        List<String> cmds = new ArrayList<>();

        String account = String.format("%s --email=%s --password=%s", "http://cde.deepi.cn", generateUserName() + "@tw.com", generateUserName());
        cmds.add(String.format("cde register %s || cde login %s", account, account));
        cmds.add("ssh-keygen -f ~/.ssh/" + applicationName() + " -t rsa -N '' ");
        cmds.add("chmod 400 ~/.ssh/" + applicationName() + "* ");
        cmds.add("cde keys:add ~/.ssh/" + applicationName() + ".pub");

        cmds.add("eval `ssh-agent -s`");
        cmds.add("ssh-add ~/.ssh/" + applicationName());

        cmds.add(String.format("git clone https://github.com/aisensiy/%s.git %s", projectName(), applicationName()));
        cmds.add("cd " + applicationName());
        cmds.add(String.format("cde apps:create %s %s", applicationName(), stackName()));

//        cmds.add(String.format("git remote add evaluation-codebase %s", evaluationCodeUri()));
//        cmds.add("git subtree add --prefix=\"integration_test\" evaluation-codebase master");
        cmds.add("echo '" + generateMeta(entryPoint) + "' > manifest.json");
        cmds.add("rm -rf .git");
        cmds.add("git init && git add . && git commit -m 'Initial commit'");

        return cmds.stream().collect(Collectors.joining("\n"));
    }

    private String projectName() {
        return String.format("%s_%s", stackName(), evaluation.getCapability().getSolution().getName());
    }

    private String evaluationCodeUri() {
        return String.format("https://github.com/aisensiy/%s.git", evaluationRepositoryName());
    }

    private String getHost(@Context UriInfo uriInfo) {
        return uriInfo.getBaseUri().toString().substring(0, uriInfo.getBaseUri().toString().length() - 1);
    }

    private String stackName() {
        return evaluation.getCapability().getStack().getName().replace(" ", "");
    }

    private String evaluationRepositoryName() {
        return evaluation.getCapability().getStack().getName().replace(" ", "") + "_" + evaluation.getCapability().getSolution().getName().replace(" ", "");
    }

    private String generateMeta(String entryPoint) {
        return String.format("{\"evaluation_uri\": \"%s\"}", entryPoint + Routing.projectEvaluationResult(project.getId(), user.getId(), evaluation.getId()).toString());
    }

    private String generateUserName() {
        return user.getName() + "" + evaluation.getCreatedAt().getTime();
    }

    private String applicationName() {
        return evaluation.getCapability().getStack().getName().replace(" ", "") + "-"
                + evaluation.getCapability().getSolution().getName().replace(" ", "") + "-"
                + evaluation.getCreatedAt().getTime()
                + "-" + user.getId();
    }

}
