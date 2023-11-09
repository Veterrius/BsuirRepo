package by.dlstudio.cookbook.controller;

import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest {

    @InjectMocks
    private RecipeController recipeController;

    @Mock
    private RecipeService recipeService;

    @Test
    void updateRecipeTest_Success() {
        Recipe recipeFromDb = new Recipe();
        recipeFromDb.setId(1);
        recipeFromDb.setDescription("desc");
        recipeFromDb.setName("rec");

        Recipe redactedRecipe = recipeFromDb;
        redactedRecipe.setName("redacted");

        Mockito.when(recipeService.getRecipeById(1)).thenReturn(Optional.of(recipeFromDb));
        recipeController.redactRecipe(1,redactedRecipe);

        Mockito.verify(recipeService).updateRecipe(redactedRecipe);
    }
}
