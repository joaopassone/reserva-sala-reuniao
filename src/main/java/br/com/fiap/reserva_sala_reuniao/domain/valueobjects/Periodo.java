package br.com.fiap.reserva_sala_reuniao.domain.valueobjects;

import java.time.LocalDateTime;

public final class Periodo {

    private final LocalDateTime inicio;
    private final LocalDateTime fim;

    public Periodo(LocalDateTime inicio, LocalDateTime fim) {

        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("As datas de início e fim não podem ser nulas.");
        }
        if (inicio.isAfter(fim) || inicio.isEqual(fim)) {
            throw new IllegalArgumentException("A data de início deve ser anterior à data de fim.");
        }

        this.inicio = inicio;
        this.fim = fim;
    }

    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFim() { return fim; }

    // Verifica se este período conflita com outro período
    public boolean conflitaCom(Periodo outro) {
        return this.inicio.isBefore(outro.fim) && outro.inicio.isBefore(this.fim);
    }
}
