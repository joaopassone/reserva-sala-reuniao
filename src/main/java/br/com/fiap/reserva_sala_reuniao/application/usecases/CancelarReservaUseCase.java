package br.com.fiap.reserva_sala_reuniao.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Reserva;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.ReservaRepository;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.UsuarioRepository;

@Service
public class CancelarReservaUseCase {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;

    public CancelarReservaUseCase(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void executar(String operadorId, String reservaId) {
        
        Usuario operador = usuarioRepository.findById(operadorId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário operador não encontrado."));

        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento de reserva não encontrado."));

        boolean ehDonoDaReserva = reserva.getUsuario().getId().equals(operadorId);
        boolean ehAdministrador = operador.ehAdmin();

        if (!ehDonoDaReserva && !ehAdministrador) {
            throw new IllegalStateException("Acesso negado: Você só pode cancelar seus próprios agendamentos, a menos que seja um administrador.");
        }

        reserva.cancelar();

        reservaRepository.save(reserva);
    }
}
