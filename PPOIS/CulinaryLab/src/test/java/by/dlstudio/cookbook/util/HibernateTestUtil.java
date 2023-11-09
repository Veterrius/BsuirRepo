package by.dlstudio.cookbook.util;

import by.dlstudio.cookbook.dao.utils.HibernateUtil;
import by.dlstudio.cookbook.entity.Cookbook;
import by.dlstudio.cookbook.entity.Ingredient;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class HibernateTestUtil {

    private static MockedStatic<HibernateUtil> mockedStatic;
    private static SessionFactory sessionFactory;
    private Session session;

    @BeforeAll
    static void setup() {
        try{
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate-test.cfg.xml")
                    .build();

            Metadata metadata = new MetadataSources(serviceRegistry)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Cookbook.class)
                    .addAnnotatedClass(Recipe.class)
                    .addAnnotatedClass(Ingredient.class)
                    .getMetadataBuilder()
                    .build();

            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
        mockedStatic = Mockito.mockStatic(HibernateUtil.class);
        Mockito.when(HibernateUtil.getSessionFactory()).thenReturn(sessionFactory);
    }

    @BeforeEach
    void setupThis() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void tearThis() {
        session.getTransaction().commit();
    }

    @AfterAll
    static void tear() {
        sessionFactory.close();
        mockedStatic.close();
    }
}
