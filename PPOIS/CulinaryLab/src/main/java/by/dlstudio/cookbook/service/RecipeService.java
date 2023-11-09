package by.dlstudio.cookbook.service;

import by.dlstudio.cookbook.entity.Ingredient;
import by.dlstudio.cookbook.entity.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    Recipe createRecipe(Recipe recipe);

    Optional<Recipe> getRecipeById(long recipeId);

    List<Recipe> getAllRecipes();

    void deleteRecipe(Recipe recipe);

    Recipe updateRecipe(Recipe updatedRecipe);

    Recipe addIngredientToRecipe(Recipe recipe, Ingredient ingredient);

    Recipe removeIngredientFromRecipe(Recipe recipe, Ingredient ingredient);

    List<Ingredient> getAllRecipeIngredients(Recipe recipe);
}
