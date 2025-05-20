package com.example.auth_service.service;

import com.example.auth_service.model.Compte;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    /********* Info Mail ***********/

    @Value("${urlPublic}")
    private String urlPublic;

    @Value("${fromusermail}")
    private String fromusermail;

    @Value("${fromuser}")
    private String fromuser;

    /****************************/

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    TemplateEngine templateEngine;

    public void activateAccount(Compte compte) {

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        Context context = new Context();
        String url = urlPublic+"/active/"+ compte.getUuid();
        context.setVariable("url",url);
        //context.setVariable("typeAccount",compte.getMerchantType());
        context.setVariable("email",compte.getEmail());

        try {
            helper.setTo(compte.getEmail());
            helper.setSubject("Activez votre compte");
            String htmlContent = templateEngine.process("activation.html",context);
            helper.setText(htmlContent, true);
            helper.setFrom(fromusermail, fromuser);
            emailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
    }

    public void otpCode(Compte compte) {

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        Context context = new Context();
        context.setVariable("code",compte.getOtp());
        context.setVariable("nom",compte.getNom_fr());

        try {
            helper.setTo(compte.getEmail());
            helper.setSubject("Code d'authentification");
            String htmlContent = templateEngine.process("otp.html",context);
            helper.setText(htmlContent, true);
            helper.setFrom(fromusermail, fromuser);
            emailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendContactEmail(String name, String email, String messageContent) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("email", email);
        context.setVariable("message", messageContent);

        String emailContent = templateEngine.process("contact-email", context);

        try{
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setTo(fromusermail);
            helper.setSubject("📩 رسالة جديدة من صفحة الاتصال");
            helper.setText(emailContent, true);

            emailSender.send(message);
        }catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    }
}
