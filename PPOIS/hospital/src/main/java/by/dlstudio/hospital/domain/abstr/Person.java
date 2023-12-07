package by.dlstudio.hospital.domain.abstr;

import jakarta.persistence.*;

import java.util.StringJoiner;

@MappedSuperclass
public abstract class Person {

    public Person() {
    }

    public Person(String name, String surname, String contactInfo) {
        this.name = name;
        this.surname = surname;
        this.contactInfo = contactInfo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "contact_info", unique = true)
    private String contactInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    protected abstract boolean verifyContactInfo(String contactInfo);

    @Override
    public String toString() {
        return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("surname='" + surname + "'")
                .add("contactInfo='" + contactInfo + "'")
                .toString();
    }
}
