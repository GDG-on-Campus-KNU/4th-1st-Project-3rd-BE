package GDG4th.personaChat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PersonaChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonaChatApplication.class, args);
	}

}
