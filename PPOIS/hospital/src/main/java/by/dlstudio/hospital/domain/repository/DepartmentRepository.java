package by.dlstudio.hospital.domain.repository;

import by.dlstudio.hospital.domain.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findDepartmentById(Long id);

    Optional<Department> findDepartmentByName(String name);
}
