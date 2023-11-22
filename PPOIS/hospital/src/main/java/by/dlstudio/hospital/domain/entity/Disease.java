package by.dlstudio.hospital.domain.entity;

import by.dlstudio.hospital.domain.enums.DiseaseType;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name = "disease")
public class Disease {

    public Disease() {
    }

    public Disease(String name, DiseaseType type) {
        this.name = name;
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "type", nullable = false,length = 100)
    @Enumerated(value = EnumType.STRING)
    private DiseaseType type;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "patient_disease",
            joinColumns = {@JoinColumn(name = "disease_id")},
            inverseJoinColumns = { @JoinColumn(name = "patient_id")}
    )
    private Set<Patient> patients = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DiseaseType getType() {
        return type;
    }

    public void setType(DiseaseType type) {
        this.type = type;
    }

    public Set<Patient> getPatients() {
        return patients;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Disease.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("type=" + type)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disease disease = (Disease) o;
        return Objects.equals(id, disease.id) && Objects.equals(name, disease.name) && type == disease.type && Objects.equals(patients, disease.patients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }
}
