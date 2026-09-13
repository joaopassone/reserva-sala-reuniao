package br.com.fiap.reserva_sala_reuniao.infrastructure.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.com.fiap.reserva_sala_reuniao.domain.events.ReservaStatusAlteradoEvent;
import br.com.fiap.reserva_sala_reuniao.infrastructure.services.EmailService;

import java.time.format.DateTimeFormatter;

@Component
public class ReservaEventListener {

    private final EmailService emailService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ReservaEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void ouvirMudancaStatus(ReservaStatusAlteradoEvent evento) {
        String assunto = "Atualização na sua Reserva: " + evento.salaNome();
        
        String html = String.format("""
            <h1>Olá, %s!</h1>
            <p>Houve uma atualização no status do agendamento da sua sala de reunião.</p>
            <ul>
                <li><strong>Sala:</strong> %s</li>
                <li><strong>Data/Hora:</strong> De %sh a %sh</li>
                <li><strong>Novo Status:</strong> <span style="color: #2563eb; font-weight: bold;">%s</span></li>
            </ul>
            <p>Se você não reconhece esta alteração, entre em contato com a administração.</p>
            """, 
            evento.usuarioNome(), 
            evento.salaNome(), 
            evento.dataInicio().format(FORMATTER),
            evento.dataFim().format(FORMATTER), 
            evento.novoStatus().name()
        );

        emailService.enviarEmail(evento.usuarioEmail(), assunto, html);
    }
}
