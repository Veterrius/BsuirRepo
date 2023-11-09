package by.dlstudio.cookbook.service;

import by.dlstudio.cookbook.entity.Cookbook;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.entity.User;

import java.util.List;
import java.util.Optional;

public interface CookbookService {

    Cookbook addRecipeToCookbook(Recipe recipe, Cookbook cookbook);

    Cookbook removeRecipeFromCookbook(Recipe recipe, Cookbook cookbook);

    List<Recipe> getCookbookRecipesWithName(String name, Cookbook cookbook);

    List<Recipe> getRecipesOfCookbook(Cookbook cookbook);

    Optional<Cookbook> getCookbookByUser(User user);
}
