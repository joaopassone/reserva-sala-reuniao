package br.com.fiap.reserva_sala_reuniao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ReservaSalaReuniaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservaSalaReuniaoApplication.class, args);
	}

}
