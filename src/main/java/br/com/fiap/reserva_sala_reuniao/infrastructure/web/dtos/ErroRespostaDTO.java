package br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record ErroRespostaDTO(
    LocalDateTime timestamp,
    int status,
    String erro,
    String mensagem,
    String caminho
) {}
