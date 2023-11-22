package by.dlstudio.hospital.service;

import by.dlstudio.hospital.domain.entity.Disease;
import by.dlstudio.hospital.domain.entity.Patient;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;

import java.util.Optional;

public interface DiseaseService {

    Optional<Disease> findDiseaseById(Long id);

    Disease createOrUpdateDisease(Disease disease);

    void deleteDisease(Disease diseaseFromDb);

    void deleteDiseaseById(Long id) throws HospitalDatabaseException;

    Patient diagnose(Disease diseaseFromDb, Long patientId) throws HospitalDatabaseException;

    Optional<Disease> findDiseaseByName(String name);
}
