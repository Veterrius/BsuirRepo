package by.dlstudio.cookbook.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cookbook")
@NamedQuery(name = "getCookbookByUser",
        query = "select c from Cookbook c where c.user = :user")
public class Cookbook implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @OneToOne(mappedBy = "cookbook")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "cookbooks")
    private Set<Recipe> recipes = new HashSet<>();

    public void removeRecipe(Recipe recipe) {
        this.recipes.remove(recipe);
        recipe.getCookbooks().remove(this);
    }

    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
        recipe.getCookbooks().add(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cookbook cookbook = (Cookbook) o;
        return id.equals(cookbook.id) && Objects.equals(user, cookbook.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }

    @Override
    public String toString() {
        return "Cookbook{" +
                "id=" + id +
                ", user=" + user +
                ", recipes=" + recipes +
                '}';
    }
}

