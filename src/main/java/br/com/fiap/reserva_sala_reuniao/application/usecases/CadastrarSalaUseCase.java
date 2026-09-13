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
public class CadastrarSalaUseCase {

    private final SalaRepository salaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecursoRepository recursoRepository;

    public CadastrarSalaUseCase(SalaRepository salaRepository, 
                                UsuarioRepository usuarioRepository, 
                                RecursoRepository recursoRepository) {
        this.salaRepository = salaRepository;
        this.usuarioRepository = usuarioRepository;
        this.recursoRepository = recursoRepository;
    }

    /**
     * Executa o cadastro de uma nova sala de reunião associando recursos existentes.
     * @param usuarioId ID do usuário que está tentando realizar a ação.
     * @param dto Dados da sala vindos da requisição.
     */
    @Transactional
    public Sala executar(String usuarioId, SalaRequestDTO dto) {
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário operador não encontrado."));

        if (!usuario.ehAdmin()) {
            throw new IllegalStateException("Acesso negado: Apenas administradores podem cadastrar salas.");
        }

        Sala novaSala = new Sala(dto.nome(), dto.capacidade(), dto.localizacao());

        if (dto.recursosIds() != null && !dto.recursosIds().isEmpty()) {
            List<Recurso> recursosEncontrados = recursoRepository.findAllById(dto.recursosIds());
            
            if (recursosEncontrados.size() != dto.recursosIds().size()) {
                throw new IllegalArgumentException("Um ou mais IDs de recursos informados não existem no catálogo.");
            }

            Set<Recurso> conjuntoRecursos = new HashSet<>(recursosEncontrados);
            novaSala.setRecursos(conjuntoRecursos);
        }

        return salaRepository.save(novaSala);
    }
}
