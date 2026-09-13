package br.com.fiap.reserva_sala_reuniao.infrastructure.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.reserva_sala_reuniao.application.usecases.AlterarReservaUseCase;
import br.com.fiap.reserva_sala_reuniao.application.usecases.CancelarReservaUseCase;
import br.com.fiap.reserva_sala_reuniao.application.usecases.CriarReservaUseCase;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Reserva;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.AlterarReservaRequestDTO;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.ReservaRequestDTO;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.ReservaResponseDTO;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final CriarReservaUseCase criarReservaUseCase;
    private final AlterarReservaUseCase alterarReservaUseCase;
    private final CancelarReservaUseCase cancelarReservaUseCase;

    public ReservaController(CriarReservaUseCase criarReservaUseCase,
                             AlterarReservaUseCase alterarReservaUseCase,
                             CancelarReservaUseCase cancelarReservaUseCase) {
        this.criarReservaUseCase = criarReservaUseCase;
        this.alterarReservaUseCase = alterarReservaUseCase;
        this.cancelarReservaUseCase = cancelarReservaUseCase;
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> criar(
            @RequestBody ReservaRequestDTO dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        Reserva reservaSalva = criarReservaUseCase.executar(
                dto.salaId(), 
                usuarioLogado.getId(), 
                dto.inicio(), 
                dto.fim()
        );

        ReservaResponseDTO resposta = new ReservaResponseDTO(
                "Reserva efetuada e confirmada com sucesso!",
                reservaSalva.getId(),
                reservaSalva.getSala().getId(),
                reservaSalva.getSala().getNome(),
                reservaSalva.getUsuario().getNome(),
                reservaSalva.getDataInicio(),
                reservaSalva.getDataFim(),
                reservaSalva.getStatus().name()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PutMapping("/{id}/alterar")
    public ResponseEntity<Reserva> alterar(
            @PathVariable("id") String reservaId,
            @RequestBody AlterarReservaRequestDTO dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        Reserva reservaAtualizada = alterarReservaUseCase.executar(usuarioLogado.getId(), reservaId, dto);
        return ResponseEntity.ok(reservaAtualizada);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelar(
            @PathVariable("id") String reservaId,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        cancelarReservaUseCase.executar(usuarioLogado.getId(), reservaId);
        return ResponseEntity.ok("Reserva cancelada com sucesso. A sala está livre para novos agendamentos.");
    }
}
