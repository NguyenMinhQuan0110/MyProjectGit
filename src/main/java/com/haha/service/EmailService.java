package com.haha.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {
	public static void sendEmail(String recipientEmail, String subject, String body) {
        // Cấu hình các thuộc tính của SMTP server
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP của Gmail
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Tạo phiên email với xác thực
        String myEmail = "minhquan11003@gmail.com"; // Email gửi đi
        String password = "kmkq wjlw bgil bsep"; // Mật khẩu ứng dụng của Gmail (chứ không phải mật khẩu Google)

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail, password);
            }
        });

        try {
            // Tạo nội dung email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            // Gửi email
            Transport.send(message);
            System.out.println("Gửi email thành công đến " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
