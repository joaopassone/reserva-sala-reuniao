package br.com.fiap.reserva_sala_reuniao.domain.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;
import java.util.regex.Pattern;

import br.com.fiap.reserva_sala_reuniao.domain.enums.TipoUsuario;

@Entity
@Table(name = "tb_usuarios")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Usuario {

    @Transient
    private static final Pattern EMAIL_REGEX = Pattern.compile(
        "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$"
    );

    @Id
    private String id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoUsuario tipo;


    public Usuario(String id, String nome, String email, String senha, TipoUsuario tipo) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome é obrigatório.");
        if (senha == null || senha.length() < 8) throw new IllegalArgumentException("Senha deve ter ao menos 8 caracteres.");
        if (tipo == null) throw new IllegalArgumentException("O tipo de usuário deve ser definido.");

        validarEmail(email);

        this.id = id != null ? id : UUID.randomUUID().toString();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }

    private void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new IllegalArgumentException("O formato do e-mail informado é inválido.");
        }
    }
   
    public boolean ehAdmin() {
        return this.tipo == TipoUsuario.ADMIN;
    }

    public boolean podeAprovarReservasEspeciais() {
        return ehAdmin();
    }
    
    public void alterarNome(String novoNome) {
        if (novoNome == null || novoNome.isBlank()) throw new IllegalArgumentException("Nome inválido.");
        this.nome = novoNome;
    }
}
