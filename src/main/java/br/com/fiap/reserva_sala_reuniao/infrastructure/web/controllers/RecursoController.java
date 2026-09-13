package br.com.fiap.reserva_sala_reuniao.infrastructure.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.reserva_sala_reuniao.application.usecases.CadastrarRecursoUseCase;
import br.com.fiap.reserva_sala_reuniao.application.usecases.RemoverRecursoUseCase;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Recurso;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;

import java.util.List;

@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

    private final CadastrarRecursoUseCase cadastrarRecursoUseCase;
    private final RemoverRecursoUseCase removerRecursoUseCase;

    public RecursoController(CadastrarRecursoUseCase cadastrarRecursoUseCase, RemoverRecursoUseCase removerRecursoUseCase) {
        this.cadastrarRecursoUseCase = cadastrarRecursoUseCase;
        this.removerRecursoUseCase = removerRecursoUseCase;
    }


    @PostMapping
    public ResponseEntity<List<Recurso>> cadastrar(
            @RequestBody List<String> nomesRecursos,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        List<Recurso> resultado = cadastrarRecursoUseCase.executar(usuarioLogado.getId(), nomesRecursos);

        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @DeleteMapping
    public ResponseEntity<List<Recurso>> remover(
            @RequestBody List<String> nomesRecursos,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        List<Recurso> removidos = removerRecursoUseCase.executar(usuarioLogado.getId(), nomesRecursos); 
                
        return ResponseEntity.ok(removidos);
    }
}
