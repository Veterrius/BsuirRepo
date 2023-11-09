package by.dlstudio.cookbook.dao;

import by.dlstudio.cookbook.dao.abstr.AbstractHibernateDAO;
import by.dlstudio.cookbook.entity.Cookbook;
import by.dlstudio.cookbook.entity.User;
import org.hibernate.Session;

import java.util.Optional;

public class CookbookDAO extends AbstractHibernateDAO<Cookbook> {
    public CookbookDAO() {
        setClazz(Cookbook.class);
    }

    /**
     * This method finds a {@link Cookbook} entity in a database by it's {@link User}
     * Query used in this method is a {@link jakarta.persistence.NamedQuery}
     * in a {@link Cookbook} class
     * @param user is a User whose cookbook we want to obtain
     * @return {@link Optional} of found cookbook.
     */
    public Optional<Cookbook> findCookbookByUser(User user) {
        Session session = sessionFactory.openSession();
        return session
                .createNamedQuery("getCookbookByUser", Cookbook.class)
                .setParameter("user", user)
                .uniqueResultOptional();
    }
}
