package by.dlstudio.hospital.service.impl;

import by.dlstudio.hospital.domain.entity.Doctor;
import by.dlstudio.hospital.domain.entity.Patient;
import by.dlstudio.hospital.domain.repository.DoctorRepository;
import by.dlstudio.hospital.domain.repository.PatientRepository;
import by.dlstudio.hospital.service.DoctorService;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;
import by.dlstudio.hospital.service.exception.VerificationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public Optional<Doctor> findDoctorById(Long id) {
        return doctorRepository.findDoctorById(id);
    }

    @Override
    public Optional<Doctor> findDoctorByPhoneNumber(String phoneNumber) {
        return doctorRepository.findDoctorByContactInfo(phoneNumber);
    }

    @Override
    public Doctor createOrUpdateDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor createOrUpdateValidDoctor(Doctor doctor) throws VerificationException {
        if (doctorIsValid(doctor)) {
            return createOrUpdateDoctor(doctor);
        } else throw new VerificationException("Doctor's contact info should be phone number" +
                " with format: \"+[country_code] (city_code) ###-####\"");
    }

    @Override
    public boolean doctorIsValid(Doctor doctor) {
        return doctor.verifyContactInfo(doctor.getContactInfo());
    }

    /**
     * This method checks if doctor is available for curing a patient.
     * It checks if patient already has a doctor and also the
     * {@link by.dlstudio.hospital.domain.enums.Qualification} of a doctor. This Qualification's
     * {@link by.dlstudio.hospital.domain.enums.DiseaseType} disease types should match the type of at least
     * one of the patient's diseases.
     * @param doctorFromDb
     * @param patientId
     * @return true if doctor is available
     * @throws HospitalDatabaseException if patient wasn't found
     */
    @Override
    public boolean checkDoctorAvailability(Doctor doctorFromDb, Long patientId) throws HospitalDatabaseException {
        Patient patientFromDb = patientRepository.findPatientById(patientId)
                .orElseThrow(()->new HospitalDatabaseException("Patient not found"));
        return doctorFromDb.getPatient() == null
                && patientFromDb.getDiseases().stream().anyMatch(d ->
                doctorFromDb.getQualification().getCurableDiseases().contains(d.getType()));
    }

    /**
     * This method deletes a doctor using a method from {@link DoctorRepository}
     * Before that, it clears doctor patient.
     * @param doctorFromDb is a doctor we want to delete.
     */
    @Override
    public void deleteDoctor(Doctor doctorFromDb) {
        Patient doctorPatient = doctorFromDb.getPatient();
        if(doctorPatient != null) {
            doctorFromDb.setPatient(null);
            doctorPatient.setDoctor(null);
            patientRepository.save(doctorPatient);
        }
        doctorRepository.delete(doctorFromDb);
    }

    /**
     * This method deletes a doctor by firstly finding it by id
     * and then invoking the deleteDoctor() method.
     * @param id is an id of a doctor we want to delete
     * @throws HospitalDatabaseException if doctor wasn't found
     */
    @Override
    public void deleteDoctorById(Long id) throws HospitalDatabaseException {
        Doctor doctorFromDb = findDoctorById(id)
                .orElseThrow(()->new HospitalDatabaseException(("Doctor not found")));
        deleteDoctor(doctorFromDb);
    }
}
