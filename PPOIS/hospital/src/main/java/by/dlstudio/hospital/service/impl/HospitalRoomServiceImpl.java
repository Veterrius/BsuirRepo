package by.dlstudio.hospital.service.impl;

import by.dlstudio.hospital.domain.entity.Department;
import by.dlstudio.hospital.domain.entity.HospitalRoom;
import by.dlstudio.hospital.domain.entity.Patient;
import by.dlstudio.hospital.domain.repository.DepartmentRepository;
import by.dlstudio.hospital.domain.repository.HospitalRoomRepository;
import by.dlstudio.hospital.domain.repository.PatientRepository;
import by.dlstudio.hospital.service.HospitalRoomService;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;
import by.dlstudio.hospital.service.exception.HospitalRoomException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HospitalRoomServiceImpl implements HospitalRoomService {

    private final HospitalRoomRepository hospitalRoomRepository;
    private final DepartmentRepository departmentRepository;
    private final PatientRepository patientRepository;

    public HospitalRoomServiceImpl(HospitalRoomRepository hospitalRoomRepository, DepartmentRepository departmentRepository,
                                   PatientRepository patientRepository) {
        this.hospitalRoomRepository = hospitalRoomRepository;
        this.departmentRepository = departmentRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public Optional<HospitalRoom> findRoomById(Long id) {
        return hospitalRoomRepository.findHospitalRoomById(id);
    }

    @Override
    public HospitalRoom updateRoom(HospitalRoom hospitalRoom) {
        return hospitalRoomRepository.save(hospitalRoom);
    }

    /**
     * This method creates a room in a specified department because a room cannot
     * exist without being in a department.
     * @param hospitalRoom is a room we create
     * @param departmentId is a department's id
     * @return hospital room with a not null department
     * @throws HospitalDatabaseException if department wasn't found
     */
    @Override
    public HospitalRoom createRoomInDepartment(HospitalRoom hospitalRoom, Long departmentId) throws HospitalDatabaseException {
        Department departmentFromDb = departmentRepository.findDepartmentById(departmentId)
                .orElseThrow(()->new HospitalDatabaseException("Department not found"));
        departmentFromDb.getHospitalRooms().add(hospitalRoom);
        departmentFromDb = departmentRepository.save(departmentFromDb);
        hospitalRoom.setDepartment(departmentFromDb);
        return hospitalRoomRepository.save(hospitalRoom);
    }

    /**
     * This method deletes a room by using the {@link HospitalRoomRepository}'s method
     * Before that, it clears the room's patients.
     * @param roomFromDb is a room we want to delete.
     */
    @Override
    public void deleteRoom(HospitalRoom roomFromDb) {
        roomFromDb.getPatients().forEach(p -> {
            p.setHospitalRoom(null);
            patientRepository.save(p);
        });
        hospitalRoomRepository.delete(roomFromDb);
    }

    /**
     * This method deletes a room by firstly getting it by id
     * and then invoking the deleteRoom() method.
     * @param id is an id of a room we want to create
     * @throws HospitalDatabaseException if room wasn't found
     */
    @Override
    public void deleteRoomById(Long id) throws HospitalDatabaseException {
        HospitalRoom roomFromDb = findRoomById(id)
                .orElseThrow(()->new HospitalDatabaseException(("Room not found")));
        deleteRoom(roomFromDb);
    }

    /**
     * This method checks if room is available by comparing it's
     * actual patients number and slots value.
     * @param roomFromDb is a room we check
     * @return true if room has free slots
     */
    @Override
    public boolean checkRoomAvailability(HospitalRoom roomFromDb) {
        return roomFromDb.getPatients().size() < roomFromDb.getSlots();
    }

    /**
     * This method assigns a patient to the specified room.
     * Firstly, it checks a room's availability by calling
     * the checkRoomAvailability() method
     * @param roomFromDb is a room we want to add patient to
     * @param patientId is a patient's id
     * @return patient with an assigned room
     * @throws HospitalDatabaseException if patient was't found
     * @throws HospitalRoomException if room isn't available
     */
    @Override
    public Patient assignPatientToRoom(HospitalRoom roomFromDb, Long patientId) throws HospitalDatabaseException, HospitalRoomException {
        Patient patientFromDb = patientRepository.findPatientById(patientId)
                .orElseThrow(()->new HospitalDatabaseException("Patient not found"));
        if (!checkRoomAvailability(roomFromDb)) throw new HospitalRoomException("No free slots left");
        roomFromDb.getPatients().add(patientFromDb);
        patientFromDb.setHospitalRoom(roomFromDb);
        hospitalRoomRepository.save(roomFromDb);
        return patientRepository.save(patientFromDb);
    }


}
