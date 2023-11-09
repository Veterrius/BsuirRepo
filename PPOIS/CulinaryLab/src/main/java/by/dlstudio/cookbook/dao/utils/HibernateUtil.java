package by.dlstudio.cookbook.dao.utils;

import by.dlstudio.cookbook.entity.Cookbook;
import by.dlstudio.cookbook.entity.Ingredient;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

/**
 * This class is a utility class which sets up configuration
 * for the Hibernate, adds annotated classes into context and
 * builds {@link SessionFactory} class
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;

    /**
     * This method creates a Hibernate session factory with specified configurations
     * @return built session factory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml")
                        .addAnnotatedClass(Recipe.class)
                        .addAnnotatedClass(Cookbook.class)
                        .addAnnotatedClass(User.class)
                        .addAnnotatedClass(Ingredient.class);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
