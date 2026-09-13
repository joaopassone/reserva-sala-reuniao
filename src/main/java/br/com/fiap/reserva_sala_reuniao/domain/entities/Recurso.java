package br.com.fiap.reserva_sala_reuniao.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "tb_recursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Recurso {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Column(nullable = false, unique = true)
    private String nome;

    public Recurso(String nome) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
    }
}
