package br.com.fiap.reserva_sala_reuniao.infrastructure.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.reserva_sala_reuniao.application.usecases.AlterarUsuarioUseCase;
import br.com.fiap.reserva_sala_reuniao.application.usecases.CadastrarUsuarioUseCase;
import br.com.fiap.reserva_sala_reuniao.application.usecases.RemoverUsuarioUseCase;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.AlterarUsuarioRequestDTO;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.CriarUsuarioRequestDTO;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final AlterarUsuarioUseCase alterarUsuarioUseCase;
    private final RemoverUsuarioUseCase removerUsuarioUseCase;

    public UsuarioController(CadastrarUsuarioUseCase cadastrarUsuarioUseCase,
                             AlterarUsuarioUseCase alterarUsuarioUseCase,
                             RemoverUsuarioUseCase removerUsuarioUseCase) {
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.alterarUsuarioUseCase = alterarUsuarioUseCase;
        this.removerUsuarioUseCase = removerUsuarioUseCase;
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody CriarUsuarioRequestDTO dto) {
        Usuario novoUsuario = cadastrarUsuarioUseCase.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> alterar(
            @PathVariable("id") String usuarioId,
            @RequestBody AlterarUsuarioRequestDTO dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        Usuario usuarioAtualizado = alterarUsuarioUseCase.executar(usuarioLogado.getId(), usuarioId, dto);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable("id") String usuarioId,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        removerUsuarioUseCase.executar(usuarioLogado.getId(), usuarioId);
        return ResponseEntity.noContent().build();
    }
}
