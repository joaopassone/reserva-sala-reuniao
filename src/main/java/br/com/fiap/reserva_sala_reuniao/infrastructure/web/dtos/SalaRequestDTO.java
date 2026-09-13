package br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos;

import java.util.List;

public record SalaRequestDTO(
    String nome,
    int capacidade,
    String localizacao,
    List<String> recursosIds
) {}
