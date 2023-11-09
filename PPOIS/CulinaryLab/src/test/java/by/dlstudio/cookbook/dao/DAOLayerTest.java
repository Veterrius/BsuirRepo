package by.dlstudio.cookbook.dao;

import by.dlstudio.cookbook.dao.utils.HibernateUtil;
import by.dlstudio.cookbook.entity.Cookbook;
import by.dlstudio.cookbook.entity.Ingredient;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.entity.User;
import by.dlstudio.cookbook.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class DAOLayerTest extends HibernateTestUtil {

    private static UserDAO userDAO;
    private static CookbookDAO cookbookDAO;
    private static IngredientDAO ingredientDAO;
    private static RecipeDAO recipeDAO;

    @BeforeAll
    static void setUpDb() {
        userDAO = new UserDAO();
        cookbookDAO = new CookbookDAO();
        ingredientDAO = new IngredientDAO();
        recipeDAO = new RecipeDAO();
    }


    @Test
    void createUserTest_Success() {
        User user = new User();
        user.setUsername("created");
        user = userDAO.create(user);
        assertNotNull(user.getId());
    }

    @Test
    void getUserByNameTest_Success() {
        User expUser = new User();
        expUser.setUsername("lol");
        expUser = userDAO.create(expUser);
        assertEquals(expUser, userDAO.findUserByName("lol").get());
    }

    @Test
    void getUserByNameTest_Exception() {
        assertThrows(NoSuchElementException.class, () -> {
            userDAO.findUserByName("asdasd").orElseThrow();
        });
    }

    @Test
    void updateUserTest_Success() {
        User user = new User();
        user.setUsername("name1");
        user = userDAO.create(user);
        user.setUsername("changedName");
        userDAO.update(user);
        assertEquals(user, userDAO.findOne(user.getId()).orElseThrow());
    }

    @Test
    void deleteUserByIdTest_Success() {
        User user = new User();
        user.setUsername("delete");
        user = userDAO.create(user);
        userDAO.deleteById(user.getId());
        assertFalse(userDAO.findOne(user.getId()).isPresent());
    }

    @Test
    void deleteUserTest_Success() {
        User user = new User();
        user.setUsername("delete");
        user = userDAO.create(user);
        userDAO.delete(user);
        assertFalse(userDAO.findOne(user.getId()).isPresent());
    }

    @Test
    void getAllUsersTest_Success() {
        User user = new User();
        user.setUsername("+1");
        userDAO.create(user);
        assertNotEquals(0,userDAO.findAll().size());
    }

    @Test
    void findCookbookByUserTest_Success() {
        User cbTest = new User();
        cbTest.setUsername("cbTest");
        Cookbook cookbook = new Cookbook();
        cookbook.setUser(cbTest);
        cbTest.setCookbook(cookbook);
        cbTest = userDAO.create(cbTest);
        assertTrue(cookbookDAO.findCookbookByUser(cbTest).isPresent());
    }

    @Test
    void findCookbookByUserTest_NotFound() {
        assertFalse(cookbookDAO.findCookbookByUser(new User()).isPresent());
    }

    @Test
    void findAllRecipeIngredientsTest_Success() {
        Recipe recipe = new Recipe();
        recipe.setName("testRecipe");
        recipe.setDescription("desc");
        Ingredient ingredient = new Ingredient();
        ingredient.setQuantity("500s");
        ingredient.setName("test");
        ingredient.setRecipe(recipe);
        recipe.setIngredients(Collections.singleton(ingredient));
        recipeDAO.create(recipe);
        ingredientDAO.create(ingredient);
        assertNotEquals(0,ingredientDAO.findAllRecipeIngredients(recipe).size());
    }

    @Test
    void findRecipesOfCookbookTest_Success() {
        User cbTest = new User();
        cbTest.setUsername("recipeTest");
        Cookbook cookbook = new Cookbook();
        cookbook.setUser(cbTest);
        cbTest.setCookbook(cookbook);
        cbTest = userDAO.create(cbTest);
        Recipe recipe = new Recipe();
        recipe.setName("testRecipe");
        recipe.setDescription("desc");
        cookbook = cookbookDAO.findCookbookByUser(cbTest).orElseThrow();
        recipe.setCookbooks(Collections.singleton(cookbook));
        cookbook.setRecipes(Collections.singleton(recipe));
        cookbookDAO.update(cookbook);
        recipeDAO.create(recipe);
        assertNotEquals(0, recipeDAO.findRecipesOfCookbook(cookbook.getId()).size());
    }
}

