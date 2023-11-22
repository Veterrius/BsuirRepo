package by.dlstudio.hospital.domain.repository;

import by.dlstudio.hospital.domain.entity.HospitalRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospitalRoomRepository extends JpaRepository<HospitalRoom, Long> {

    Optional<HospitalRoom> findHospitalRoomById(Long id);
}
