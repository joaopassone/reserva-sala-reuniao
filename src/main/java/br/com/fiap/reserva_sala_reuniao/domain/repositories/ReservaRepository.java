package br.com.fiap.reserva_sala_reuniao.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Reserva;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, String> {

       /**
        * Valida choque de horários direto no banco de dados.
        * Retorna registros se houver sobreposição de uma reserva CONFIRMADA para a mesma sala.
        */
       @Query("SELECT r FROM Reserva r WHERE r.sala.id = :salaId AND r.status = 'CONFIRMADA' " +
              "AND (:inicio < r.dataFim AND :fim > r.dataInicio)")
       List<Reserva> buscarConflitos(@Param("salaId") String salaId, 
                                   @Param("inicio") LocalDateTime inicio, 
                                   @Param("fim") LocalDateTime fim);

       /**
        * Busca todas as reservas de uma determinada sala que estejam ativas dentro de um intervalo de tempo.
        */
       @Query("SELECT r FROM Reserva r WHERE r.sala.id = :salaId AND r.status IN ('CONFIRMADA', 'SOLICITADA') " +
              "AND (r.dataInicio <= :fim AND r.dataFim >= :inicio) ORDER BY r.dataInicio ASC")
       List<Reserva> buscarOcupacaoPorSalaNoIntervalo(@Param("salaId") String salaId, 
                                                        @Param("inicio") LocalDateTime inicio, 
                                                        @Param("fim") LocalDateTime fim);

       /**
        * Busca as reservas de TODAS as salas que estejam ativas dentro de um intervalo de tempo.
        */
       @Query("SELECT r FROM Reserva r WHERE r.status IN ('CONFIRMADA', 'SOLICITADA') " +
              "AND (r.dataInicio <= :fim AND r.dataFim >= :inicio) ORDER BY r.sala.id ASC, r.dataInicio ASC")
       List<Reserva> buscarOcupacaoDeTodasNoIntervalo(@Param("inicio") LocalDateTime inicio, 
                                                        @Param("fim") LocalDateTime fim);

       List<Reserva> findBySalaId(String salaId);
}
