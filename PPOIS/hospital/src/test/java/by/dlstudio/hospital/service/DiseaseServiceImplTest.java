package by.dlstudio.hospital.service;

import by.dlstudio.hospital.domain.entity.Disease;
import by.dlstudio.hospital.domain.entity.Patient;
import by.dlstudio.hospital.domain.enums.DiseaseType;
import by.dlstudio.hospital.domain.repository.DiseaseRepository;
import by.dlstudio.hospital.domain.repository.PatientRepository;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;
import by.dlstudio.hospital.service.impl.DiseaseServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DiseaseServiceImplTest {

    @Autowired
    private DiseaseServiceImpl diseaseService;
    @Autowired
    private DiseaseRepository diseaseRepository;
    @Autowired
    private PatientRepository patientRepository;
    @MockBean
    private PatientService patientService;

    @Test
    void findDiseaseByIdTest_Success() {
        Disease disease = new Disease("test", DiseaseType.TEST_ONE);
        disease = diseaseRepository.save(disease);

        assertEquals(disease, diseaseService.findDiseaseById(disease.getId()).orElseThrow());
    }

    @Test
    void findDiseaseByIdTest_EmptyOptional() {
        assertTrue(diseaseService.findDiseaseById(99L).isEmpty());
    }

    @Test
    void findDiseaseByNameTest_Success() {
        Disease disease = new Disease("test0", DiseaseType.TEST_TWO);
        disease = diseaseRepository.save(disease);

        assertEquals(disease, diseaseService.findDiseaseByName(disease.getName()).orElseThrow());
    }

    void findDiseaseByNameTest_EmptyOptional() {
        assertTrue(diseaseService.findDiseaseByName("unasdasd").isEmpty());
    }

    @Test
    void createOrUpdateDiseaseTest_Success() {
        Disease disease = new Disease("test1",DiseaseType.TEST_THREE);
        disease = diseaseService.createOrUpdateDisease(disease);

        assertNotNull(disease.getId());

        disease.setType(DiseaseType.TEST_ONE);
        disease = diseaseService.createOrUpdateDisease(disease);
        assertEquals(DiseaseType.TEST_ONE, disease.getType());
    }

    @Test
    void deleteDiseaseTest_Success() {
        Disease disease = new Disease("test2",DiseaseType.TEST_TWO);
        disease = diseaseRepository.save(disease);

        diseaseService.deleteDisease(disease);

        assertTrue(diseaseRepository.findDiseaseByName("test2").isEmpty());
    }

    @Test
    void deleteDiseaseByIdTest_Success() {
        Disease disease = new Disease("test3",DiseaseType.TEST_ONE);
        disease = diseaseRepository.save(disease);
        try {
            diseaseService.deleteDiseaseById(disease.getId());
        } catch (HospitalDatabaseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(diseaseRepository.findDiseaseByName("test3").isEmpty());
    }

    @Test
    void deleteDiseaseByIdTest_HospitalDatabaseException_DiseaseNotFound() {
        assertThrows(HospitalDatabaseException.class, ()->diseaseService.deleteDiseaseById(99L));
    }

    @Test
    void diagnoseTest_Success() {
        Disease disease = new Disease("test4",DiseaseType.TEST_ONE);
        disease = diseaseRepository.save(disease);

        Patient patient = new Patient("t","t","t");
        patient = patientRepository.save(patient);
        Patient expectedPatient = new Patient("t","t","t");
        expectedPatient.setId(patient.getId());
        expectedPatient.getDiseases().add(disease);

        Mockito.when(patientService.findPatientById(patient.getId())).thenReturn(Optional.of(patient));
        try {
            diseaseService.diagnose(disease, patient.getId());
            Mockito.verify(patientService).registerOrUpdatePatient(expectedPatient);
        } catch (HospitalDatabaseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(diseaseRepository.findDiseaseByName("test4")
                .orElseThrow().getPatients().contains(expectedPatient));
    }

    @Test
    void diagnoseTest_HospitalDatabaseException_PatientNotFound() {
        Disease disease = new Disease("test5",DiseaseType.TEST_ONE);
        diseaseRepository.save(disease);
        assertThrows(HospitalDatabaseException.class, ()->diseaseService.diagnose(disease,99L));
    }
}
