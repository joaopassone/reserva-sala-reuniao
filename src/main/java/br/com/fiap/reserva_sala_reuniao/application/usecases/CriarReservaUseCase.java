package br.com.fiap.reserva_sala_reuniao.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Reserva;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Sala;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.ReservaRepository;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.SalaRepository;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.UsuarioRepository;
import br.com.fiap.reserva_sala_reuniao.domain.valueobjects.Periodo;

import java.time.LocalDateTime;

@Service
public class CriarReservaUseCase {

    private final ReservaRepository reservaRepository;
    private final SalaRepository salaRepository;
    private final UsuarioRepository usuarioRepository;

    public CriarReservaUseCase(ReservaRepository reservaRepository, 
                                 SalaRepository salaRepository, 
                                 UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.salaRepository = salaRepository;
        this.usuarioRepository = usuarioRepository;
    }


    @Transactional
    public Reserva executar(String salaId, String usuarioId, LocalDateTime inicio, LocalDateTime fim) {
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new IllegalArgumentException("Sala de reunião não encontrada."));

        Periodo periodo = new Periodo(inicio, fim);

        if (inicio.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Não é possível realizar um agendamento em datas ou horários passados.");
        }

        var conflitos = reservaRepository.buscarConflitos(salaId, periodo.getInicio(), periodo.getFim());
        
        if (!conflitos.isEmpty()) {
            throw new IllegalStateException("Conflito de agendamento: A sala já está reservada ou confirmada para este horário.");
        }

        Reserva novaReserva = new Reserva(sala, usuario, periodo.getInicio(), periodo.getFim());

        novaReserva.confirmar();

        return reservaRepository.save(novaReserva);
    }
}
