package by.dlstudio.hospital.service.impl;

import by.dlstudio.hospital.domain.entity.Patient;
import by.dlstudio.hospital.domain.repository.DiseaseRepository;
import by.dlstudio.hospital.domain.repository.HospitalRoomRepository;
import by.dlstudio.hospital.domain.repository.PatientRepository;
import by.dlstudio.hospital.service.DoctorService;
import by.dlstudio.hospital.service.PatientService;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final HospitalRoomRepository hospitalRoomRepository;
    private final DiseaseRepository diseaseRepository;
    private final DoctorService doctorService;

    public PatientServiceImpl(PatientRepository patientRepository, HospitalRoomRepository hospitalRoomRepository,
                              DiseaseRepository diseaseRepository, DoctorService doctorService) {
        this.patientRepository = patientRepository;
        this.hospitalRoomRepository = hospitalRoomRepository;
        this.diseaseRepository = diseaseRepository;
        this.doctorService = doctorService;
    }

    @Override
    public Optional<Patient> findPatientById(Long id) {
        return patientRepository.findPatientById(id);
    }

    @Override
    public Optional<Patient> findPatientByPhoneNumber(String phoneNumber) {
        return patientRepository.findPatientByPhoneNumber(phoneNumber);
    }

    @Override
    public Iterable<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient registerOrUpdatePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public void deletePatient(Patient patientFromDb) {
        patientRepository.delete(patientFromDb);
    }

    /**
     * This method deletes a patient firstly getting it by id
     * and then calling the deletePatient() method
     * @param id is an id of a patient we want to delete
     * @throws HospitalDatabaseException if patient wasn't found
     */
    @Override
    public void deletePatientById(Long id) throws HospitalDatabaseException {
        Patient patientFromDb = findPatientById(id)
                .orElseThrow(()->new HospitalDatabaseException(("Patient not found")));
        deletePatient(patientFromDb);
    }

    /**
     * This method discharges a patient from the hospital while
     * keeping it in a database. It clears patient's diseases,
     * removes it from a room and also remove its doctors.
     * @param patientFromDb is a patient we want to discharge
     * @return discharged patient
     */
    @Override
    public Patient dischargePatient(Patient patientFromDb) {
        if (!patientFromDb.getDiseases().isEmpty()) {
            patientFromDb.getDiseases().forEach(d -> {
                d.getPatients().remove(patientFromDb);
                diseaseRepository.save(d);
                patientFromDb.getDiseases().remove(d);
            });
        }
        if (patientFromDb.getDoctor() != null) {
            patientFromDb.getDoctor().setPatient(null);
            doctorService.createOrUpdateDoctor(patientFromDb.getDoctor());
            patientFromDb.setDoctor(null);
        }
        if (patientFromDb.getHospitalRoom() != null) {
            patientFromDb.getHospitalRoom().getPatients().remove(patientFromDb);
            hospitalRoomRepository.save(patientFromDb.getHospitalRoom());
            patientFromDb.setHospitalRoom(null);
        }
        return patientRepository.save(patientFromDb);
    }
}
