package br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos;

import java.util.List;

public record OcupacaoSalaResponseDTO(
    String salaId,
    String nomeSala,
    String localizacao,
    List<PeriodoOcupadoDTO> horariosOcupados
) {}
