package com.tw.mapper;

import com.tw.domain.Service;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ServiceMapper {
    int save(@Param("map") Map<String, Object> map);

    List<Service> findServicesForProject(long id);
}
