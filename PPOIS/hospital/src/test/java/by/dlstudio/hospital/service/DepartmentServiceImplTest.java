package by.dlstudio.hospital.service;

import by.dlstudio.hospital.domain.entity.Department;
import by.dlstudio.hospital.domain.entity.Doctor;
import by.dlstudio.hospital.domain.entity.HospitalRoom;
import by.dlstudio.hospital.domain.entity.Patient;
import by.dlstudio.hospital.domain.enums.Qualification;
import by.dlstudio.hospital.domain.repository.DepartmentRepository;
import by.dlstudio.hospital.domain.repository.DoctorRepository;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;
import by.dlstudio.hospital.service.exception.HospitalDepartmentException;
import by.dlstudio.hospital.service.exception.HospitalDoctorException;
import by.dlstudio.hospital.service.exception.HospitalRoomException;
import by.dlstudio.hospital.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DepartmentServiceImplTest {

    @Autowired
    private DepartmentServiceImpl departmentService;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @MockBean
    private DoctorService doctorService;
    @MockBean
    private PatientService patientService;
    @MockBean
    private HospitalRoomService hospitalRoomService;

    @Test
    void findDepartmentByIdTest_Success() {
        Department department = new Department();
        department.setName("test");
        department = departmentRepository.save(department);
        assertEquals(department,departmentService.findDepartmentById(department.getId()).orElseThrow());
    }

    @Test
    void findDepartmentByNameTest_Success() {
        Department department = new Department();
        department.setName("test1");
        department = departmentRepository.save(department);
        assertEquals(department, departmentService.findDepartmentByName(department.getName()).orElseThrow());
    }

    @Test
    void createOrUpdateDepartmentTest_Success() {
        Department department = new Department();
        department.setName("test2");
        department = departmentService.createOrUpdateDepartment(department);
        assertNotNull(department.getId());
        department.setName("changed");
        department = departmentService.createOrUpdateDepartment(department);
        assertEquals(department, departmentRepository.findDepartmentByName("changed").orElseThrow());
    }

    @Test
    void deleteDepartmentTest_Success() {
        Department department = new Department();
        department.setName("test3");
        department = departmentRepository.save(department);

        HospitalRoom hospitalRoom = new HospitalRoom();
        hospitalRoom.setDepartment(department);
        hospitalRoom.setSlots(1);
        hospitalRoom.setId(100L);

        department.getHospitalRooms().add(hospitalRoom);
        departmentService.deleteDepartment(department);
        Mockito.verify(hospitalRoomService).deleteRoom(hospitalRoom);

        assertTrue(departmentRepository.findDepartmentByName("test3").isEmpty());
    }

    @Test
    void deleteDepartmentByIdTest_Success() {
        Department department = new Department();
        department.setName("test4");
        department = departmentRepository.save(department);
        try {
            departmentService.deleteDepartmentById(department.getId());
        } catch (HospitalDatabaseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(departmentRepository.findDepartmentByName("test4").isEmpty());
    }

    @Test
    void deleteDepartmentByIdTestSuccess_HospitalDatabaseException_DepartmentNotFound() {
        assertThrows(HospitalDatabaseException.class,()->departmentService.deleteDepartmentById(99L));
    }

    @Test
    void findFreeRoomInDepartmentTest_Success() {
        Department department = new Department();
        department.setName("test0");
        department.setId(100L);
        HospitalRoom hospitalRoom = new HospitalRoom();
        hospitalRoom.setId(100L);
        hospitalRoom.setSlots(1);
        hospitalRoom.setDepartment(department);
        department.getHospitalRooms().add(hospitalRoom);

        Mockito.when(hospitalRoomService.checkRoomAvailability(hospitalRoom)).thenReturn(true);
        assertEquals(Optional.of(hospitalRoom), departmentService.findFreeRoomInDepartment(department));
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class DepartmentAssigningTestNest {

        private final Long TEST_ID = 100L;
        private Long testDocId;

        @BeforeAll
        public void dataStartUp() {
            Doctor doctorFromDb = new Doctor("a", "a", "a");
            doctorFromDb.setQualification(Qualification.TEST);
            doctorFromDb = doctorRepository.save(doctorFromDb);
            testDocId = doctorFromDb.getId();
        }

        @BeforeEach
        public void init() {
            Patient patientFromDb = new Patient("t", "t", "t");
            patientFromDb.setId(TEST_ID);
            Doctor doctorFromDb = doctorRepository.findDoctorById(testDocId).orElseThrow();
            Mockito.when(doctorService.findDoctorById(testDocId)).thenReturn(Optional.of(doctorFromDb));
            Mockito.when(patientService.findPatientById(TEST_ID)).thenReturn(Optional.of(patientFromDb));
        }

        @Test
        public void assignDoctorToDepartmentTest_Success() {
            Department department = new Department();
            department.setName("test5");
            department = departmentRepository.save(department);

            Doctor expDoctor = doctorRepository.findDoctorById(testDocId).orElseThrow();
            expDoctor.setDepartment(department);

            try {
                Mockito.when(doctorService.createOrUpdateDoctor(expDoctor)).thenReturn(expDoctor);
                departmentService.assignDoctorToDepartment(department, testDocId);
                Mockito.verify(doctorService).createOrUpdateDoctor(expDoctor);
            } catch (HospitalDatabaseException e) {
                throw new RuntimeException(e);
            }
            assertNotEquals(0, department.getDoctors().size());
            expDoctor.setDepartment(null);
            doctorRepository.save(expDoctor);
        }

        @Test
        public void assignDoctorToDepartmentTest_HospitalDatabaseException_DoctorNotFound() {
            assertThrows(HospitalDatabaseException.class,
                    () -> departmentService.assignDoctorToDepartment(new Department(), TEST_ID));
        }

        @Test
        public void directAssignDoctorToPatientTest_Success() {
            Doctor doctorFromDb = doctorRepository.findDoctorById(testDocId).orElseThrow();
            Patient patientFromDb = patientService.findPatientById(TEST_ID).orElseThrow();
            Doctor docWithPatient = doctorRepository.findDoctorById(testDocId).orElseThrow();
            docWithPatient.setPatient(patientFromDb);
            patientFromDb.setDoctor(docWithPatient);

            try {
                Mockito.when(doctorService.checkDoctorAvailability(doctorFromDb, TEST_ID)).thenReturn(true);
                Mockito.when(doctorService.createOrUpdateDoctor(docWithPatient)).thenReturn(docWithPatient);
                departmentService.assignDoctorToPatient(testDocId, TEST_ID);
                Mockito.verify(doctorService).createOrUpdateDoctor(docWithPatient);
                Mockito.verify(patientService).registerOrUpdatePatient(patientFromDb);
            } catch (HospitalDatabaseException | HospitalDoctorException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        public void directAssignDoctorToPatientTest_HospitalDatabaseException_DoctorNotFound() {
            assertThrows(HospitalDatabaseException.class,
                    () -> departmentService.assignDoctorToPatient(99L, TEST_ID));
        }

        @Test
        public void directAssignDoctorToPatientTest_HospitalDatabaseException_PatientNotFound() {
            assertThrows(HospitalDatabaseException.class,
                    () -> departmentService.assignDoctorToPatient(testDocId, 99L));
        }

        @Test
        public void directAssignDoctorToPatientTest_HospitalDoctorException() {
            Doctor doctor = doctorRepository.findDoctorById(testDocId).orElseThrow();
            try {
                Mockito.when(doctorService.checkDoctorAvailability(doctor, TEST_ID)).thenReturn(false);
                assertThrows(HospitalDoctorException.class,
                        () -> departmentService.assignDoctorToPatient(testDocId, TEST_ID));
            } catch (HospitalDatabaseException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        public void assignDoctorToPatientTest_Success() {
            Department department = new Department();
            department.setName("test6");
            Doctor doctorFromDb = doctorRepository.findDoctorById(testDocId).orElseThrow();
            HospitalRoom freeRoom = new HospitalRoom();
            freeRoom.setSlots(1);
            freeRoom.setId(TEST_ID);
            freeRoom.setDepartment(department);
            department.getHospitalRooms().add(freeRoom);
            Patient patient = patientService.findPatientById(TEST_ID).orElseThrow();
            Doctor docWithPatient = doctorRepository.findDoctorById(testDocId).orElseThrow();
            docWithPatient.setPatient(patient);
            department.getDoctors().add(doctorFromDb);

            try {
                Mockito.when(doctorService.checkDoctorAvailability(doctorFromDb, TEST_ID)).thenReturn(true);
                Mockito.when(doctorService.createOrUpdateDoctor(docWithPatient)).thenReturn(docWithPatient);
                Mockito.when(hospitalRoomService.checkRoomAvailability(freeRoom)).thenReturn(true);
                departmentService.assignDoctorToPatient(department,TEST_ID);
                Mockito.verify(hospitalRoomService).assignPatientToRoom(freeRoom,TEST_ID);
            } catch (HospitalDatabaseException | HospitalRoomException | HospitalDoctorException |
                     HospitalDepartmentException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        public void assignDoctorToPatientTest_HospitalDepartmentException_NoAvailableDoctors() {
            Doctor doctor = doctorRepository.findDoctorById(testDocId).orElseThrow();
            Department department = new Department();
            department.setName("test7");
            try {
                Mockito.when(doctorService.checkDoctorAvailability(doctor,TEST_ID)).thenReturn(false);
                assertThrows(HospitalDepartmentException.class,
                        ()->departmentService.assignDoctorToPatient(department,TEST_ID));
            } catch (HospitalDatabaseException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        public void assignDoctorToPatientTest_HospitalDepartmentException_NoFreeRooms() {
            Doctor doctor = doctorRepository.findDoctorById(testDocId).orElseThrow();
            Department department = new Department();
            department.setName("test8");
            department.getDoctors().add(doctor);

            assertThrows(HospitalDepartmentException.class,
                    ()->departmentService.assignDoctorToPatient(department,TEST_ID));
        }

        @Test
        public void assignDoctorToPatientTest_HospitalRoomException_NoFreeSlots() {
            Department department = new Department();
            department.setName("test6");
            Doctor doctorFromDb = doctorRepository.findDoctorById(testDocId).orElseThrow();
            HospitalRoom occupiedRoom = new HospitalRoom();
            occupiedRoom.setSlots(1);
            occupiedRoom.setId(TEST_ID);
            occupiedRoom.setDepartment(department);
            department.getHospitalRooms().add(occupiedRoom);
            department.getDoctors().add(doctorFromDb);

            try {
                Mockito.when(doctorService.checkDoctorAvailability(doctorFromDb, TEST_ID)).thenReturn(true);
                Mockito.when(hospitalRoomService.checkRoomAvailability(occupiedRoom)).thenReturn(true);
                Mockito.when(hospitalRoomService.assignPatientToRoom(occupiedRoom,TEST_ID)).thenThrow(new HospitalRoomException());
                assertThrows(HospitalRoomException.class,
                        ()->departmentService.assignDoctorToPatient(department,TEST_ID));
            } catch (HospitalDatabaseException | HospitalRoomException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
