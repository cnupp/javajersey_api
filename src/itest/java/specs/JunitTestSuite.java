package specs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import specs.example.StackOwnerTest;
import specs.example.StackUserTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        StackOwnerTest.class,
        StackUserTest.class
})
public class JunitTestSuite {
}
