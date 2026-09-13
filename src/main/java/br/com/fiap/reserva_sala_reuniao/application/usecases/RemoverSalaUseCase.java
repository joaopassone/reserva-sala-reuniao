package br.com.fiap.reserva_sala_reuniao.application.usecases;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Reserva;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Sala;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.ReservaRepository;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.SalaRepository;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.UsuarioRepository;

@Service
public class RemoverSalaUseCase {

    private final SalaRepository salaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;

    public RemoverSalaUseCase(SalaRepository salaRepository, UsuarioRepository usuarioRepository, ReservaRepository reservaRepository) {
        this.salaRepository = salaRepository;
        this.usuarioRepository = usuarioRepository;
        this.reservaRepository = reservaRepository;
        
    }

    /**
     * Executa a remoção física de uma sala de reunião do sistema.
     * @param usuarioId ID do usuário que está tentando realizar a ação.
     * @param salaId ID da sala a ser removida.
     */
    @Transactional
    public void executar(String usuarioId, String salaId) {
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (!usuario.ehAdmin()) {
            throw new IllegalStateException("Acesso negado: Apenas administradores podem remover salas.");
        }

        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new IllegalArgumentException("Sala de reunião não encontrada para remoção."));

        List<Reserva> reservasDaSala = reservaRepository.findBySalaId(salaId);
        
        if (!reservasDaSala.isEmpty()) {
            reservaRepository.deleteAll(reservasDaSala);
        }

        sala.getRecursos().clear();

        salaRepository.save(sala);
        salaRepository.deleteById(sala.getId());
    }
}
