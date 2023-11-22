package by.dlstudio.hospital.service;

import by.dlstudio.hospital.domain.entity.Department;
import by.dlstudio.hospital.domain.entity.Doctor;
import by.dlstudio.hospital.domain.entity.HospitalRoom;
import by.dlstudio.hospital.domain.entity.Patient;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;
import by.dlstudio.hospital.service.exception.HospitalDepartmentException;
import by.dlstudio.hospital.service.exception.HospitalDoctorException;
import by.dlstudio.hospital.service.exception.HospitalRoomException;

import java.util.Optional;

public interface DepartmentService {

    Optional<Department> findDepartmentById(Long id);

    Optional<Department> findDepartmentByName(String name);

    Department createOrUpdateDepartment(Department department);

    void deleteDepartment(Department departmentFromDb);

    void deleteDepartmentById(Long id) throws HospitalDatabaseException;

    Doctor assignDoctorToDepartment(Department departmentFromDb, Long doctorId) throws HospitalDatabaseException, HospitalDepartmentException;

    void assignDoctorToPatient(Long doctorId, Long patientId) throws HospitalDatabaseException, HospitalDoctorException;

    void assignDoctorToPatient(Department departmentFromDb, Long patientId) throws HospitalDoctorException, HospitalDatabaseException, HospitalDepartmentException, HospitalRoomException;

    Optional<HospitalRoom> findFreeRoomInDepartment(Department departmentFromDb);
}
