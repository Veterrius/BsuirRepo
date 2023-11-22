package by.dlstudio.hospital.domain.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "department",fetch = FetchType.EAGER)
    private Set<HospitalRoom> hospitalRooms = new HashSet<>();

    @OneToMany(mappedBy = "department",fetch = FetchType.EAGER)
    private Set<Doctor> doctors = new HashSet<>();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Set<HospitalRoom> getHospitalRooms() {
        return hospitalRooms;
    }

    public void setHospitalRooms(Set<HospitalRoom> hospitalRooms) {
        this.hospitalRooms = hospitalRooms;
    }

    public Set<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Department.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name=" + name)
                .add("hospitalRooms=" + hospitalRooms)
                .add("doctors=" + doctors)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
