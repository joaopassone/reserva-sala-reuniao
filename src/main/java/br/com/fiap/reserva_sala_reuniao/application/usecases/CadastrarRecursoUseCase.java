package br.com.fiap.reserva_sala_reuniao.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Recurso;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.RecursoRepository;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CadastrarRecursoUseCase {

    private final RecursoRepository recursoRepository;
    private final UsuarioRepository usuarioRepository;

    public CadastrarRecursoUseCase(RecursoRepository recursoRepository, UsuarioRepository usuarioRepository) {
        this.recursoRepository = recursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Executa o cadastro em lote de uma lista de nomes de recursos.
     * @param usuarioId ID do usuário que está tentando realizar a ação.
     * @param nomesRecursos Lista contendo os nomes dos recursos (Ex: ["Projetor", "Ar Condicionado"]).
     */
    @Transactional
    public List<Recurso> executar(String usuarioId, List<String> nomesRecursos) {
  
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (!usuario.ehAdmin()) {
            throw new IllegalStateException("Acesso negado: Apenas administradores podem cadastrar recursos.");
        }

        if (nomesRecursos == null || nomesRecursos.isEmpty()) {
            throw new IllegalArgumentException("A lista de recursos para cadastro não pode estar vazia.");
        }

        List<Recurso> recursosSalvos = new ArrayList<>();

        for (String nome : nomesRecursos) {
            if (nome == null || nome.isBlank()) {
                throw new IllegalArgumentException("O nome do recurso não pode ser vazio ou nulo.");
            }

            String nomeFormatado = nome.trim();
            var recursoExistente = recursoRepository.findByNomeIgnoreCase(nomeFormatado);
            
            if (recursoExistente.isPresent()) {
                recursosSalvos.add(recursoExistente.get());
            } else {
                Recurso novoRecurso = new Recurso(nomeFormatado);
                recursosSalvos.add(novoRecurso);
            }
        }

        return recursoRepository.saveAll(recursosSalvos);
    }
}
