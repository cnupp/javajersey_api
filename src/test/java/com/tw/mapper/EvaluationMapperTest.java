package com.tw.mapper;

import com.tw.domain.Capability;
import com.tw.domain.Evaluation;
import com.tw.domain.TestHelper;
import com.tw.mapper.util.PrimaryKey;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class EvaluationMapperTest extends MapperTestBase {

    private long projectId = 100L;
    private long userId = 100L;
    private Map<String, Object> solution;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        solution = TestHelper.solution(1L).toJson();
    }

    @Test
    public void test() throws Exception {
        PrimaryKey stackPrimaryKey = new PrimaryKey();
        stackRepository.create(TestHelper.stackToMap(TestHelper.stack(1L)), stackPrimaryKey);

        PrimaryKey solutionPrimaryKey = new PrimaryKey();
        solutionRepository.create(solution, solutionPrimaryKey);

        Map<String, Object> map = new HashMap<>();
        map.put("solution_id", solutionPrimaryKey.id);
        map.put("stack_id", stackPrimaryKey.id);
        capabilityMapper.save(projectId, map);

        Capability capability = capabilityMapper.findById(projectId, (long) map.get("id"));

        Map<String, Object> evaluationMap = new HashMap<>();
        evaluationMap.put("capability_id", capability.getId());
        evaluationMap.put("commit_uri", "test");
        String token = UUID.randomUUID().toString();
        evaluationMap.put("accessKey", token);
        evaluationMapper.save(projectId, userId, evaluationMap);

        final Evaluation evaluation = evaluationMapper.findById(projectId, userId, (long) evaluationMap.get("id"));
        assertThat(evaluation, not(nullValue()));
        assertThat(evaluation.getCapability(), not(nullValue()));
        assertThat(evaluation.getAccessKey(), is(token));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("score", 100);
        resultMap.put("status", "PASSED");
        evaluationMapper.saveResult(projectId, userId, evaluation.getId(), resultMap);

        assertThat(evaluationMap.get("id"), not(nullValue()));
    }

    @Test
    public void should_find_evaluations_belonged_to_user() throws Exception {
        final long projectId1 = 1L;
        long capabilityId1 = createCapability(projectId1);
        final long projectId2 = 2L;
        long capabilityId2 = createCapability(projectId2);

        Map<String, Object> evaluationMap1 = new HashMap<>();
        evaluationMap1.put("capability_id", capabilityId1);
        evaluationMap1.put("commit_uri", "test");

        Map<String, Object> evaluationMap2 = new HashMap<>();
        evaluationMap2.put("capability_id", capabilityId2);
        evaluationMap2.put("commit_uri", "test");
        String token = UUID.randomUUID().toString();
        evaluationMap2.put("accessKey", token);

        evaluationMapper.save(projectId1, userId, evaluationMap1);
        evaluationMapper.save(projectId2, userId, evaluationMap2);
        evaluationMapper.save(projectId2, 200L, evaluationMap2);

        List<Evaluation> evaluationsBelongedToUser1 = evaluationMapper.findEvaluationsBelongedToUser(userId);

        MatcherAssert.assertThat(evaluationsBelongedToUser1.size(), is(2));
        MatcherAssert.assertThat(evaluationsBelongedToUser1.get(1).getAccessKey(), is(token));

    }

    @Test
    public void should_find_evaluations_belonged_to_user_of_project() throws Exception {
        Map<String, Object> capabilityMap = new HashMap<>();
        capabilityMap.put("solution_id", 1L);
        capabilityMap.put("stack_id", 2L);
        capabilityMapper.save(projectId, capabilityMap);

        Map<String, Object> evaluationMap1 = new HashMap<>();
        evaluationMap1.put("capability_id", capabilityMap.get("id"));
        evaluationMap1.put("commit_uri", "test");

        Map<String, Object> evaluationMap2 = new HashMap<>();
        evaluationMap2.put("capability_id", 22222l);
        evaluationMap2.put("commit_uri", "test");
        evaluationMapper.save(projectId, userId, evaluationMap1);
        evaluationMapper.save(101L, userId, evaluationMap2);

        List<Evaluation> evaluationsBelongedToUser1 = evaluationMapper.findEvaluationsBelongedToProjectUser(projectId, userId);

        MatcherAssert.assertThat(evaluationsBelongedToUser1.size(), is(1));

    }
}