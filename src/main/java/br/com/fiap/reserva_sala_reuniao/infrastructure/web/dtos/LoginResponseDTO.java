package br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos;

import br.com.fiap.reserva_sala_reuniao.domain.enums.TipoUsuario;

public record LoginResponseDTO(String token, String email, TipoUsuario tipoUsuario) {}
