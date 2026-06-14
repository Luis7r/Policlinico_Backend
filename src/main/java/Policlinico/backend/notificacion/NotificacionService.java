package Policlinico.backend.notificacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificacionService.class);

    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final String remitente;

    public NotificacionService(
            ObjectProvider<JavaMailSender> mailSenderProvider,
            @Value("${app.mail.from:no-reply@policlinico.local}") String remitente) {

        this.mailSenderProvider = mailSenderProvider;
        this.remitente = remitente;
    }

    public boolean enviar(String destinatario, String asunto, String contenido) {

        if (destinatario == null || destinatario.isBlank()) {
            LOGGER.warn("No se envio notificacion: el destinatario esta vacio");
            return false;
        }

        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();

        if (mailSender == null) {
            LOGGER.warn(
                "No se envio correo a {} porque no hay JavaMailSender configurado",
                destinatario
            );
            return false;
        }

        try {

            SimpleMailMessage mensaje = new SimpleMailMessage();

            mensaje.setFrom("Policlinico Chorrillos Salud <" + remitente + ">");
            mensaje.setTo(destinatario);
            mensaje.setSubject(asunto);
            mensaje.setText(contenido);

            mailSender.send(mensaje);

            return true;

        } catch (MailException ex) {

            LOGGER.warn(
                "No se pudo enviar correo a {}: {}",
                destinatario,
                ex.getMessage()
            );

            return false;
        }
    }

    @Async
    public void enviarAsync(
            String destinatario,
            String asunto,
            String contenido) {

        enviar(destinatario, asunto, contenido);
    }
}