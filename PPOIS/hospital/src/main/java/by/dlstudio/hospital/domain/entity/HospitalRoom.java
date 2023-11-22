package by.dlstudio.hospital.domain.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name = "hospital_room")
public class HospitalRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "department_id",nullable = false)
    private Department department;

    @OneToMany(mappedBy = "hospitalRoom",fetch = FetchType.EAGER)
    private Set<Patient> patients = new HashSet<>();

    @Column(name = "slots", nullable = false)
    private Integer slots;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Patient> getPatients() {
        return patients;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public Integer getSlots() {
        return slots;
    }

    public void setSlots(Integer slots) {
        this.slots = slots;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HospitalRoom.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("patients=" + patients)
                .add("slots=" + slots)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HospitalRoom that = (HospitalRoom) o;
        return Objects.equals(id, that.id) && Objects.equals(department, that.department)
                && Objects.equals(slots, that.slots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, slots, department);
    }
}
