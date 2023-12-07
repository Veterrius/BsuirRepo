package by.dlstudio.hospital.domain.entity;

import by.dlstudio.hospital.domain.abstr.Person;
import by.dlstudio.hospital.domain.enums.Qualification;
import by.dlstudio.hospital.domain.util.ContactInfoVerifier;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "doctor")
public class Doctor extends Person {

    public Doctor() {
        super();
    }

    public Doctor(String name, String surname, String phoneNumber) {
        super(name, surname, phoneNumber);
    }

    @Column(name = "qualification",nullable = false)
    @Enumerated(EnumType.STRING)
    private Qualification qualification;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public boolean verifyContactInfo(String phoneNumber) {
        return ContactInfoVerifier.verifyPhoneNumber(phoneNumber);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Doctor.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("name='" + getName() + "'")
                .add("surname='" + getSurname() + "'")
                .add("phoneNumber='" + getContactInfo() + "'")
                .add("qualification=" + qualification)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return getId().equals(doctor.getId()) && getContactInfo().equals(doctor.getContactInfo())
                && qualification.equals(doctor.getQualification());
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualification, department, patient);
    }
}
