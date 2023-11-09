package by.dlstudio.cookbook.service;

import by.dlstudio.cookbook.dao.IngredientDAO;
import by.dlstudio.cookbook.dao.RecipeDAO;
import by.dlstudio.cookbook.entity.Ingredient;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.service.impl.IngredientServiceImpl;
import by.dlstudio.cookbook.util.HibernateTestUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IngredientServiceImplTest extends HibernateTestUtil {

    private static IngredientDAO ingredientDAO;
    private static RecipeDAO recipeDAO;
    private static IngredientServiceImpl ingredientService;

    @BeforeAll
    static void setUpDb() {
        recipeDAO = new RecipeDAO();
        ingredientDAO = new IngredientDAO();
        ingredientService = new IngredientServiceImpl(ingredientDAO);
    }

    @Test
    void redactIngredient() {
        Ingredient ingToRedact = new Ingredient();
        ingToRedact.setQuantity("51g");
        ingToRedact.setName("redact");

        Recipe recipe = new Recipe();
        recipe.setDescription("desc");
        recipe.setName("name");
        recipe = recipeDAO.create(recipe);
        ingToRedact.setRecipe(recipe);

        ingToRedact = ingredientDAO.create(ingToRedact);
        ingToRedact.setName("redacted");
        ingredientService.redactIngredient(ingToRedact);
        assertEquals(ingToRedact, ingredientDAO.findOne(ingToRedact.getId()).orElseThrow());
    }

    @Test
    void getIngredientById() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("name");
        ingredient.setQuantity("quantity");

        Recipe recipe = new Recipe();
        recipe.setDescription("desc");
        recipe.setName("name");
        recipe = recipeDAO.create(recipe);
        ingredient.setRecipe(recipe);

        ingredient = ingredientDAO.create(ingredient);
        assertEquals(ingredient, ingredientService.getIngredientById(ingredient.getId()).orElseThrow());
    }
}
