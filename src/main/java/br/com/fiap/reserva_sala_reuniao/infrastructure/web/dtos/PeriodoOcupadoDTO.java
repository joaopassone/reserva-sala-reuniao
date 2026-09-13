package br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos;

import java.time.LocalDateTime;

public record PeriodoOcupadoDTO(
    String reservaId,
    String usuarioNome,
    LocalDateTime inicio,
    LocalDateTime fim,
    String status
) {}
