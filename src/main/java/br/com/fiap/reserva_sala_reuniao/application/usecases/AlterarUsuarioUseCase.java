package br.com.fiap.reserva_sala_reuniao.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.UsuarioRepository;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.AlterarUsuarioRequestDTO;

@Service
public class AlterarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public AlterarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario executar(String operadorId, String usuarioId, AlterarUsuarioRequestDTO dto) {

        Usuario operador = usuarioRepository.findById(operadorId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário operador não encontrado."));

        Usuario usuarioAlvo = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário alvo não encontrado para alteração."));

        if (!operadorId.equals(usuarioId) && !operador.ehAdmin()) {
            throw new IllegalStateException("Acesso negado: Você não tem permissão para alterar os dados de outro usuário.");
        }

        var usuarioComMesmoEmail = usuarioRepository.findByEmail(dto.email());
        if (usuarioComMesmoEmail.isPresent() && !usuarioComMesmoEmail.get().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("O e-mail informado já está em uso por outro usuário.");
        }

        Usuario usuarioAtualizado = new Usuario(usuarioAlvo.getId(), dto.nome(), dto.email(), usuarioAlvo.getSenha(), usuarioAlvo.getTipo());

        return usuarioRepository.save(usuarioAtualizado);
    }
}
