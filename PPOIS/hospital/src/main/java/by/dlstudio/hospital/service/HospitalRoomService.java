package by.dlstudio.hospital.service;

import by.dlstudio.hospital.domain.entity.HospitalRoom;
import by.dlstudio.hospital.domain.entity.Patient;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;
import by.dlstudio.hospital.service.exception.HospitalRoomException;

import java.util.Optional;

public interface HospitalRoomService {

    Optional<HospitalRoom> findRoomById(Long id);

    HospitalRoom createRoomInDepartment(HospitalRoom hospitalRoom, Long departmentId) throws HospitalDatabaseException;

    HospitalRoom updateRoom(HospitalRoom hospitalRoom);

    void deleteRoom(HospitalRoom roomFromDb);

    void deleteRoomById(Long id) throws HospitalDatabaseException;

    boolean checkRoomAvailability(HospitalRoom roomFromDb);

    Patient assignPatientToRoom(HospitalRoom roomFromDb, Long patientId) throws HospitalDatabaseException, HospitalRoomException;
}
