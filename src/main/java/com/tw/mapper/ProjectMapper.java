package com.tw.mapper;

import com.tw.domain.Assignment;
import com.tw.domain.Project;
import com.tw.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProjectMapper {
    int save(@Param("map") Map<String, Object> map);

    Project findById(@Param("id") long id);

    int assignUser(@Param("projectId") long projectId, @Param("map") Map<String, Object> map);

    User findUserById(@Param("projectId") long projectId, @Param("userId") long userId);

    List<Project> findUserProjects(@Param("userId") long userId);

    List<User> findUsers(@Param("projectId") long projectId);

    List<Assignment> findUserAssignments(@Param("userId") long userId);
}
