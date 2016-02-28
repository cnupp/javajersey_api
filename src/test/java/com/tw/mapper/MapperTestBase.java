package com.tw.mapper;

import com.tw.domain.TestHelper;
import com.tw.mapper.util.PrimaryKey;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

public class MapperTestBase {
    protected SqlSession sqlSession;
    protected CapabilityMapper capabilityMapper;
    protected ProjectMapper projectMapper;
    protected StackRepository stackRepository;
    protected SolutionRepository solutionRepository;
    protected EvaluationMapper evaluationMapper;
    protected QualificationMapper qualificationMapper;
    protected UserRepository userRepository;
    protected ServiceMapper serviceMapper;

    @Before
    public void setUp() throws Exception {
        sqlSession = MyBatisUtil.getSqlSessionFactory().openSession();
        capabilityMapper = sqlSession.getMapper(CapabilityMapper.class);
        projectMapper = sqlSession.getMapper(ProjectMapper.class);
        stackRepository = sqlSession.getMapper(StackRepository.class);
        solutionRepository = sqlSession.getMapper(SolutionRepository.class);
        evaluationMapper = sqlSession.getMapper(EvaluationMapper.class);
        qualificationMapper = sqlSession.getMapper(QualificationMapper.class);
        userRepository = sqlSession.getMapper(UserRepository.class);
        serviceMapper = sqlSession.getMapper(ServiceMapper.class);
    }

    @After
    public void tearDown() throws Exception {
        sqlSession.rollback();
        sqlSession.close();
    }

    protected long createCapability(long projectId) {

        PrimaryKey stackPrimaryKey = new PrimaryKey();
        stackRepository.create(TestHelper.stackToMap(TestHelper.stack(1L)), stackPrimaryKey);

        PrimaryKey solutionPrimaryKey = new PrimaryKey();
        Map<String, Object> solution1 = TestHelper.solution(1L).toJson();
        solutionRepository.create(solution1, solutionPrimaryKey);

        Map<String, Object> capabilityMap1 = new HashMap<>();
        capabilityMap1.put("solution_id", solutionPrimaryKey.id);
        capabilityMap1.put("stack_id", stackPrimaryKey.id);
        capabilityMapper.save(projectId, capabilityMap1);
        return (long) capabilityMap1.get("id");
    }
}
