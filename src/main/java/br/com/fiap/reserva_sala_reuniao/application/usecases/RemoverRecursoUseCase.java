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
public class RemoverRecursoUseCase {

    private final RecursoRepository recursoRepository;
    private final UsuarioRepository usuarioRepository;

    public RemoverRecursoUseCase(RecursoRepository recursoRepository, UsuarioRepository usuarioRepository) {
        this.recursoRepository = recursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Executa a remoção em lote de uma lista de nomes de recursos.
     * @param usuarioId ID do usuário que está tentando realizar a ação.
     * @param nomesRecursos Lista contendo os nomes dos recursos a serem removidos (Ex: ["Projetor", "Ar Condicionado"]).
     */
    @Transactional
    public List<Recurso> executar(String usuarioId, List<String> nomesRecursos) {
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (!usuario.ehAdmin()) {
            throw new IllegalStateException("Acesso negado: Apenas administradores podem remover recursos.");
        }

        if (nomesRecursos == null || nomesRecursos.isEmpty()) {
            throw new IllegalArgumentException("A lista de recursos para remoção não pode estar vazia.");
        }

        List<Recurso> recursosParaDeletar = new ArrayList<>();

        for (String nome : nomesRecursos) {
            if (nome == null || nome.isBlank()) {
                continue;
            }

            String nomeFormatado = nome.trim();

            var recursoExistente = recursoRepository.findByNomeIgnoreCase(nomeFormatado);

            if (recursoExistente.isPresent()) {
                recursosParaDeletar.add(recursoExistente.get());
            }
        }

        if (!recursosParaDeletar.isEmpty()) {
            recursoRepository.deleteAll(recursosParaDeletar);
        }

        return recursosParaDeletar;
    }
}
