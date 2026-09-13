package br.com.fiap.reserva_sala_reuniao.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Reserva;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.ReservaRepository;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.UsuarioRepository;
import br.com.fiap.reserva_sala_reuniao.domain.valueobjects.Periodo;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.AlterarReservaRequestDTO;

import java.time.LocalDateTime;

@Service
public class AlterarReservaUseCase {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;

    public AlterarReservaUseCase(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Reserva executar(String operadorId, String reservaId, AlterarReservaRequestDTO dto) {
        
        Usuario operador = usuarioRepository.findById(operadorId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário operador não encontrado."));

        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento de reserva não encontrado."));

        boolean ehDonoDaReserva = reserva.getUsuario().getId().equals(operadorId);
        boolean ehAdministrador = operador.ehAdmin();

        if (!ehDonoDaReserva && !ehAdministrador) {
            throw new IllegalStateException("Acesso negado: Você só pode modificar seus próprios agendamentos, a menos que seja um administrador.");
        }

        Periodo novoPeriodo = new Periodo(dto.inicio(), dto.fim());

        if (dto.inicio().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Não é possível alterar uma reserva para uma data ou horário passado.");
        }

        var conflitos = reservaRepository.buscarConflitos(reserva.getSala().getId(), novoPeriodo.getInicio(), novoPeriodo.getFim());
        
        boolean possuiConflitosReais = conflitos.stream()
                .anyMatch(c -> !c.getId().equals(reservaId));

        if (possuiConflitosReais) {
            throw new IllegalStateException("Conflito de agendamento: A sala já possui uma reserva confirmada neste novo horário.");
        }

        reserva.setDataInicio(novoPeriodo.getInicio());
        reserva.setDataFim(novoPeriodo.getFim());
        
        if (reserva.getStatus() == Reserva.StatusReserva.CANCELADA) {
            reserva.setStatus(Reserva.StatusReserva.SOLICITADA);
        }

        reserva.confirmar();

        return reservaRepository.save(reserva);
    }
}
