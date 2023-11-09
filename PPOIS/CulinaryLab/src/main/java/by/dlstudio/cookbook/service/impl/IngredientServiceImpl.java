package by.dlstudio.cookbook.service.impl;

import by.dlstudio.cookbook.dao.IngredientDAO;
import by.dlstudio.cookbook.entity.Ingredient;
import by.dlstudio.cookbook.service.IngredientService;

import java.util.Optional;

public class IngredientServiceImpl implements IngredientService {

    private final IngredientDAO ingredientDAO;

    /**
     * This constructor has a basic initialization for {@link IngredientDAO}
     */
    public IngredientServiceImpl() {
        ingredientDAO = new IngredientDAO();
    }

    /**
     * This constructor may be used instead to replace the basic initialization
     * Currently used in testing
     * @param ingredientDAO is an IngredientDAO we want to use instead of a basic one
     */
    public IngredientServiceImpl(IngredientDAO ingredientDAO) {
        this.ingredientDAO = ingredientDAO;
    }

    /**
     * This method redacts an ingredient by using method from {@link IngredientDAO}
     * class
     * @param ingredient we want to redact
     * @return redacted Ingredient
     */
    @Override
    public Ingredient redactIngredient(Ingredient ingredient) {
        return ingredientDAO.update(ingredient);
    }

    /**
     * This method finds an ingredient by its id using method from {@link IngredientDAO}
     * @param ingredientId is an id by which we find the ingredient
     * @return Optional of found ingredient
     */
    @Override
    public Optional<Ingredient> getIngredientById(long ingredientId) {
        return ingredientDAO.findOne(ingredientId);
    }
}
