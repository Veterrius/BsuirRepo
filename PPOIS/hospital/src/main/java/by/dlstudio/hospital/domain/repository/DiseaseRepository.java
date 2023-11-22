package by.dlstudio.hospital.domain.repository;

import by.dlstudio.hospital.domain.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {

    Optional<Disease> findDiseaseById(Long id);

    Optional<Disease> findDiseaseByName(String name);
}
