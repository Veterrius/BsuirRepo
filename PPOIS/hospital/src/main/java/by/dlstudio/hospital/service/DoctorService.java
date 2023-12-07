package by.dlstudio.hospital.service;

import by.dlstudio.hospital.domain.entity.Doctor;
import by.dlstudio.hospital.domain.entity.Patient;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;
import by.dlstudio.hospital.service.exception.VerificationException;

import java.util.Optional;

public interface DoctorService {

    Optional<Doctor> findDoctorById(Long id);

    Optional<Doctor> findDoctorByPhoneNumber(String phoneNumber);

    Doctor createOrUpdateDoctor(Doctor doctor);

    Doctor createOrUpdateValidDoctor(Doctor doctor) throws VerificationException;

    boolean doctorIsValid(Doctor doctor);

    boolean checkDoctorAvailability(Doctor doctorFromDb, Long patientId) throws HospitalDatabaseException;

    void deleteDoctor(Doctor doctorFromDb);

    void deleteDoctorById(Long id) throws HospitalDatabaseException;
}
