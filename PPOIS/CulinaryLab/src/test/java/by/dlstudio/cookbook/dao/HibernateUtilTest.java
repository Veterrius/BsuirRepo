package by.dlstudio.cookbook.dao;

import by.dlstudio.cookbook.dao.utils.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HibernateUtilTest {

    @Test
    void getSessionFactory_Test() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        assertNotNull(sessionFactory);
    }

}
