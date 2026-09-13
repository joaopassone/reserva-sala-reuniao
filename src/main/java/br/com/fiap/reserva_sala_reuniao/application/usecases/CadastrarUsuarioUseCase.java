package br.com.fiap.reserva_sala_reuniao.application.usecases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.UsuarioRepository;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.CriarUsuarioRequestDTO;

@Service
public class CadastrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public CadastrarUsuarioUseCase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario executar(CriarUsuarioRequestDTO dto) {

        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("O e-mail informado já está cadastrado no sistema.");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.senha());

        Usuario novoUsuario = new Usuario(null, dto.nome(), dto.email(), senhaCriptografada, dto.tipo());

        return usuarioRepository.save(novoUsuario);
    }
}
