package br.com.fiap.reserva_sala_reuniao.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Recurso;

import java.util.Optional;

public interface RecursoRepository extends JpaRepository<Recurso, String> {
    
    Optional<Recurso> findByNomeIgnoreCase(String nome);
}
