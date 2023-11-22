package by.dlstudio.hospital.service;

import by.dlstudio.hospital.domain.entity.Disease;
import by.dlstudio.hospital.domain.entity.Doctor;
import by.dlstudio.hospital.domain.entity.HospitalRoom;
import by.dlstudio.hospital.domain.entity.Patient;
import by.dlstudio.hospital.domain.enums.DiseaseType;
import by.dlstudio.hospital.domain.enums.Qualification;
import by.dlstudio.hospital.domain.repository.DiseaseRepository;
import by.dlstudio.hospital.domain.repository.HospitalRoomRepository;
import by.dlstudio.hospital.domain.repository.PatientRepository;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;
import by.dlstudio.hospital.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PatientServiceImplTest {

    @Autowired
    private PatientServiceImpl patientService;
    @Autowired
    private PatientRepository patientRepository;

    @MockBean
    private HospitalRoomRepository hospitalRoomRepository;
    @MockBean
    private DiseaseRepository diseaseRepository;
    @MockBean
    private DoctorService doctorService;

    @Test
    void findPatientByIdTest_Success() {
        Patient patient = new Patient("test","test","test");
        patient = patientRepository.save(patient);
        assertEquals(patient,patientService.findPatientById(patient.getId()).orElseThrow());
    }

    @Test
    void findPatientByPhoneNumberTest_Success() {
        Patient patient = new Patient("test1", "test1", "test1");
        patient = patientRepository.save(patient);
        assertEquals(patient, patientService.findPatientByPhoneNumber(patient.getPhoneNumber()).orElseThrow());
    }

    @Test
    void registerOrUpdatePatientTest_Success() {
        Patient patient = new Patient("test2","test2","test2");
        patient = patientService.registerOrUpdatePatient(patient);

        assertNotNull(patient.getId());

        patient.setName("changed");
        patient = patientService.registerOrUpdatePatient(patient);

        assertEquals("changed",patientRepository.findPatientById(patient.getId()).orElseThrow().getName());
    }

    @Test
    void deletePatientTest_Success() {
        Patient patient = new Patient("test3","test3","test3");
        patient = patientRepository.save(patient);

        patientService.deletePatient(patient);

        assertTrue(patientRepository.findPatientByPhoneNumber("test3").isEmpty());
    }

    @Test
    void deletePatientByIdTest_Success() {
        Patient patient = new Patient("test4","test4","test4");
        patient = patientRepository.save(patient);

        try {
            patientService.deletePatientById(patient.getId());
        } catch (HospitalDatabaseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(patientRepository.findPatientByPhoneNumber("test4").isEmpty());
    }

    @Test
    void deletePatientByIdTest_HospitalDatabaseException_PatientNotFound() {
        assertThrows(HospitalDatabaseException.class, ()->patientService.deletePatientById(99L));
    }

    @Test
    void findAllPatientsTest_Success() {
        Patient patient = new Patient("test5","test5","test5");
        patientRepository.save(patient);

        assertNotEquals(0,Set.of(patientService.findAllPatients()).size());
    }

    @Test
    void dischargePatientTest_Success() {
        Patient patient = new Patient("test6","test6","test6");
        patient = patientRepository.save(patient);

        Doctor doctor = new Doctor("t","t","t");
        doctor.setQualification(Qualification.TEST);
        doctor.setPatient(patient);
        doctor.setId(1L);
        Doctor freedDoctor = new Doctor("t","t","t");
        freedDoctor.setQualification(Qualification.TEST);
        freedDoctor.setId(1L);

        Disease disease = new Disease("t", DiseaseType.TEST_ONE);
        disease.setId(1L);
        disease.getPatients().add(patient);
        Disease freedDisease = new Disease("t",DiseaseType.TEST_ONE);
        freedDisease.setId(1L);

        HospitalRoom hospitalRoom = new HospitalRoom();
        hospitalRoom.setId(1L);
        hospitalRoom.setSlots(1);
        hospitalRoom.getPatients().add(patient);
        HospitalRoom freedRoom = new HospitalRoom();
        freedRoom.setId(1L);
        freedRoom.setSlots(1);

        patient.setHospitalRoom(hospitalRoom);
        patient.setDoctor(doctor);
        patient.getDiseases().add(disease);

        Mockito.when(hospitalRoomRepository.save(freedRoom)).thenReturn(freedRoom);
        Mockito.when(doctorService.createOrUpdateDoctor(freedDoctor)).thenReturn(freedDoctor);
        Mockito.when(diseaseRepository.save(freedDisease)).thenReturn(freedDisease);

        patient = patientService.dischargePatient(patient);
        Mockito.verify(hospitalRoomRepository).save(freedRoom);
        Mockito.verify(doctorService).createOrUpdateDoctor(freedDoctor);
        Mockito.verify(diseaseRepository).save(freedDisease);

        assertNull(patient.getDoctor());
        assertNull(patient.getHospitalRoom());
        assertTrue(patient.getDiseases().isEmpty());
    }
}
