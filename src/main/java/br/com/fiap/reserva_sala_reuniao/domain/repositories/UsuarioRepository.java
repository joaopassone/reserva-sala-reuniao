package br.com.fiap.reserva_sala_reuniao.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    
    // Método utilizado pelo TokenService e SecurityFilter para validar o login
    Optional<Usuario> findByEmail(String email);
}
