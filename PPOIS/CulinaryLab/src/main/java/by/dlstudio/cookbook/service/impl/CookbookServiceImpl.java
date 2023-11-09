package by.dlstudio.cookbook.service.impl;

import by.dlstudio.cookbook.dao.CookbookDAO;
import by.dlstudio.cookbook.dao.RecipeDAO;
import by.dlstudio.cookbook.entity.Cookbook;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.entity.User;
import by.dlstudio.cookbook.service.CookbookService;

import java.util.List;
import java.util.Optional;

public class CookbookServiceImpl implements CookbookService {

    private final RecipeDAO recipeDAO;
    private final CookbookDAO cookbookDAO;

    /**
     * This constructor has a basic initialization for {@link RecipeDAO}
     * and {@link CookbookDAO} class objects.
     */
    public CookbookServiceImpl() {
        recipeDAO = new RecipeDAO();
        cookbookDAO = new CookbookDAO();
    }

    /**
     * This constructor may be used instead to replace the basic initialization
     * Currently used in testing
     * @param recipeDAO is a RecipeDAO we want to use instead of a basic one
     * @param cookbookDAO is a CookbookDAO we want to use instead of a basic one
     */
    public CookbookServiceImpl(RecipeDAO recipeDAO, CookbookDAO cookbookDAO) {
        this.recipeDAO = recipeDAO;
        this.cookbookDAO = cookbookDAO;
    }

    /**
     * This method adds specified recipe to a specified cookbook using method
     * from the {@link RecipeDAO class}
     * @param recipe is a recipe we want to addc
     * @param cookbook is a cookbook in which we want to add a recipe
     * @return cookbook with added recipe
     */
    @Override
    public Cookbook addRecipeToCookbook(Recipe recipe, Cookbook cookbook) {
        cookbook.addRecipe(recipe);
        recipeDAO.update(recipe);
        return cookbook;
    }

    /**
     * This method removes a specified recipe from a specified cookbook
     * using methods from the {@link RecipeDAO} and {@link CookbookDAO} classes
     * @param recipe is a recipe we want to remove
     * @param cookbook is a cookbook in which we want to remove the recipe
     * @return cookbook with removed recipe
     */
    @Override
    public Cookbook removeRecipeFromCookbook(Recipe recipe, Cookbook cookbook) {
        cookbook.removeRecipe(recipe);
        recipeDAO.update(recipe);
        return cookbookDAO.update(cookbook);
    }

    /**
     * This method finds all recipes from a specified cookbook with a
     * specified name using getRecipesOfCookbook() method firstly
     * @param name is a name of a recipes we want to find
     * @param cookbook is a cookbook in which we find recipes
     * @return list of found recipes
     */
    @Override
    public List<Recipe> getCookbookRecipesWithName(String name, Cookbook cookbook) {
        List<Recipe> recipesOfCookbook = getRecipesOfCookbook(cookbook);
        return recipesOfCookbook.stream().filter(c -> c.getName().equals(name)).toList();
    }

    /**
     * This method find all recipes of the specified cookbook using method
     * in the {@link RecipeDAO} class
     * @param cookbook is a cookbook in which we find recipes
     * @return list of found recipes
     */
    @Override
    public List<Recipe> getRecipesOfCookbook(Cookbook cookbook) {
        return recipeDAO.findRecipesOfCookbook(cookbook.getId());
    }

    /**
     * This method finds cookbook by a specified user
     * @param user is a user whose cookbook we want to get
     * @return optional of found cookbook
     */
    @Override
    public Optional<Cookbook> getCookbookByUser(User user) {
        return cookbookDAO.findCookbookByUser(user);
    }
}
