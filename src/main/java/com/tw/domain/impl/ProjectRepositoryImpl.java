package com.tw.domain.impl;

import com.tw.domain.ProjectRepository;
import com.tw.mapper.ProjectMapper;

import javax.inject.Inject;
import java.util.Map;

public class ProjectRepositoryImpl implements ProjectRepository{
    @Inject
    ProjectMapper projectMapper;

    public int saveProject(Map<String, Object> map) {
        return projectMapper.save(map);
    }
}
