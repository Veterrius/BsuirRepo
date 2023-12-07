package by.dlstudio.hospital.service;

import by.dlstudio.hospital.domain.entity.Department;
import by.dlstudio.hospital.domain.entity.HospitalRoom;
import by.dlstudio.hospital.domain.entity.Patient;
import by.dlstudio.hospital.domain.repository.DepartmentRepository;
import by.dlstudio.hospital.domain.repository.HospitalRoomRepository;
import by.dlstudio.hospital.domain.repository.PatientRepository;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;
import by.dlstudio.hospital.service.exception.HospitalRoomException;
import by.dlstudio.hospital.service.impl.HospitalRoomServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HospitalRoomServiceImplTest {

    @Autowired
    private HospitalRoomServiceImpl hospitalRoomService;
    @Autowired
    private HospitalRoomRepository hospitalRoomRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private PatientRepository patientRepository;

    private final String TEST_DEPARTMENT_NAME = "roomTestDep";

    @BeforeAll
    public void init() {
        Department department = new Department();
        department.setName(TEST_DEPARTMENT_NAME);
        department = departmentRepository.save(department);
    }

    @Test
    public void findRoomByIdTest_Success() {
        HospitalRoom hospitalRoom = new HospitalRoom();
        hospitalRoom.setSlots(1);
        hospitalRoom.setDepartment(departmentRepository.findDepartmentByName(TEST_DEPARTMENT_NAME).orElseThrow());
        hospitalRoom = hospitalRoomRepository.save(hospitalRoom);

        assertEquals(hospitalRoom, hospitalRoomRepository.findHospitalRoomById(hospitalRoom.getId()).orElseThrow());
    }

    @Test
    public void updateRoomTest_Success() {
        HospitalRoom hospitalRoom = new HospitalRoom();
        hospitalRoom.setSlots(1);
        hospitalRoom.setDepartment(departmentRepository.findDepartmentByName(TEST_DEPARTMENT_NAME).orElseThrow());
        hospitalRoom = hospitalRoomRepository.save(hospitalRoom);

        int notExpSlots = hospitalRoom.getSlots();

        hospitalRoom.setSlots(5);
        hospitalRoom = hospitalRoomService.updateRoom(hospitalRoom);
        assertNotEquals(notExpSlots, hospitalRoom.getSlots());
    }

    @Test
    public void deleteRoomTest_Success() {
        HospitalRoom hospitalRoom = new HospitalRoom();
        hospitalRoom.setSlots(1);
        hospitalRoom.setDepartment(departmentRepository.findDepartmentByName(TEST_DEPARTMENT_NAME).orElseThrow());
        hospitalRoom = hospitalRoomRepository.save(hospitalRoom);
        Patient patient = new Patient("r", "r", "r");
        patient.setHospitalRoom(hospitalRoom);
        patient = patientRepository.save(patient);
        hospitalRoom.getPatients().add(patient);
        hospitalRoom = hospitalRoomRepository.save(hospitalRoom);

        hospitalRoom = hospitalRoomRepository.save(hospitalRoom);
        hospitalRoomService.deleteRoom(hospitalRoom);
        assertTrue(hospitalRoomRepository.findHospitalRoomById(hospitalRoom.getId()).isEmpty());
        assertNull(patientRepository.findPatientById(patient.getId()).orElseThrow().getHospitalRoom());
    }

    @Test
    public void deleteRoomByIdTest_Success() {
        HospitalRoom hospitalRoom = new HospitalRoom();
        hospitalRoom.setSlots(5);
        hospitalRoom.setDepartment(departmentRepository.findDepartmentByName(TEST_DEPARTMENT_NAME).orElseThrow());
        hospitalRoom = hospitalRoomRepository.save(hospitalRoom);

        try {
            hospitalRoomService.deleteRoomById(hospitalRoom.getId());
        } catch (HospitalDatabaseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(hospitalRoomRepository.findHospitalRoomById(hospitalRoom.getId()).isEmpty());
    }

    @Test
    public void deleteRoomByIdTest_HospitalDatabaseException_RoomNotFound() {
        assertThrows(HospitalDatabaseException.class, () -> hospitalRoomService.deleteRoomById(99L));
    }

    @Test
    public void createRoomInDepartmentTest_Success() {
        Department department = new Department();
        department.setName("roomTest");
        department = departmentRepository.save(department);

        HospitalRoom hospitalRoom = new HospitalRoom();
        hospitalRoom.setSlots(5);

        try {
            hospitalRoom = hospitalRoomService.createRoomInDepartment(hospitalRoom, department.getId());
        } catch (HospitalDatabaseException e) {
            throw new RuntimeException(e);
        }

        assertEquals(hospitalRoom.getDepartment(),
                hospitalRoomRepository.findHospitalRoomById(hospitalRoom.getId()).orElseThrow().getDepartment());
    }

    @Test
    public void createRoomInDepartmentTest_HospitalDatabaseException_DepartmentNotFound() {
        assertThrows(HospitalDatabaseException.class,
                () -> hospitalRoomService.createRoomInDepartment(new HospitalRoom(), 99L));
    }

    @Test
    public void checkRoomAvailabilityTest_Success() {
        HospitalRoom hospitalRoom = new HospitalRoom();
        hospitalRoom.setSlots(1);
        hospitalRoom.setDepartment(departmentRepository.findDepartmentByName(TEST_DEPARTMENT_NAME).orElseThrow());
        hospitalRoom = hospitalRoomRepository.save(hospitalRoom);

        assertTrue(hospitalRoomService.checkRoomAvailability(hospitalRoom));
    }

    @Test
    public void assignPatientToRoomTest_Success() {
        HospitalRoom hospitalRoom = new HospitalRoom();
        hospitalRoom.setSlots(1);
        hospitalRoom.setDepartment(departmentRepository.findDepartmentByName(TEST_DEPARTMENT_NAME).orElseThrow());
        hospitalRoom = hospitalRoomRepository.save(hospitalRoom);

        Patient patient = new Patient("r1","r1","r1");
        patient = patientRepository.save(patient);
        try {
            patient = hospitalRoomService.assignPatientToRoom(hospitalRoom, patient.getId());
        } catch (HospitalDatabaseException | HospitalRoomException e) {
            throw new RuntimeException(e);
        }

        assertEquals(patient.getHospitalRoom(),
                patientRepository.findPatientById(patient.getId()).orElseThrow().getHospitalRoom());
    }

    @Test
    public void assignPatientToRoomTest_HospitalDatabaseException_PatientNotFound() {
        assertThrows(HospitalDatabaseException.class,
                ()->hospitalRoomService.assignPatientToRoom(new HospitalRoom(),99L));
    }

    @Test
    public void assignPatientToRoomTest_HospitalRoomException_NoSlotsLeft() {
        HospitalRoom hospitalRoom = new HospitalRoom();
        hospitalRoom.setSlots(1);
        hospitalRoom.setDepartment(departmentRepository.findDepartmentByName(TEST_DEPARTMENT_NAME).orElseThrow());
        hospitalRoom = hospitalRoomRepository.save(hospitalRoom);

        Patient patient = new Patient("r2","r2","r2");
        patient.setHospitalRoom(hospitalRoom);
        patient = patientRepository.save(patient);

        hospitalRoom.getPatients().add(patient);
        hospitalRoom = hospitalRoomRepository.save(hospitalRoom);

        HospitalRoom finalHospitalRoom = hospitalRoom;
        Patient finalPatient = patient;
        assertThrows(HospitalRoomException.class,
                ()->hospitalRoomService.assignPatientToRoom(finalHospitalRoom, finalPatient.getId()));
    }
}
