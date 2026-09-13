package br.com.fiap.reserva_sala_reuniao.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Recurso;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Sala;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.RecursoRepository;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.SalaRepository;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.UsuarioRepository;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.SalaRequestDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AlterarSalaUseCase {

    private final SalaRepository salaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecursoRepository recursoRepository;

    public AlterarSalaUseCase(SalaRepository salaRepository, 
                              UsuarioRepository usuarioRepository, 
                              RecursoRepository recursoRepository) {
        this.salaRepository = salaRepository;
        this.usuarioRepository = usuarioRepository;
        this.recursoRepository = recursoRepository;
    }

    /**
     * Executa a alteração das características de uma sala de reunião existente.
     * @param usuarioId ID do usuário que está tentando realizar a ação.
     * @param salaId ID da sala que sofrerá as modificações.
     * @param dto Novos dados para atualização da sala.
     */
    @Transactional
    public Sala executar(String usuarioId, String salaId, SalaRequestDTO dto) {
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (!usuario.ehAdmin()) {
            throw new IllegalStateException("Acesso negado: Apenas administradores podem alterar salas.");
        }

        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new IllegalArgumentException("Sala de reunião não encontrada para alteração."));

        sala.atualizarCaracteristicas(dto.nome(), dto.capacidade(), dto.localizacao());

        if (dto.recursosIds() != null) {
            List<Recurso> novosRecursos = recursoRepository.findAllById(dto.recursosIds());
            
            if (novosRecursos.size() != dto.recursosIds().size()) {
                throw new IllegalArgumentException("Um ou mais IDs de recursos informados não existem no catálogo.");
            }

            Set<Recurso> conjuntoRecursos = new HashSet<>(novosRecursos);
            sala.setRecursos(conjuntoRecursos);
        } else {
            sala.getRecursos().clear();
        }

        return salaRepository.save(sala);
    }
}
