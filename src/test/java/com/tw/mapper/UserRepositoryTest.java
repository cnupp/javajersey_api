package com.tw.mapper;

import com.tw.domain.Role;
import com.tw.domain.User;
import org.apache.ibatis.exceptions.PersistenceException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserRepositoryTest extends MapperTestBase{
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        userRepository = sqlSession.getMapper(UserRepository.class);
    }

    @Test
    public void should_create_a_new_user() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "1234");
        map.put("name", "Zhang Xiang");
        map.put("role", Role.EMPLOYEE);
        int affectedRow = userRepository.createUser(map);
        assertThat(affectedRow, is(1));
    }

    @Test
    public void should_not_create_two_users_with_same_id() throws Exception {
        expectedEx.expect(PersistenceException.class);
        expectedEx.expectMessage("Duplicate entry '1234' for key 'refId'");

        long userId = 1234l;

        Map<String, Object> mapForUser1 = new HashMap<>();
        mapForUser1.put("id", userId);
        mapForUser1.put("name", "Zhang Xiang");
        mapForUser1.put("role", Role.EMPLOYEE);
        int affectedRow = userRepository.createUser(mapForUser1);
        assertThat(affectedRow, is(1));

        Map<String, Object> mapForUser2 = new HashMap<>();
        mapForUser2.put("id", userId);
        mapForUser2.put("name", "Li Xing");
        mapForUser2.put("role", Role.EMPLOYEE);
        userRepository.createUser(mapForUser2);
    }

    @Test
    public void should_find_user_by_id() throws Exception {
        String name = "Zhang Xiang";
        long userId = 1234l;

        Map<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("name", name);
        map.put("role", Role.EMPLOYEE);
        userRepository.createUser(map);

        User user = userRepository.findUserById((Long) map.get("id"));
        assertThat(user.getName(), is(name));
        assertThat(user.getId(), is(userId));
        assertThat(user.getRole(), is(Role.EMPLOYEE));

    }

    @Test
    public void should_find_user_by_name() throws Exception {
        String name = "Zhang Xiang";
        long userId = 1234l;

        Map<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("name", name);
        map.put("role", Role.EMPLOYEE);
        userRepository.createUser(map);

        User user = userRepository.findUserByName(name);
        assertThat(user.getName(), is(name));
        assertThat(user.getId(), is(userId));
        assertThat(user.getRole(), is(Role.EMPLOYEE));
    }
}
