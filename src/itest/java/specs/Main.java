package specs;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import specs.util.RequestHelper;

import javax.ws.rs.core.Form;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {
        String entryPointOfTestTarget = System.getenv("ENTRYPOINT");
        Main instance = new Main();
        if (entryPointOfTestTarget == null) {
            System.err.println("ENTRYPOINT NOT SET");
            instance.exit(1, false);
        }
        Result result = JUnitCore.runClasses(JunitTestSuite.class);
        if (!result.getFailures().isEmpty()) {
            instance.exit(1, true);
        }

        instance.exit(0, true);
    }

    private void exit(int exitCode, boolean submitResult) throws IOException {
        if(submitResult) {
            Properties properties = readPropertiesFromJar();
            String entry_point_for_ketsu = properties.get("entry_point").toString();
            RequestHelper requestHelper = new RequestHelper(entry_point_for_ketsu);
            requestHelper.login();
            String cookie = requestHelper.login();
            long timeCost = calculateTimeCost(properties);
            Form form = new Form();
            form.param("status", exitCode == 0 ? "PASSED" : "FAILED");
            form.param("score", timeCost + "");

            String evaluation_uri = properties.get("evaluation_uri").toString();
            if (!requestHelper.submitResult(evaluation_uri, cookie, form)) {
                System.err.println("Commit Evaluation Result Failed");
            }
        }
        System.exit(exitCode);
    }

    private long calculateTimeCost(Properties properties) {
        Long first_commit_time = Long.valueOf(properties.get("first_commit") + "");
        Date startTime = new Date(first_commit_time * 1000);
        Date endTime = new Date();
        return (endTime.getTime() - startTime.getTime()) / 1000;
    }

    private Properties readPropertiesFromJar() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/meta.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }
}
