package br.com.fiap.reserva_sala_reuniao.application.usecases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.UsuarioRepository;
import br.com.fiap.reserva_sala_reuniao.infrastructure.security.TokenService;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.LoginResponseDTO;

@Service
public class AutenticarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AutenticarUsuarioUseCase(UsuarioRepository usuarioRepository, 
                                     TokenService tokenService, 
                                     PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional(readOnly = true)
    public LoginResponseDTO executar(String email, String senha) {
        
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas: e-mail ou senha incorretos."));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new IllegalArgumentException("Credenciais inválidas: e-mail ou senha incorretos.");
        }

        String token = tokenService.gerarToken(usuario);

        return new LoginResponseDTO(token, usuario.getEmail(), usuario.getTipo());
    }
}
