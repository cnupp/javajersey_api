package com.tw.resources;

import com.tw.core.Customer;
import com.tw.core.CustomerRepository;
import com.tw.support.ApiSupport;
import com.tw.support.ApiTestRunner;
import com.tw.support.TestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(ApiTestRunner.class)
public class CustomersResourceTest extends ApiSupport{
    @Inject
    CustomerRepository customerRepository;

    @Test
    public void should_register_new_user() throws Exception {
        final Map<String, Object> customer = TestHelper.customer();
        final Response response = post("/customers", customer);
        assertThat(response.getStatus(), is(201));
        assertThat(Pattern.matches(".*/customers/[a-z0-9-]*", response.getLocation().toASCIIString()), is(true));
    }

    @Test
    public void should_get_customer_detail() throws Exception {
        final Customer customer = customerRepository.register(TestHelper.customer());
        final Map<String, Object> userMap = new HashMap<String, Object>() {{
            put("id", customer.getId());
        }};
        when(session.get("user")).thenReturn(userMap);

        final Response response = get(String.format("/customers/%s", customer.getId()));
        assertThat(response.getStatus(), is(200));
        final Map result = response.readEntity(Map.class);
        assertThat(result.get("username"), is("guest"));
        assertThat(result.get("email"), is("guest@test.com"));

    }

}
