package com.tw.support;

import com.google.inject.AbstractModule;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionManager;
import org.junit.rules.TestRule;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import javax.inject.Inject;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class ApiTestRunner extends InjectBasedRunner {
    @Inject
    private SqlSessionFactory sqlSessionFactory;

    @Inject
    private SqlSessionManager sqlSessionManager;

    public ApiTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    private final TestRule removeAllData = (base, description) -> new Statement() {
        @Override
        public void evaluate() throws Throwable {
            try {
                base.evaluate();
            } finally {
                SqlSession sqlSession = sqlSessionFactory.openSession();
                Connection connection = sqlSession.getConnection();
                java.sql.Statement statement = connection.createStatement();
                // Take care of the order for delete operations, eg.
                // field in table A has reference for table B, then A should be deleted first
                // otherwise exception will occur and database will be broken,
                // remember to clean database manually before running tests when exception happens

                statement.executeUpdate("DELETE FROM customers");
                statement.close();
                connection.commit();
            }
        }
    };

    @Override
    protected List<AbstractModule> getModules() {
        return asList();
    }

    @Override
    protected List<TestRule> getTestRules(Object target) {
        List<TestRule> rules = new ArrayList<>();
        rules.add(removeAllData);
        rules.addAll(super.getTestRules(target));
        return rules;
    }
}
