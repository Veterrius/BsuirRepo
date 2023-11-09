package by.dlstudio.cookbook.controller;

import by.dlstudio.cookbook.entity.Cookbook;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.entity.User;
import by.dlstudio.cookbook.service.CookbookService;
import by.dlstudio.cookbook.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class CookbookControllerTest {

    @InjectMocks
    private CookbookController cookbookController;
    @Mock
    private CookbookService cookbookService;
    @Mock
    private RecipeService recipeService;

    @Test
    void addRecipeToCookbookAndRemoveItTest_Success() {
        User principal = new User();
        principal.setUsername("princ");
        Cookbook userCookbook = new Cookbook();
        userCookbook.setId(1);
        userCookbook.setUser(principal);
        Recipe recipeToAdd = new Recipe();
        recipeToAdd.setName("name");
        recipeToAdd.setId(1);
        recipeToAdd.setDescription("desc");

        Mockito.when(cookbookService.getCookbookByUser(principal)).thenReturn(Optional.of(userCookbook));
        Mockito.when(recipeService.getRecipeById(1)).thenReturn(Optional.of(recipeToAdd));

        cookbookController.addRecipeToCookbook(1,principal);
        Mockito.verify(cookbookService).addRecipeToCookbook(recipeToAdd, userCookbook);

        cookbookController.removeRecipeFromCookbook(1,principal);
        Mockito.verify(cookbookService).removeRecipeFromCookbook(recipeToAdd,userCookbook);
    }

    @Test
    void showAllCookbookRecipes_AndWithName_Success() {
        User principal = new User();
        principal.setId(1);
        principal.setUsername("princ");
        Cookbook pCb = new Cookbook();
        pCb.setUser(principal);
        pCb.setId(1);

        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setDescription("desc");
        recipe.setName("1");
        Recipe recipe1 = new Recipe();
        recipe1.setId(2);
        recipe1.setDescription("desc1");
        recipe1.setName("1");
        pCb.setRecipes(Set.of(recipe, recipe1));

        Mockito.when(cookbookService.getCookbookByUser(principal)).thenReturn(Optional.of(pCb));
        Mockito.when(cookbookService.getCookbookRecipesWithName("1",pCb))
                .thenReturn(pCb.getRecipes().stream().toList());
        Mockito.when(cookbookService.getRecipesOfCookbook(pCb))
                .thenReturn(pCb.getRecipes().stream().toList());

        assertEquals(2,cookbookController.showAllCookbookRecipesWithName(principal, "1").size());
        assertEquals(2,cookbookController.showAllCookbookRecipes(principal).size());
    }
}
