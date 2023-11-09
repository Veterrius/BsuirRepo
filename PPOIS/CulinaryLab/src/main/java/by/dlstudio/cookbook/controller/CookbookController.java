package by.dlstudio.cookbook.controller;


import by.dlstudio.cookbook.entity.Cookbook;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.entity.User;
import by.dlstudio.cookbook.service.CookbookService;
import by.dlstudio.cookbook.service.RecipeService;
import by.dlstudio.cookbook.service.impl.CookbookServiceImpl;
import by.dlstudio.cookbook.service.impl.RecipeServiceImpl;

import java.util.List;

public class CookbookController {

    private final CookbookService cookbookService;
    private final RecipeService recipeService;

    public CookbookController() {
        cookbookService = new CookbookServiceImpl();
        recipeService = new RecipeServiceImpl();
    }

    public CookbookController(CookbookService cookbookService, RecipeService recipeService) {
        this.cookbookService = cookbookService;
        this.recipeService = recipeService;
    }

    public Cookbook addRecipeToCookbook(long recipeId, User principal) {
        Cookbook cookbook = cookbookService.getCookbookByUser(principal).orElseThrow();
        Recipe recipe = recipeService.getRecipeById(recipeId).orElseThrow();
        return cookbookService.addRecipeToCookbook(recipe, cookbook);
    }

    public Cookbook removeRecipeFromCookbook(long recipeId, User principal) {
        Cookbook cookbook = cookbookService.getCookbookByUser(principal).orElseThrow();
        Recipe recipe = recipeService.getRecipeById(recipeId).orElseThrow();
        return cookbookService.removeRecipeFromCookbook(recipe, cookbook);
    }

    public List<Recipe> showAllCookbookRecipesWithName(User principal, String name) {
        Cookbook cookbook = cookbookService.getCookbookByUser(principal).orElseThrow();
        return cookbookService.getCookbookRecipesWithName(name, cookbook);
    }

    public List<Recipe> showAllCookbookRecipes(User principal) {
        Cookbook cookbook = cookbookService.getCookbookByUser(principal).orElseThrow();
        return cookbookService.getRecipesOfCookbook(cookbook);
    }
}
