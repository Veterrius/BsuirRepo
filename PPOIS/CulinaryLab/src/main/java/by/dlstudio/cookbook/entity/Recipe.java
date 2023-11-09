package by.dlstudio.cookbook.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "recipe")
@NamedQuery(name = "getRecipesByCookbookId",
        query = "select r from Recipe r join r.cookbooks c where c.id = :id")
public class Recipe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER)
    private Set<Ingredient> ingredients = new HashSet<>();

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cookbook_recipe",
            joinColumns = { @JoinColumn(name = "recipe_id")},
            inverseJoinColumns = { @JoinColumn(name = "cookbook_id") }
    )
    private Set<Cookbook> cookbooks = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Cookbook> getCookbooks() {
        return cookbooks;
    }

    public void setCookbooks(Set<Cookbook> cookbooks) {
        this.cookbooks = cookbooks;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return id.equals(recipe.id) && name.equals(recipe.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
