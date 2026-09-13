package br.com.fiap.reserva_sala_reuniao.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Reserva;
import br.com.fiap.reserva_sala_reuniao.domain.entities.Sala;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.ReservaRepository;
import br.com.fiap.reserva_sala_reuniao.domain.repositories.SalaRepository;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.OcupacaoSalaResponseDTO;
import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.PeriodoOcupadoDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConsultarOcupacaoSalasUseCase {

    private final ReservaRepository reservaRepository;
    private final SalaRepository salaRepository;

    public ConsultarOcupacaoSalasUseCase(ReservaRepository reservaRepository, SalaRepository salaRepository) {
        this.reservaRepository = reservaRepository;
        this.salaRepository = salaRepository;
    }

    /**
     * Consulta a ocupação das salas para um dia específico.
     * @param dataApuracao O dia escolhido para verificar o cronograma.
     * @param salaId Opcional (Pode ser nulo). Se informado, filtra apenas a sala solicitada.
     */
    @Transactional(readOnly = true)
    public List<OcupacaoSalaResponseDTO> executar(LocalDate dataApuracao, String salaId) {
        
        if (dataApuracao == null) {
            throw new IllegalArgumentException("A data para apuração da ocupação é obrigatória.");
        }

        LocalDateTime inicioDoDia = dataApuracao.atStartOfDay();
        LocalDateTime fimDoDia = dataApuracao.atTime(LocalTime.MAX);

        List<OcupacaoSalaResponseDTO> resposta = new ArrayList<>();

        // CASO 1: Foi passado um salaId específico
        if (salaId != null && !salaId.isBlank()) {
            Sala sala = salaRepository.findById(salaId)
                    .orElseThrow(() -> new IllegalArgumentException("Sala de reunião não encontrada para consulta."));

            List<Reserva> reservasDaSala = reservaRepository.buscarOcupacaoPorSalaNoIntervalo(salaId, inicioDoDia, fimDoDia);
            resposta.add(mapearParaDTO(sala, reservasDaSala));
            
        } else {
            // CASO 2: Não foi passado salaId (Trazer a ocupação de todas as salas cadastradas)
            List<Sala> todasAsSalas = salaRepository.findAll();
            List<Reserva> todasAsReservasDoDia = reservaRepository.buscarOcupacaoDeTodasNoIntervalo(inicioDoDia, fimDoDia);

            Map<String, List<Reserva>> reservasAgrupadasPorSala = todasAsReservasDoDia.stream()
                    .collect(Collectors.groupingBy(reserva -> reserva.getSala().getId()));

            for (Sala sala : todasAsSalas) {
                List<Reserva> reservasDaSala = reservasAgrupadasPorSala.getOrDefault(sala.getId(), new ArrayList<>());
                resposta.add(mapearParaDTO(sala, reservasDaSala));
            }
        }

        return resposta;
    }

    private OcupacaoSalaResponseDTO mapearParaDTO(Sala sala, List<Reserva> reservas) {
        List<PeriodoOcupadoDTO> horarios = reservas.stream()
                .map(r -> new PeriodoOcupadoDTO(
                        r.getId(),
                        r.getUsuario().getNome(),
                        r.getDataInicio(),
                        r.getDataFim(),
                        r.getStatus().name()
                ))
                .collect(Collectors.toList());

        return new OcupacaoSalaResponseDTO(sala.getId(), sala.getNome(), sala.getLocalizacao(), horarios);
    }
}
