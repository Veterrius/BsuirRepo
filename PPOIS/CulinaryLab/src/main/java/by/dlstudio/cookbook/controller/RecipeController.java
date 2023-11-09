package by.dlstudio.cookbook.controller;

import by.dlstudio.cookbook.entity.Ingredient;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.service.RecipeService;
import by.dlstudio.cookbook.service.impl.RecipeServiceImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController() {
        recipeService = new RecipeServiceImpl();
    }

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public Recipe getRecipeById(long recipeId) {
        return recipeService.getRecipeById(recipeId).orElseThrow();
    }

    public List<Recipe> showAllRecipes() {
        return recipeService.getAllRecipes();
    }

    public Recipe createRecipe(Recipe recipe) {
        return recipeService.createRecipe(recipe);
    }

    public void deleteRecipe(long recipeId) {
        recipeService.deleteRecipe(getRecipeById(recipeId));
    }

    public void redactRecipe(long recipeId, Recipe redactedRecipe) {
        Recipe recipeFromDb = getRecipeById(recipeId);
        recipeCopy(redactedRecipe,recipeFromDb);
        recipeService.updateRecipe(recipeFromDb);
    }

    private void recipeCopy(@NotNull Recipe sourceRecipe, @NotNull Recipe destRecipe) {
        destRecipe.setIngredients(sourceRecipe.getIngredients());
        destRecipe.setCookbooks(sourceRecipe.getCookbooks());
        destRecipe.setDescription(sourceRecipe.getDescription());
        destRecipe.setName(sourceRecipe.getName());
    }
}
