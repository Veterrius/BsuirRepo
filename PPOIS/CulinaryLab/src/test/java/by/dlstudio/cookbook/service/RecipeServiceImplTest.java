package by.dlstudio.cookbook.service;

import by.dlstudio.cookbook.dao.CookbookDAO;
import by.dlstudio.cookbook.dao.IngredientDAO;
import by.dlstudio.cookbook.dao.RecipeDAO;
import by.dlstudio.cookbook.entity.Cookbook;
import by.dlstudio.cookbook.entity.Ingredient;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.service.impl.CookbookServiceImpl;
import by.dlstudio.cookbook.service.impl.RecipeServiceImpl;
import by.dlstudio.cookbook.util.HibernateTestUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeServiceImplTest extends HibernateTestUtil {

    private static RecipeDAO recipeDAO;
    private static CookbookDAO cookbookDAO;
    private static IngredientDAO ingredientDAO;
    private static RecipeServiceImpl recipeService;
    private static CookbookServiceImpl cookbookService;

    @BeforeAll
    public static void setUpDb() {
        cookbookDAO = new CookbookDAO();
        recipeDAO = new RecipeDAO();
        ingredientDAO = new IngredientDAO();
        cookbookService = new CookbookServiceImpl(recipeDAO, cookbookDAO);
        recipeService = new RecipeServiceImpl(recipeDAO, ingredientDAO);
    }


    @Test
    void getRecipeByIdTest_Success() {
        Recipe recipe = new Recipe();
        recipe.setDescription("desc");
        recipe.setName("test");
        recipe = recipeDAO.create(recipe);
        assertEquals(recipe, recipeService.getRecipeById(recipe.getId()).orElseThrow());
    }


    @Test
    void createRecipeTest_Success() {
        Recipe recipe = new Recipe();
        recipe.setName("ads");
        recipe.setDescription("desc");
        recipe = recipeService.createRecipe(recipe);
        assertEquals(recipe, recipeDAO.findOne(recipe.getId()).orElseThrow());
    }

    @Test
    void getAllRecipesTest_Success() {
        Recipe addRec = new Recipe();
        addRec.setDescription("desc");
        addRec.setName("rec");
        addRec = recipeService.createRecipe(addRec);
        assertNotEquals(0, recipeService.getAllRecipes().size());
    }

    @Test
    void deleteRecipeTest_Success() {
        Recipe recipeToDelete = new Recipe();
        recipeToDelete.setName("deleted");
        recipeToDelete.setDescription("desc");
        recipeToDelete = recipeService.createRecipe(recipeToDelete);
        recipeService.deleteRecipe(recipeToDelete);
        assertFalse(recipeDAO.findOne(recipeToDelete.getId()).isPresent());
    }

    @Test
    void deleteRecipeWithCookbooksTest_Success() {
        Recipe recipeToDelete = new Recipe();
        recipeToDelete.setName("deleted");
        recipeToDelete.setDescription("desc");
        recipeToDelete = recipeService.createRecipe(recipeToDelete);
        Cookbook cookbook = new Cookbook();
        cookbook = cookbookDAO.create(cookbook);
        cookbook = cookbookService.addRecipeToCookbook(recipeToDelete, cookbook);
        recipeToDelete = recipeDAO.findOne(recipeToDelete.getId()).orElseThrow();

        recipeDAO.delete(recipeToDelete);

        assertFalse(recipeDAO.findOne(recipeToDelete.getId()).isPresent());
        assertEquals(0, cookbookService.getRecipesOfCookbook(cookbook).size());
    }

    @Test
    void updateRecipeTest_Success() {
        Recipe recipeToUpdate = new Recipe();
        recipeToUpdate.setDescription("desc");
        recipeToUpdate.setName("update");
        recipeToUpdate = recipeDAO.create(recipeToUpdate);
        recipeToUpdate.setName("updated");
        recipeService.updateRecipe(recipeToUpdate);
        assertEquals(recipeToUpdate, recipeDAO.findOne(recipeToUpdate.getId()).orElseThrow());
    }


    @Test
    void addIngredientToRecipeAndRemoveItTest_Success() {
        Ingredient ingredient = new Ingredient();
        ingredient.setQuantity("500g");
        ingredient.setName("ing");

        Recipe recipe = new Recipe();
        recipe.setName("haveIng");
        recipe.setDescription("desc");
        recipe = recipeDAO.create(recipe);

        recipe = recipeService.addIngredientToRecipe(recipe, ingredient);
        ingredient = recipeService.getAllRecipeIngredients(recipe).get(0);
        assertEquals(ingredient, recipe.getIngredients().stream().findFirst().orElseThrow());

        recipe = recipeService.removeIngredientFromRecipe(recipe, ingredient);
        assertFalse(recipeDAO.findOne(recipe.getId()).orElseThrow().getIngredients().contains(ingredient));

    }

    @Test
    void getAllRecipeIngredientsTest_Success() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("inRecipe");
        ingredient.setQuantity("500g");

        Recipe recipe = new Recipe();
        recipe.setName("contIng");
        recipe.setDescription("desc");
        recipe = recipeDAO.create(recipe);
        recipe = recipeService.addIngredientToRecipe(recipe, ingredient);

        assertNotEquals(0, recipeService.getAllRecipeIngredients(recipe).size());
    }
}
