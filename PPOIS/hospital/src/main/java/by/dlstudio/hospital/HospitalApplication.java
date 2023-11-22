package by.dlstudio.hospital;

import by.dlstudio.hospital.domain.entity.*;
import by.dlstudio.hospital.domain.enums.DiseaseType;
import by.dlstudio.hospital.domain.enums.Qualification;
import by.dlstudio.hospital.domain.repository.DepartmentRepository;
import by.dlstudio.hospital.domain.repository.DoctorRepository;
import by.dlstudio.hospital.domain.repository.HospitalRoomRepository;
import by.dlstudio.hospital.domain.repository.PatientRepository;
import by.dlstudio.hospital.service.*;
import by.dlstudio.hospital.service.exception.HospitalDatabaseException;
import by.dlstudio.hospital.service.exception.HospitalDepartmentException;
import by.dlstudio.hospital.service.exception.HospitalDoctorException;
import by.dlstudio.hospital.service.exception.HospitalRoomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.print.Doc;

@SpringBootApplication
public class HospitalApplication {

	private static final Logger LOG = LoggerFactory.
			getLogger(HospitalApplication.class);

	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION");
		SpringApplication.run(HospitalApplication.class, args);
		LOG.info("APPLICATION FINISHED");
	}

	@Bean
	public CommandLineRunner demo(PatientService patientService, HospitalRoomService hospitalRoomService,
								  DoctorService doctorService, DepartmentService departmentService,
								  DiseaseService diseaseService) {
		return args -> {

        };
	}
}
