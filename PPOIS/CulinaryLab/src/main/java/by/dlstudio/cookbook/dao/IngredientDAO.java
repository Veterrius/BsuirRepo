package by.dlstudio.cookbook.dao;

import by.dlstudio.cookbook.entity.Ingredient;
import by.dlstudio.cookbook.dao.abstr.AbstractHibernateDAO;
import by.dlstudio.cookbook.entity.Recipe;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class IngredientDAO extends AbstractHibernateDAO<Ingredient> {
    public IngredientDAO() {
        setClazz(Ingredient.class);
    }

    /**
     * This methods finds all recipe's ingredients in a database
     * Query used in this method is a {@link jakarta.persistence.NamedQuery}
     * in a {@link Ingredient} class
     * @param recipe is a recipe from which we want to obtain the ingredients
     * @return List of found ingredients
     */
    public List<Ingredient> findAllRecipeIngredients(Recipe recipe) {
        Session session = sessionFactory.openSession();
        return session
                .createNamedQuery("getAllIngredientsOfRecipe", Ingredient.class)
                .setParameter("recipe",recipe)
                .list();
    }
}
