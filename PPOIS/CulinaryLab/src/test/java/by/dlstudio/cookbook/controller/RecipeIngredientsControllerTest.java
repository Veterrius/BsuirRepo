package by.dlstudio.cookbook.controller;

import by.dlstudio.cookbook.entity.Ingredient;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.service.IngredientService;
import by.dlstudio.cookbook.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class RecipeIngredientsControllerTest {

    @InjectMocks
    private RecipeIngredientsController RINGController;
    @Mock
    private RecipeService recipeService;
    @Mock
    private IngredientService ingredientService;


    @Test
    void showAllIngredientsOfRecipeTest_Success() {
        Recipe recipe = new Recipe();
        recipe.setName("rec");
        recipe.setDescription("desc");
        recipe.setId(1);

        Mockito.when(recipeService.getRecipeById(1)).thenReturn(Optional.of(recipe));
        RINGController.showAllIngredientsOfRecipe(1);

        Mockito.verify(recipeService).getAllRecipeIngredients(recipe);
    }

    @Test
    void addAndRemoveIngredientFromRecipeTest_Success() {
        Recipe recipe = new Recipe();
        recipe.setName("rec");
        recipe.setDescription("desc");
        recipe.setId(1);

        Ingredient ingredient = new Ingredient();
        ingredient.setName("name");
        ingredient.setQuantity("500g");
        ingredient.setId(1);

        Mockito.when(recipeService.getRecipeById(1)).thenReturn(Optional.of(recipe));
        RINGController.addIngredientToRecipe(1,ingredient);
        Mockito.verify(recipeService).addIngredientToRecipe(recipe,ingredient);

        ingredient.setRecipe(recipe);
        recipe.setIngredients(Set.of(ingredient));

        Mockito.when(recipeService.getRecipeById(1)).thenReturn(Optional.of(recipe));
        Mockito.when(ingredientService.getIngredientById(1))
                .thenReturn(Optional.of(ingredient));
        RINGController.removeIngredientFromRecipe(1,1);
        Mockito.verify(recipeService).removeIngredientFromRecipe(recipe,ingredient);
    }

    @Test
    void redactIngredientInRecipeTest_Success() {
        Recipe recipe = new Recipe();
        recipe.setName("rec");
        recipe.setDescription("desc");
        recipe.setId(1);
        Ingredient ingredient = new Ingredient();
        ingredient.setName("name");
        ingredient.setQuantity("500g");
        ingredient.setId(1);
        ingredient.setRecipe(recipe);
        recipe.setIngredients(Set.of(ingredient));

        Ingredient redactedIngredient = new Ingredient();
        redactedIngredient.setName("redacted");
        redactedIngredient.setQuantity("500g");
        redactedIngredient.setId(1);

        Mockito.when(recipeService.getRecipeById(1)).thenReturn(Optional.of(recipe));
        Mockito.when(ingredientService.getIngredientById(1))
                .thenReturn(Optional.of(ingredient));
        RINGController.redactIngredientInRecipe(1,1,redactedIngredient);
        Mockito.verify(ingredientService).redactIngredient(redactedIngredient);
    }
}
