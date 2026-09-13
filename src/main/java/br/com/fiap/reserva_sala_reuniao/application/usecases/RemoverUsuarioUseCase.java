package br.com.fiap.reserva_sala_reuniao.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.UsuarioRepository;

@Service
public class RemoverUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public RemoverUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void executar(String operadorId, String usuarioId) {

        Usuario operador = usuarioRepository.findById(operadorId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário operador não encontrado."));

        Usuario usuarioAlvo = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário a ser removido não encontrado."));

        if (!operadorId.equals(usuarioId) && !operador.ehAdmin()) {
            throw new IllegalStateException("Acesso negado: Você não pode excluir a conta de outro usuário.");
        }

        usuarioRepository.delete(usuarioAlvo);
    }
}
