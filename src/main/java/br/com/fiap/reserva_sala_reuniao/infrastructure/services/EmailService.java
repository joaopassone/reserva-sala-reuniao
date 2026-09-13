package br.com.fiap.reserva_sala_reuniao.infrastructure.services;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;
    private final String remetente;

    public EmailService(@Value("${resend.api.key}") String apiKey,
                        @Value("${resend.email.remetente}") String remetente) {
        this.resend = new Resend(apiKey);
        this.remetente = remetente;
    }

    public void enviarEmail(String para, String assunto, String conteudoHtml) {
        try {
            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(this.remetente)
                    .to(para)
                    .subject(assunto)
                    .html(conteudoHtml)
                    .build();

            resend.emails().send(params);
        } catch (ResendException e) {
            System.err.println("Falha ao enviar e-mail via Resend: " + e.getMessage());
        }
    }
}
