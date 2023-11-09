package by.dlstudio.cookbook.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "cb_user")
@NamedQuery(name = "getUserByUsername",
        query = "select u from User u where u.username = :username")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cookbook_id", referencedColumnName = "id")
    private Cookbook cookbook;

    public User(String username, Cookbook cookbook) {
        this.username = username;
        this.cookbook = cookbook;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Cookbook getCookbook() {
        return cookbook;
    }

    public void setCookbook(Cookbook cookbook) {
        this.cookbook = cookbook;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
