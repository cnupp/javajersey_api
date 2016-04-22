package specs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import specs.example.StackOwnerTest;
import specs.example.ExampleTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        StackOwnerTest.class,
        ExampleTest.class
})
public class JunitTestSuite {
}
