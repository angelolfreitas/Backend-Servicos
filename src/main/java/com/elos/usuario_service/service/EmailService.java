package com.elos.usuario_service.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${app.mail.sender}")
    private String remetente;

    @Async
    public void enviar(String para, String assunto, String corpoHtml) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, "UTF-8");
            helper.setFrom(remetente, "Elos");
            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(corpoHtml, true);
            mailSender.send(msg);
        } catch (Exception e) {
            log.error("Falha ao enviar e-mail", e);
        }
    }
}