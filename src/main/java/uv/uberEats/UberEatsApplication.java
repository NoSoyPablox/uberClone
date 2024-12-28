package uv.uberEats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

//@EntityScan(basePackages = "uv.uberEats.models")
@SpringBootApplication
public class UberEatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UberEatsApplication.class, args);
	}

}
