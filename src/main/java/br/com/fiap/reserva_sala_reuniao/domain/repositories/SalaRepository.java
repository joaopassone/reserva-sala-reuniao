package br.com.fiap.reserva_sala_reuniao.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Sala;

import java.util.List;

public interface SalaRepository extends JpaRepository<Sala, String> {
        
    // Busca salas filtrando por uma capacidade mínima aceitável de pessoas
    List<Sala> findByCapacidadeGreaterThanEqual(int capacidade);
}
