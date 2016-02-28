package com.tw.api;

import com.tw.domain.Role;
import com.tw.domain.TestHelper;
import com.tw.domain.User;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

public class AuthenticationApiTest extends ApiTestBase {
    @Test
    public void should_authenticate_user() throws Exception {
        User user = TestHelper.user(1L, Role.EMPLOYEE);
        when(userRepository.findUserByName(anyObject())).thenReturn(user);

        Form form = new Form();
        form.param("user_name", "abc");

        final Response response = target("/authentication").request().post(Entity.form(form));
        assertThat(response.getStatus(), is(200));
    }
}
