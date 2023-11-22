package by.dlstudio.hospital.service;


import by.dlstudio.hospital.domain.entity.Disease;
import by.dlstudio.hospital.domain.entity.Doctor;
import by.dlstudio.hospital.domain.entity.Patient;
import by.dlstudio.hospital.domain.enums.DiseaseType;
import by.dlstudio.hospital.domain.enums.Qualification;
import by.dlstudio.hospital.domain.repository.DoctorRepository;
import by.dlstudio.hospital.domain.repository.PatientRepository;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;
import by.dlstudio.hospital.service.impl.DoctorServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DoctorServiceImplTest {

    @Autowired
    private DoctorServiceImpl doctorService;

    @Autowired
    private DoctorRepository doctorRepository;

    @MockBean
    private PatientRepository patientRepository;

    @Test
    void findDoctorByIdTest_Success() {
        Doctor doctor = new Doctor("test", "test", "test");
        doctor.setQualification(Qualification.TEST);

        doctorRepository.save(doctor);
        assertEquals(doctor, doctorService.findDoctorById(doctor.getId()).orElseThrow());
    }

    @Test
    void findDoctorByIdTest_EmptyOptional() {
        assertTrue(doctorService.findDoctorById(99L).isEmpty());
    }

    @Test
    void findDoctorByPhoneNumberTest_Success() {
        Doctor doctor = new Doctor("test1", "test1", "test1");
        doctor.setQualification(Qualification.TEST);

        doctor = doctorRepository.save(doctor);
        assertEquals(doctor, doctorService.findDoctorByPhoneNumber(doctor.getPhoneNumber()).orElseThrow());
    }

    @Test
    void findDoctorByPhoneNumberTest_EmptyOptional() {
        assertTrue(doctorService.findDoctorByPhoneNumber("missing").isEmpty());
    }

    @Test
    void createOrUpdateDoctorTest_Success() {
        Doctor doctor = new Doctor("test2", "test2", "test2");
        doctor.setQualification(Qualification.TEST);

        doctor = doctorService.createOrUpdateDoctor(doctor);
        assertNotNull(doctor.getId());

        doctor.setSurname("changed");
        doctor = doctorService.createOrUpdateDoctor(doctor);

        assertEquals("changed", doctor.getSurname());
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class DoctorAvailabilityTestNest {

        @BeforeAll
        public void startUp() {
            Doctor doctor = new Doctor("test3","test3","test3");
            doctor.setQualification(Qualification.TEST);
            doctorRepository.save(doctor);
        }

        @BeforeEach
        public void init() {
            Patient patient = new Patient("test","test","test");
            patient.setId(99L);
            Disease disease = new Disease("test",DiseaseType.TEST_ONE);
            disease.getPatients().add(patient);
            disease.setId(99L);
            patient.getDiseases().add(disease);
            Mockito.when(patientRepository.findPatientById(patient.getId()))
                    .thenReturn(Optional.of(patient));
        }

        @Test
        void checkDoctorAvailabilityTest_DoctorAvailable() {
            Doctor doctor = doctorRepository.findDoctorByPhoneNumber("test3").orElseThrow();
            try {
                assertTrue(doctorService.checkDoctorAvailability(doctor, 99L));
            } catch (HospitalDatabaseException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        void checkDoctorAvailabilityTest_DoctorNotAvailable() {
            Doctor doctor = doctorRepository.findDoctorByPhoneNumber("test3").orElseThrow();
            doctor.setPatient(patientRepository.findPatientById(99L).orElseThrow());
            try {
                assertFalse(doctorService.checkDoctorAvailability(doctor, 99L));
            } catch (HospitalDatabaseException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        void checkDoctorAvailabilityTest_HospitalDatabaseException_PatientNotFound() {
            Doctor doctor = doctorRepository.findDoctorByPhoneNumber("test3").orElseThrow();
            assertThrows(HospitalDatabaseException.class, ()->
                    doctorService.checkDoctorAvailability(doctor,100L));
        }
    }

    @Test
    void deleteDoctorTest_Success() {
        Doctor doctor = new Doctor("test4","test4","test4");
        doctor.setQualification(Qualification.TEST);
        doctor = doctorRepository.save(doctor);
        Patient patient = new Patient("t","t","t");
        patient.setId(100L);
        patient.setDoctor(doctor);
        doctor.setPatient(patient);

        Patient freePatient = new Patient("t","t","t");
        freePatient.setId(100L);

        Mockito.when(patientRepository.save(freePatient)).thenReturn(freePatient);
        doctorService.deleteDoctor(doctor);
        Mockito.verify(patientRepository).save(freePatient);

        assertTrue(doctorRepository.findDoctorByPhoneNumber("test4").isEmpty());
    }

    @Test
    void deleteDoctorByIdTest_Success() {
        Doctor doctor = new Doctor("test5","test5","test5");
        doctor.setQualification(Qualification.TEST);
        doctor = doctorRepository.save(doctor);
        try {
            doctorService.deleteDoctorById(doctor.getId());
        } catch (HospitalDatabaseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(doctorRepository.findDoctorByPhoneNumber("test5").isEmpty());
    }

    @Test
    void deleteDoctorByIdTest_DatabaseHospitalException_DoctorNotFound() {
        assertThrows(HospitalDatabaseException.class, ()->doctorService.deleteDoctorById(99L));
    }
}
