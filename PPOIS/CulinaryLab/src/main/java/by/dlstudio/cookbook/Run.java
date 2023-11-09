package by.dlstudio.cookbook;

import by.dlstudio.cookbook.controller.CookbookController;
import by.dlstudio.cookbook.controller.RecipeController;
import by.dlstudio.cookbook.controller.RecipeIngredientsController;
import by.dlstudio.cookbook.controller.UserController;
import by.dlstudio.cookbook.entity.Ingredient;
import by.dlstudio.cookbook.entity.Recipe;
import by.dlstudio.cookbook.entity.User;

public class Run {
    public static void main(String[] args) {

        UserController userController = new UserController();
        RecipeController recipeController = new RecipeController();
        RecipeIngredientsController RINGController = new RecipeIngredientsController();
        CookbookController cookbookController = new CookbookController();

        User principal = new User();
        principal.setUsername("admin");
        userController.createUser(principal);
        userController.changeUsername(principal, "princ");

        Recipe carrotPie = new Recipe();
        carrotPie.setName("carrot_pie");
        carrotPie.setDescription("desc");
        carrotPie = recipeController.createRecipe(carrotPie);

        cookbookController.addRecipeToCookbook(1, principal);

        Ingredient ingredient = new Ingredient();
        ingredient.setName("carrot");
        ingredient.setQuantity("500g");
        carrotPie = RINGController.addIngredientToRecipe(1, ingredient);
        ingredient.setQuantity("350g");
        carrotPie = RINGController.redactIngredientInRecipe(1, 1, ingredient);
        System.out.println(RINGController.showAllIngredientsOfRecipe(1));
        carrotPie = RINGController.removeIngredientFromRecipe(1, 1);

        Recipe carrotPie2 = new Recipe();
        carrotPie2.setName("carrot_pie");
        carrotPie2.setDescription("desc");
        carrotPie2 = recipeController.createRecipe(carrotPie2);
        recipeController.redactRecipe(1, carrotPie2);
        System.out.println(recipeController.showAllRecipes());

        cookbookController.addRecipeToCookbook(2, principal);
        System.out.println(cookbookController.showAllCookbookRecipes(principal));
        System.out.println(cookbookController.showAllCookbookRecipesWithName(principal,
                "carrot_pie"));

        recipeController.deleteRecipe(1);
        cookbookController.removeRecipeFromCookbook(2, principal);
    }

}
