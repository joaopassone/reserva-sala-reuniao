package br.com.fiap.reserva_sala_reuniao.infrastructure.security;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import br.com.fiap.reserva_sala_reuniao.domain.entities.Usuario;

import java.util.Date;

import javax.crypto.SecretKey;

@Service
public class TokenService {

    private final SecretKey key = Jwts.SIG.HS256.key().build();
    private final long expirationTime = 86400000; // 1 dia em milissegundos

    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("role", usuario.getTipo().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    public String validarToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

}
