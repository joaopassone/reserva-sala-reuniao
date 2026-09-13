package br.com.fiap.reserva_sala_reuniao.domain.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "tb_salas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Sala {
    
    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private int capacidade;

    @Column(nullable = false)
    private String localizacao;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tb_salas_recursos",
        joinColumns = @JoinColumn(name = "sala_id"),
        inverseJoinColumns = @JoinColumn(name = "recurso_id")
    )
    
    private Set<Recurso> recursos = new HashSet<>();

    public Sala(String nome, int capacidade, String localizacao) {
        validarCampos(nome, capacidade, localizacao);

        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.capacidade = capacidade;
        this.localizacao = localizacao;
    }

    public void atualizarCaracteristicas(String novoNome, int novaCapacidade, String novaLocalizacao) {
        validarCampos(novoNome, novaCapacidade, novaLocalizacao);
        
        this.nome = novoNome.trim();
        this.capacidade = novaCapacidade;
        this.localizacao = novaLocalizacao.trim();
    }

    private void validarCampos(String nome, int capacidade, String localizacao) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da sala é obrigatório e não pode ser vazio.");
        }
        if (localizacao == null || localizacao.isBlank()) {
            throw new IllegalArgumentException("A localização da sala é obrigatória.");
        }
        if (capacidade <= 0) {
            throw new IllegalArgumentException("A capacidade da sala deve ser obrigatoriamente maior que zero.");
        }
    }

}
