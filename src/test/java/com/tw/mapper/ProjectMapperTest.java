package com.tw.mapper;

import com.tw.domain.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class ProjectMapperTest extends MapperTestBase {

    private Map<String, Object> projectMap;
    private Map<String, Object> assignMap;
    private String userId;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        projectMap = TestHelper.project(1L).toJson();
        userId = "100";

        assignMap = new HashMap<>();
        assignMap.put("user_id", userId);
        assignMap.put("starts_at", "2015-12-11");
        assignMap.put("ends_at", "2015-12-13");
    }

    private Map<String, Object> employeeMap(long userId) {
        return TestHelper.user(userId, Role.EMPLOYEE).toJson();
    }

    private Map<String, Object> managerMap(long userId) {
        return TestHelper.projectManager(userId).toJson();
    }

    private Map<String, Object> assignmentMap(long userId) {
        Assignment assignment = TestHelper.assignment(userId, TestHelper.project(1L));
        return assignment.toJson();
    }

    @Test
    public void should_save_project() throws Exception {
        projectMapper.save(projectMap);
        assertThat(projectMap.get("id"), not(nullValue()));
    }

    @Test
    public void should_find_project_by_id() throws Exception {
        projectMapper.save(projectMap);

        Project project = projectMapper.findById((Long) projectMap.get("id"));
        assertThat(project.getId(), is(projectMap.get("id")));
    }

    @Test
    public void should_assign_user_to_project() throws Exception {
        final int result = projectMapper.assignUser(1L, assignMap);
        assertThat(result, is(1));
    }

    @Test
    public void should_get_user_by_id_from_project() throws Exception {
        projectMapper.save(projectMap);

        final Map<String, Object> userMap = TestHelper.user(1L, Role.EMPLOYEE).toJson();
        userRepository.createUser(userMap);

        projectMapper.assignUser(100L, assignmentMap(1L));
        User user = projectMapper.findUserById(100, 1);
        assertThat(user.getId(), is(1L));
    }

    @Test
    public void should_get_project_managers_projects() throws Exception {
        projectMapper.save(projectMap);

        Map<String, Object> userMap = managerMap(Long.parseLong(userId));

        userRepository.createUser(userMap);

        projectMapper.assignUser((Long) projectMap.get("id"), assignMap);

        List<Project> projects = projectMapper.findUserProjects(100);
        assertThat(projects.size(), is(1));
        assertThat(projects.get(0).getId(), is(projectMap.get("id")));
    }

    @Test
    public void should_get_project_members() throws Exception {
        int employeeId = 1011;
        projectMapper.save(projectMap);
        long projectId = (long) projectMap.get("id");

        Map<String, Object> userMap1 = managerMap(1001);
        userRepository.createUser(userMap1);

        Map<String, Object> userMap2 = employeeMap(employeeId);
        userRepository.createUser(userMap2);

        projectMapper.assignUser(projectId, assignmentMap((Long) userMap1.get("id")));
        projectMapper.assignUser(projectId, assignmentMap((Long) userMap2.get("id")));

        Map<String, Object> qualificationMap = new HashMap<>();
        qualificationMap.put("capability_id", "5");
        qualificationMapper.save(employeeId, qualificationMap);

        final List<User> users = projectMapper.findUsers(projectId);
        assertThat(users.size(), is(2));
    }

    @Test
    public void should_get_project_assignments() throws Exception {
        projectMapper.save(projectMap);

        Map<String, Object> userMap = employeeMap(Long.parseLong(userId));

        userRepository.createUser(userMap);

        projectMapper.assignUser((Long) projectMap.get("id"), assignMap);

        final List<Assignment> assignments = projectMapper.findUserAssignments(Long.parseLong(userId));
        assertThat(assignments.size(), is(1));
        assertThat(assignments.get(0).getProject().getId(), is(projectMap.get("id")));
    }
}
