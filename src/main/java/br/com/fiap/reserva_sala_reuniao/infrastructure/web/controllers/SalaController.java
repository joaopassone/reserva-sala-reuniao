package br.com.fiap.reserva_sala_reuniao.infrastructure.web.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.reserva_sala_reuniao.application.usecases.AlterarSalaUseCase;
import br.com.fiap.reserva_sala_reuniao.application.usecases.CadastrarSalaUseCase;
import br.com.fiap.reserva_sala_reuniao.application.usecases.ConsultarOcupacaoSalasUseCase;
import br.com.fiap.reserva_sala_reuniao.application.usecases.RemoverSalaUseCase;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Sala;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.OcupacaoSalaResponseDTO;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.SalaRequestDTO;

@RestController
@RequestMapping("/api/salas")
public class SalaController {

    private final CadastrarSalaUseCase cadastrarSalaUseCase;
    private final AlterarSalaUseCase alterarSalaUseCase;
    private final RemoverSalaUseCase removerSalaUseCase;
    private final ConsultarOcupacaoSalasUseCase consultarOcupacaoSalasUseCase;

    public SalaController(CadastrarSalaUseCase cadastrarSalaUseCase,
                          AlterarSalaUseCase alterarSalaUseCase,
                          RemoverSalaUseCase removerSalaUseCase,
                          ConsultarOcupacaoSalasUseCase consultarOcupacaoSalasUseCase) {
        this.cadastrarSalaUseCase = cadastrarSalaUseCase;
        this.alterarSalaUseCase = alterarSalaUseCase;
        this.removerSalaUseCase = removerSalaUseCase;
        this.consultarOcupacaoSalasUseCase = consultarOcupacaoSalasUseCase;
    }

    @GetMapping("/ocupacao")
    public ResponseEntity<List<OcupacaoSalaResponseDTO>> consultarOcupacao(
            @RequestParam("data") String dataString,
            @RequestParam(required = false) String salaId) {
        
        LocalDate dataApuracao = LocalDate.parse(dataString);
        List<OcupacaoSalaResponseDTO> relatorio = consultarOcupacaoSalasUseCase.executar(dataApuracao, salaId);
        
        return ResponseEntity.ok(relatorio);
    }

    @PostMapping
    public ResponseEntity<Sala> cadastrar(
            @RequestBody SalaRequestDTO dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        Sala novaSala = cadastrarSalaUseCase.executar(usuarioLogado.getId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaSala);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sala> alterar(
            @PathVariable("id") String salaId,
            @RequestBody SalaRequestDTO dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        Sala salaAtualizada = alterarSalaUseCase.executar(usuarioLogado.getId(), salaId, dto);
        return ResponseEntity.ok(salaAtualizada);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable("id") String salaId,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        removerSalaUseCase.executar(usuarioLogado.getId(), salaId);

        return ResponseEntity.noContent().build();
    }
}
