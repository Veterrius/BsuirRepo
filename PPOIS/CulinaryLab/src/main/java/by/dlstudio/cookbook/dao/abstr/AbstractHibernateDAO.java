package by.dlstudio.cookbook.dao.abstr;

import by.dlstudio.cookbook.dao.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * This class is a generic abstract class that realizes basic Hibernate CRUD methods
 * and also findOne() and findAll() methods.
 * This class uses {@link HibernateUtil} to obtain {@link SessionFactory} object.
 * @param <T> is a class of an Entity used in DAO that extends this class
 */
public abstract class AbstractHibernateDAO<T extends Serializable> {

    private Class<T> clazz;

    protected SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    /**
     * This method replaces T with an Entity class.
     * @param clazzToSet is an Entity class which will be operated in CRUD methods
     */
    public final void setClazz(final Class<T> clazzToSet) {
        clazz = clazzToSet;
    }

    /**
     * This method finds a specified Entity object in a database
     * @param id is an id of the object we want to find in the database
     * @return Optional of an Entity that was found
     */
    public Optional<T> findOne(final long id) {
        Optional<T> entityFromDb;
        Session session = sessionFactory.openSession();
        if (session.get(clazz, id) != null) {
            entityFromDb = Optional.of(session.get(clazz, id));
        } else entityFromDb = Optional.empty();
        session.close();
        return entityFromDb;
    }

    /**
     * This methods finds all objects of a specified Entity class
     * that exist in the database
     * @return List of found objects
     */
    public List<T> findAll() {
        List<T> resultList;
        Session session = sessionFactory.openSession();
        resultList = session.createQuery("from " + clazz.getName(), clazz).list();
        session.close();
        return resultList;
    }

    /**
     * This method creates an entity in a database
     * @param entity is an Entity we want to create
     * @return created Entity
     */
    public T create(final T entity) {
        sessionFactory.inTransaction(session -> {
            session.persist(entity);
        });
        return entity;
    }

    /**
     * This method updates an entity in a database
     * @param entity is an Entity we want to update
     * @return updated Entity
     */
    public T update(final T entity) {;
        sessionFactory.inTransaction(session -> {
             session.merge(entity);
        });
        return entity;
    }

    /**
     * This method removes an Entity from the database
     * @param entity is an Entity we want to remove
     */
    public void delete(final T entity) {
        sessionFactory.inTransaction(session -> {
            session.remove(entity);
        });
    }

    /**
     * This method removes an Entity from the database by it's Id,
     * firstly finding the Entity
     * @param entityId is an Id of the Entity we want to delete
     */
    public void deleteById(final long entityId) {
        final T entityFromDb = findOne(entityId).orElseThrow();
        delete(entityFromDb);
    }


}
