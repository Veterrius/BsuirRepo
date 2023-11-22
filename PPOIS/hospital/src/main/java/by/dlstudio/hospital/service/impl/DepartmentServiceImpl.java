package by.dlstudio.hospital.service.impl;

import by.dlstudio.hospital.domain.entity.*;
import by.dlstudio.hospital.domain.repository.DepartmentRepository;
import by.dlstudio.hospital.service.DepartmentService;
import by.dlstudio.hospital.service.DoctorService;
import by.dlstudio.hospital.service.HospitalRoomService;
import by.dlstudio.hospital.service.PatientService;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;
import by.dlstudio.hospital.service.exception.HospitalDepartmentException;
import by.dlstudio.hospital.service.exception.HospitalDoctorException;
import by.dlstudio.hospital.service.exception.HospitalRoomException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final HospitalRoomService hospitalRoomService;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DoctorService doctorService,
                                 PatientService patientService, HospitalRoomService hospitalRoomService) {
        this.departmentRepository = departmentRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.hospitalRoomService = hospitalRoomService;
    }

    @Override
    public Optional<Department> findDepartmentById(Long id) {
        return departmentRepository.findDepartmentById(id);
    }

    @Override
    public Optional<Department> findDepartmentByName(String name) {
        return departmentRepository.findDepartmentByName(name);
    }

    @Override
    public Department createOrUpdateDepartment(Department department) {
        return departmentRepository.save(department);
    }

    /**
     * This method deletes a specified department using a {@link DepartmentRepository} method.
     * Before that, it clears all of the hospital rooms of a deleted department.
     * @param departmentFromDb is a department we want to delete
     */
    @Override
    public void deleteDepartment(Department departmentFromDb) {
        for (HospitalRoom room : departmentFromDb.getHospitalRooms()) {
            hospitalRoomService.deleteRoom(room);
        }
        departmentFromDb.setHospitalRooms(null);
        departmentFromDb = departmentRepository.save(departmentFromDb);
        departmentRepository.delete(departmentFromDb);
    }

    /**
     * This method deletes a department by firstly finding it by id
     * and then invoking the deleteDepartment() method.
     * @param id is an id of department
     * @throws HospitalDatabaseException if department wasn't found
     */
    @Override
    public void deleteDepartmentById(Long id) throws HospitalDatabaseException {
        Department departmentFromDb = findDepartmentById(id)
                .orElseThrow(()->new HospitalDatabaseException(("Department not found")));
        deleteDepartment(departmentFromDb);
    }

    /**
     * This method assigns a doctor to a specified department
     * @param departmentFromDb is a department from database that we want
     *                         to assign doctor to
     * @param doctorId is an id of the doctor we want to assign
     * @return doctor with not null department
     * @throws HospitalDatabaseException if doctor wasn't found
     */
    @Override
    public Doctor assignDoctorToDepartment(Department departmentFromDb, Long doctorId) throws HospitalDatabaseException {
        Doctor doctorFromDb = doctorService.findDoctorById(doctorId)
                .orElseThrow(()->new HospitalDatabaseException(("Doctor not found")));
        doctorFromDb.setDepartment(departmentFromDb);
        doctorFromDb = doctorService.createOrUpdateDoctor(doctorFromDb);
        departmentFromDb.getDoctors().add(doctorFromDb);
        departmentRepository.save(departmentFromDb);
        return doctorFromDb;
    }

    /**
     * This method assigns a specified doctor to the specified patient
     * @param doctorId is an id of a doctor we want to assign
     * @param patientId is an id of a patient
     * @throws HospitalDatabaseException if doctor or patient weren't found
     * @throws HospitalDoctorException if doctor is already assigned
     * or lacks needed qualification
     */
    @Override
    public void assignDoctorToPatient(Long doctorId, Long patientId) throws HospitalDatabaseException, HospitalDoctorException {
        Doctor doctorFromDb = doctorService.findDoctorById(doctorId)
                .orElseThrow(()->new HospitalDatabaseException(("Doctor not found")));
        Patient patientFromDb = patientService.findPatientById(patientId)
                .orElseThrow(()->new HospitalDatabaseException(("Patient not found")));
        if (!doctorService.checkDoctorAvailability(doctorFromDb, patientId))
            throw new HospitalDoctorException("This doctor is already assigned to another patient or lacks qualification");
        doctorFromDb.setPatient(patientFromDb);
        doctorFromDb = doctorService.createOrUpdateDoctor(doctorFromDb);
        patientFromDb.setDoctor(doctorFromDb);
        patientService.registerOrUpdatePatient(patientFromDb);
    }

    /**
     * This method assigns a valid doctor from a department to a specified patient
     * It is important that it then invokes assignDoctorToPatient() method.
     * @param departmentFromDb is a department in which we want to find doctor in
     * @param patientId is a patient's id
     * @throws HospitalDoctorException if doctor is already assigned
     * or lacks needed qualification
     * @throws HospitalDatabaseException if doctor or patient weren't found
     * @throws HospitalDepartmentException if department doesn't have
     * a free room or a matching doctor
     * @throws HospitalRoomException if the found room for patient actually wasn't free
     */
    @Override
    public void assignDoctorToPatient(Department departmentFromDb, Long patientId) throws HospitalDoctorException, HospitalDatabaseException, HospitalDepartmentException, HospitalRoomException {
        for (Doctor depDoctor : departmentFromDb.getDoctors()) {
            if (doctorService.checkDoctorAvailability(depDoctor, patientId)) {
                HospitalRoom freeRoom = findFreeRoomInDepartment(departmentFromDb)
                        .orElseThrow(()->new HospitalDepartmentException("Department doesn't have a free room"));
                assignDoctorToPatient(depDoctor.getId(),patientId);
                hospitalRoomService.assignPatientToRoom(freeRoom, patientId);
                return;
            }
        }
        throw new HospitalDepartmentException("Department doesn't have a matching doctor");
    }

    /**
     * This method finds free room in department
     * @param departmentFromDb is a department we want to find the free room in
     * @return Optional of found room.
     */
    @Override
    public Optional<HospitalRoom> findFreeRoomInDepartment(Department departmentFromDb) {
        return departmentFromDb.getHospitalRooms().stream()
                .filter(hospitalRoomService::checkRoomAvailability).findAny();
    }
}
