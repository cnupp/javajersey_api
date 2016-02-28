package com.tw.mapper;

import com.tw.domain.Solution;
import com.tw.domain.TestHelper;
import com.tw.mapper.util.PrimaryKey;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SolutionRepositoryTest extends MapperTestBase {

    private SolutionRepository solutionRepository;
    private Solution solution1;
    private Solution solution2;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        solutionRepository = sqlSession.getMapper(SolutionRepository.class);
        solution1 = TestHelper.solution(1L);
        solution2 = TestHelper.solution(2L);
    }

    @Test
    public void should_create_a_new_solution() throws Exception {
        PrimaryKey primaryKey = new PrimaryKey();
        int affectedRows = solutionRepository.create(TestHelper.solution(1L).toJson(), primaryKey);

        assertThat(affectedRows, is(1));
    }

    @Test
    public void should_find_all_solutions() throws Exception {
        solutionRepository.create(solution1.toJson(), new PrimaryKey());
        solutionRepository.create(solution2.toJson(), new PrimaryKey());

        List<Solution> solutions = solutionRepository.getSolutions();

        assertThat(solutions.size(), is(2));
        assertThat(solutions.get(0).getName(), is(solution1.getName()));
        assertThat(solutions.get(1).getName(), is(solution2.getName()));
    }

    @Test
    public void should_get_solution_by_id() throws Exception {
        PrimaryKey primaryKey = new PrimaryKey();
        solutionRepository.create(solution1.toJson(), primaryKey);

        Solution solution = solutionRepository.getSolutionById(primaryKey.id);

        assertThat(solution.getName(), is(solution1.getName()));
        assertThat(solution.getDescription(), is(solution1.getDescription()));

    }
}
