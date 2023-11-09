package by.dlstudio.cookbook.dao;

import by.dlstudio.cookbook.dao.abstr.AbstractHibernateDAO;
import by.dlstudio.cookbook.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class UserDAO extends AbstractHibernateDAO<User> {

    public UserDAO() {
        setClazz(User.class);
    }

    /**
     * This method find user in a database by its name
     * Query used in this method is a {@link jakarta.persistence.NamedQuery}
     * in a {@link User} class
     * @param username is a username by which we find the user.
     * @return Optional of the found user.
     */
    public Optional<User> findUserByName(String username) {
        Session session = sessionFactory.openSession();
        return session
                .createNamedQuery("getUserByUsername", User.class)
                .setParameter("username",username)
                .uniqueResultOptional();
    }
}
