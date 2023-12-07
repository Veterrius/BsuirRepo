package by.dlstudio.hospital.domain.repository;

import by.dlstudio.hospital.domain.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findPatientById(Long id);

    Optional<Patient> findPatientByContactInfo(String address);
}
