package dao;

import org.hibernate.Session;
import pojo.entity.TestEntity;

public class TestDAO extends SimpleDAO<TestEntity> {

    private static final String TEST_NAME_PARAMETER = "testName";
    private static final String PACKAGE_NAME_PARAMETER = "packageName";
    private static final String ID_SUITE_PARAMETER = "idSuite";

    public TestDAO() {
        super(TestEntity.class);
    }

    public TestEntity find(Session session, Long idSuite, String testName, String packageName) {
        return session.createQuery(" from test as t where" +
            "t.test_name = :" + TEST_NAME_PARAMETER + " and " +
            "t.package_name = :" + PACKAGE_NAME_PARAMETER + " and " +
            "t.fk_test_suite_entity_id = :" + ID_SUITE_PARAMETER, TestEntity.class)
            .setParameter(TEST_NAME_PARAMETER, testName)
            .setParameter(PACKAGE_NAME_PARAMETER, packageName)
            .setParameter(ID_SUITE_PARAMETER, idSuite)
            .uniqueResult();
    }
}
