package by.dlstudio.cookbook.service.impl;

import by.dlstudio.cookbook.dao.IngredientDAO;
import by.dlstudio.cookbook.dao.RecipeDAO;
import by.dlstudio.cookbook.entity.Cookbook;
import by.dlstudio.cookbook.entity.Ingredient;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.service.RecipeService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class RecipeServiceImpl implements RecipeService {

    private final RecipeDAO recipeDAO;
    private final IngredientDAO ingredientDAO;

    /**
     * This constructor has a basic initialization for {@link IngredientDAO}
     * and {@link RecipeDAO}
     */
    public RecipeServiceImpl() {
        recipeDAO = new RecipeDAO();
        ingredientDAO = new IngredientDAO();
    }

    /**
     * This constructor may be used instead to replace the basic initialization
     * Currently used in testing
     * @param ingredientDAO is an IngredientDAO we want to use instead of a basic one
     * @param recipeDAO is a RecipeDAO we want to use instead of a basic one
     */
    public RecipeServiceImpl(RecipeDAO recipeDAO, IngredientDAO ingredientDAO) {
        this.recipeDAO = recipeDAO;
        this.ingredientDAO = ingredientDAO;
    }

    /**
     * This method creates a specified recipe by using method in a {@link RecipeDAO} class
     * @param recipe is a recipe we want to create
     * @return created recipe
     */
    @Override
    public Recipe createRecipe(Recipe recipe) {
        return recipeDAO.create(recipe);
    }

    /**
     * This method finds a recipe by its id using method from {@link RecipeDAO}
     * @param recipeId is an id by which we find the recipe
     * @return Optional of found recipe
     */
    @Override
    public Optional<Recipe> getRecipeById(long recipeId) {
        return recipeDAO.findOne(recipeId);
    }

    /**
     * This method find all recipes using method from {@link RecipeDAO} class
     * @return list of found recipes
     */
    @Override
    public List<Recipe> getAllRecipes() {
        return recipeDAO.findAll();
    }

    /**
     * This method deletes specified recipe using method from {@link RecipeDAO} class.
     * Before that, it ensures that this recipe contains no ingredients,
     * and no cookbook contains this recipe
     * @param recipe we want to delete
     */
    @Override
    public void deleteRecipe(@NotNull Recipe recipe) {
        for (Ingredient ingredient : recipe.getIngredients()) {
            removeIngredientFromRecipe(recipe, ingredient);
        }
        for (Cookbook cb : recipe.getCookbooks()) {
            cb.getRecipes().remove(recipe);
        }
        recipeDAO.delete(recipe);
    }

    /**
     * This method updates a specified recipe using method in {@link RecipeDAO} class
     * @param updatedRecipe is a recipe we want to update
     * @return updated recipe
     */
    @Override
    public Recipe updateRecipe(Recipe updatedRecipe) {
        return recipeDAO.update(updatedRecipe);
    }

    /**
     * This method adds the specified ingredient to a specified recipe, creating
     * this ingredient in a database before that. It uses methods from {@link RecipeDAO}
     * and {@link IngredientDAO} classes
     * @param recipe to which we want to add the ingredient to
     * @param ingredient we want to add
     * @return recipe with added ingredient
     */
    @Override
    public Recipe addIngredientToRecipe(Recipe recipe, Ingredient ingredient) {
        ingredient.setRecipe(recipe);
        ingredientDAO.create(ingredient);
        recipe.getIngredients().add(ingredient);
        return recipeDAO.update(recipe);
    }

    /**
     * This method removes a specified ingredient from recipe, also deleting it from the
     * database. It uses methods from {@link RecipeDAO} and {@link IngredientDAO} classes
     * @param recipe from which we want to remove ingredient
     * @param ingredient we want to remove
     * @return recipe with removed ingredient
     */
    @Override
    public Recipe removeIngredientFromRecipe(@NotNull Recipe recipe, Ingredient ingredient) {
        recipe.getIngredients().remove(ingredient);
        ingredientDAO.delete(ingredient);
        return recipe;
    }

    /**
     * This method gets all specified recipe's ingredients using method
     * in {@link IngredientDAO} class.
     * @param recipe which ingredients we want to find
     * @return list of found ingredients
     */
    @Override
    public List<Ingredient> getAllRecipeIngredients(Recipe recipe) {
        return ingredientDAO.findAllRecipeIngredients(recipe);
    }
}
