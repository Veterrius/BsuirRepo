package by.dlstudio.hospital.service.impl;

import by.dlstudio.hospital.domain.entity.Disease;
import by.dlstudio.hospital.domain.entity.Patient;
import by.dlstudio.hospital.domain.repository.DiseaseRepository;
import by.dlstudio.hospital.service.DiseaseService;
import by.dlstudio.hospital.service.PatientService;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;
    private final PatientService patientService;

    public DiseaseServiceImpl(DiseaseRepository diseaseRepository, PatientService patientService) {
        this.diseaseRepository = diseaseRepository;
        this.patientService = patientService;
    }

    @Override
    public Optional<Disease> findDiseaseById(Long id) {
        return diseaseRepository.findDiseaseById(id);
    }

    @Override
    public Optional<Disease> findDiseaseByName(String name) {
        return diseaseRepository.findDiseaseByName(name);
    }

    @Override
    public Disease createOrUpdateDisease(Disease disease) {
        return diseaseRepository.save(disease);
    }

    @Override
    public void deleteDisease(Disease diseaseFromDb) {
        diseaseRepository.delete(diseaseFromDb);
    }

    /**
     * This method deletes a disease by firstly finding it in a database
     * and then invoking the deleteDisease() method
     * @param id is an id of a disease we want to delete
     * @throws HospitalDatabaseException if disease wasn't found
     */
    @Override
    public void deleteDiseaseById(Long id) throws HospitalDatabaseException {
        Disease diseaseFromDb = findDiseaseById(id)
                .orElseThrow(()->new HospitalDatabaseException(("Disease not found")));
        deleteDisease(diseaseFromDb);
    }

    /**
     * This method assigns a disease to the specified patient.
     * @param diseaseFromDb is a disease we want to diagnose
     * @param patientId is a patient's id
     * @return patient with added disease
     * @throws HospitalDatabaseException if patient wasn't found
     */
    @Override
    public Patient diagnose(Disease diseaseFromDb, Long patientId) throws HospitalDatabaseException {
        Patient patientFromDb = patientService.findPatientById(patientId)
                .orElseThrow(()-> new HospitalDatabaseException("Patient not found"));
        diseaseFromDb.getPatients().add(patientFromDb);
        diseaseFromDb = diseaseRepository.save(diseaseFromDb);
        patientFromDb.getDiseases().add(diseaseFromDb);
        return patientService.registerOrUpdatePatient(patientFromDb);
    }
}
