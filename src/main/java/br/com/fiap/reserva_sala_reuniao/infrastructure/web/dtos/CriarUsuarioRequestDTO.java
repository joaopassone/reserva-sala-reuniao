package br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos;

import br.com.fiap.reserva_sala_reuniao.domain.enums.TipoUsuario;

public record CriarUsuarioRequestDTO(
    String nome,
    String email,
    String senha,
    TipoUsuario tipo
) {}
