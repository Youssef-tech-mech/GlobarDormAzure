package ntu.service_centric.global_dorm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ntu.service_centric.global_dorm")
public class GlobalDorm {
	public static void main(String[] args) {
		SpringApplication.run(GlobalDorm.class, args);
	}
}
