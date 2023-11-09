package by.dlstudio.cookbook.dao;

import by.dlstudio.cookbook.entity.Cookbook;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.dao.abstr.AbstractHibernateDAO;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class RecipeDAO extends AbstractHibernateDAO<Recipe> {
    public RecipeDAO() {
        setClazz(Recipe.class);
    }

    /**
     * This method finds all recipes of the specified cookbook in a database
     * Query used in this method is a {@link jakarta.persistence.NamedQuery}
     * in a {@link Recipe} class
     * @param cookbookId is an id of the cookbook we want to find
     * @return List of found recipes
     */
    public List<Recipe> findRecipesOfCookbook(long cookbookId) {
        Session session = sessionFactory.openSession();
        return session
                .createNamedQuery("getRecipesByCookbookId", Recipe.class)
                .setParameter("id",cookbookId)
                .list();
    }
}
