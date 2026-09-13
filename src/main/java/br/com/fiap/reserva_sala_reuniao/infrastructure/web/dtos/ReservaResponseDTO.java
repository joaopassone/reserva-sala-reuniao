package br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos;

import java.time.LocalDateTime;

public record ReservaResponseDTO(
    String mensagem,
    String reservaId,
    String salaId,
    String salaNome,
    String usuarioNome,
    LocalDateTime inicio,
    LocalDateTime fim,
    String status
) {}

