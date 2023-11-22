package by.dlstudio.hospital.service;

import by.dlstudio.hospital.domain.entity.Patient;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;
import by.dlstudio.hospital.service.exception.HospitalRoomException;

import java.util.Optional;

public interface PatientService {

    Optional<Patient> findPatientById(Long id);

    Optional<Patient> findPatientByPhoneNumber(String phoneNumber);

    Patient registerOrUpdatePatient(Patient patient);

    Patient dischargePatient(Patient patientFromDb);

    void deletePatient(Patient patientFromDb);

    void deletePatientById(Long id) throws HospitalDatabaseException;

    Iterable<Patient> findAllPatients();

}
