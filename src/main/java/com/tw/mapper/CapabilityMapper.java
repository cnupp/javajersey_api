package com.tw.mapper;

import com.tw.domain.Capability;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CapabilityMapper {
    int save(@Param("projectId") long projectId, @Param("map") Map<String, Object> map);

    List<Capability> find(@Param("projectId") long projectId, @Param("includeDeprecated") boolean includeDeprecated);

    Capability findById(@Param("projectId") long projectId, @Param("capabilityId") long capabilityId);

    int deprecate(@Param("capability") Capability capability);
}
