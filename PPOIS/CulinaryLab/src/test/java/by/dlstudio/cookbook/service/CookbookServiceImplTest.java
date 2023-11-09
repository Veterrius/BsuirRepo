package by.dlstudio.cookbook.service;

import by.dlstudio.cookbook.dao.CookbookDAO;
import by.dlstudio.cookbook.dao.RecipeDAO;
import by.dlstudio.cookbook.dao.UserDAO;
import by.dlstudio.cookbook.entity.Cookbook;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.entity.User;
import by.dlstudio.cookbook.service.impl.CookbookServiceImpl;
import by.dlstudio.cookbook.service.impl.UserServiceImpl;
import by.dlstudio.cookbook.util.HibernateTestUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CookbookServiceImplTest extends HibernateTestUtil {

    private static RecipeDAO recipeDAO;
    private static CookbookServiceImpl cookbookService;
    private static UserServiceImpl userService;

    @BeforeAll
    public static void setUpDb() {
        CookbookDAO cookbookDAO = new CookbookDAO();
        recipeDAO = new RecipeDAO();
        UserDAO userDAO = new UserDAO();
        userService = new UserServiceImpl(userDAO, cookbookDAO);
        cookbookService = new CookbookServiceImpl(recipeDAO, cookbookDAO);
    }


    @Test
    void getCookbookByUserTest_Success() {
        User user = new User();
        user.setUsername("user");
        user = userService.createUser(user);
        assertEquals(user.getCookbook(), cookbookService.getCookbookByUser(user).orElseThrow());
    }

    @Test
    void addRecipeToCookbookAndRemoveItTest_Success() {
        Recipe recipe = new Recipe();
        recipe.setDescription("desc");
        recipe.setName("rec");
        recipe = recipeDAO.create(recipe);

        User user = new User();
        user.setUsername("cbTest");
        user = userService.createUser(user);

        Cookbook userCookbook = cookbookService.getCookbookByUser(user).orElseThrow();

        userCookbook = cookbookService.addRecipeToCookbook(recipe, userCookbook);
        assertTrue(userCookbook.getRecipes().contains(recipe));

        userCookbook = cookbookService.removeRecipeFromCookbook(recipe, userCookbook);

        assertFalse(userCookbook.getRecipes().contains(recipe));
    }

    @Test
    void getCookbookRecipes_AllAndWithName_Success() {
        User user = new User();
        user.setUsername("testRec");
        user = userService.createUser(user);

        Recipe recipe = new Recipe();
        recipe.setName("1");
        recipe.setDescription("desc1");
        Recipe recipe1 = new Recipe();
        recipe1.setName("1");
        recipe1.setDescription("desc2");

        recipe1 = recipeDAO.create(recipe1);
        recipe = recipeDAO.create(recipe);

        cookbookService.addRecipeToCookbook(recipe, user.getCookbook());
        cookbookService.addRecipeToCookbook(recipe1, user.getCookbook());

        assertEquals(2,cookbookService
                .getCookbookRecipesWithName("1", user.getCookbook()).size());
        assertEquals(2, cookbookService.getRecipesOfCookbook(user.getCookbook()).size());
    }
}
