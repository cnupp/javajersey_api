package com.tw.domain;

import java.util.Map;

public interface ProjectRepository {
    int saveProject(Map<String, Object> map);
}
