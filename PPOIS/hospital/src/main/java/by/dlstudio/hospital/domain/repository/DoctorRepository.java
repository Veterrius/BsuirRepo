package by.dlstudio.hospital.domain.repository;

import by.dlstudio.hospital.domain.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findDoctorById(Long id);

    Optional<Doctor> findDoctorByContactInfo(String phoneNumber);
}
