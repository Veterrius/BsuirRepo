package by.dlstudio.cookbook.service;

import by.dlstudio.cookbook.entity.Ingredient;

import java.util.Optional;

public interface IngredientService {


    Ingredient redactIngredient(Ingredient ingredient);

    Optional<Ingredient> getIngredientById(long ingredientId);
}
