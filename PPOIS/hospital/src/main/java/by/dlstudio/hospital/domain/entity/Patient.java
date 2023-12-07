package by.dlstudio.hospital.domain.entity;

import by.dlstudio.hospital.domain.abstr.Person;
import by.dlstudio.hospital.domain.util.ContactInfoVerifier;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name = "patient")
public class Patient extends Person {

    public Patient() {
    }

    public Patient(String name, String surname, String address) {
        super(name, surname, address);
    }

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "patients")
    private Set<Disease> diseases = new HashSet<>();

    @OneToOne(mappedBy = "patient")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "hospital_room_id")
    private HospitalRoom hospitalRoom;

    public Set<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(Set<Disease> diseases) {
        this.diseases = diseases;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public HospitalRoom getHospitalRoom() {
        return hospitalRoom;
    }

    public void setHospitalRoom(HospitalRoom hospitalRoom) {
        this.hospitalRoom = hospitalRoom;
    }

    @Override
    public boolean verifyContactInfo(String address) {
        return ContactInfoVerifier.verifyAddress(address);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Patient.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("name='" + getName() + "'")
                .add("surname='" + getSurname() + "'")
                .add("address='" + getContactInfo() + "'")
                .add("diseases=" + diseases)
                .add("doctor=" + doctor)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return getId().equals(patient.getId()) && getContactInfo().equals(patient.getContactInfo())
                && diseases.stream().allMatch(d -> patient.getDiseases().stream()
                .anyMatch(d1 -> d1.getId().equals(d.getId())));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),getContactInfo(),getName());
    }
}
