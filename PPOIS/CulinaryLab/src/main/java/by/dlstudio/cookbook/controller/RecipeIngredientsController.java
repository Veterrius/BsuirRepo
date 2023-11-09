package by.dlstudio.cookbook.controller;

import by.dlstudio.cookbook.entity.Ingredient;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.service.IngredientService;
import by.dlstudio.cookbook.service.RecipeService;
import by.dlstudio.cookbook.service.impl.IngredientServiceImpl;
import by.dlstudio.cookbook.service.impl.RecipeServiceImpl;

import java.util.List;

public class RecipeIngredientsController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    public RecipeIngredientsController() {
        recipeService = new RecipeServiceImpl();
        ingredientService = new IngredientServiceImpl();
    }

    public RecipeIngredientsController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    public List<Ingredient> showAllIngredientsOfRecipe(long recipeId) {
        return recipeService.getAllRecipeIngredients(recipeService.getRecipeById(recipeId).orElseThrow());
    }

    public Recipe addIngredientToRecipe(long recipeId, Ingredient ingredient) {
        return recipeService.addIngredientToRecipe(recipeService.getRecipeById(recipeId).orElseThrow(), ingredient);
    }

    public Recipe removeIngredientFromRecipe(long recipeId, long ingredientId) {
        return recipeService.removeIngredientFromRecipe(recipeService.getRecipeById(recipeId).orElseThrow(),
                ingredientService.getIngredientById(ingredientId).orElseThrow());
    }

    public Recipe redactIngredientInRecipe(long recipeId, long ingredientId, Ingredient redactedIngredient) {
        Ingredient ingFromDb = ingredientService.getIngredientById(ingredientId).orElseThrow();
        ingredientCopy(redactedIngredient, ingFromDb);
        ingredientService.redactIngredient(ingFromDb);
        return recipeService.getRecipeById(recipeId).orElseThrow();
    }

    private void ingredientCopy(Ingredient source, Ingredient dest) {
        dest.setQuantity(source.getQuantity());
        dest.setName(source.getName());
        dest.setRecipe(source.getRecipe());
    }
}
