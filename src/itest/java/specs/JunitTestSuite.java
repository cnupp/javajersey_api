package specs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import specs.example.ProjectManagerTest;
import specs.example.StackUserTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//        StackOwnerTest.class,
        StackUserTest.class,
        ProjectManagerTest.class
})
public class JunitTestSuite {
}
