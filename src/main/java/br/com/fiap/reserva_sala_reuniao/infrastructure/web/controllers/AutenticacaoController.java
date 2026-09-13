package br.com.fiap.reserva_sala_reuniao.infrastructure.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.reserva_sala_reuniao.application.usecases.AutenticarUsuarioUseCase;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.LoginRequestDTO;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.LoginResponseDTO;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    public AutenticacaoController(AutenticarUsuarioUseCase autenticarUsuarioUseCase) {
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        LoginResponseDTO resposta = autenticarUsuarioUseCase.executar(dto.email(), dto.senha());

        return ResponseEntity.ok(resposta);
    }
}
