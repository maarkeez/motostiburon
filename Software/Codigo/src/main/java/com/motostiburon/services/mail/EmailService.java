package com.motostiburon.services.mail;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

@Component
public class EmailService {

	private Properties getSMTPProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");
		return properties;
	}

	private Authenticator getAuthenticator(String user, String credentials) {

		Authenticator authenticator = new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, credentials);
			}
		};
		return authenticator;
	}

	private Message getMessage(String de, String para, String cc, String asunto, String mensaje, Session session) throws MessagingException {
		Message message = new MimeMessage(session);

		message.setFrom(new InternetAddress(de));

		Address[] direccionDeRespuesta = { new InternetAddress(de) };

		message.setReplyTo(direccionDeRespuesta);

		// Set To: header field of the header.
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(para));
		message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
		

		// Set Subject: header field
		message.setSubject(asunto);

		// Send the actual HTML message, as big as you like
		message.setContent("<div>" + mensaje + "</div>", "text/html");
		return message;
	}

	private String messageToString(Message message) throws MessagingException, IOException {
		String toReturn = "De: ";
		toReturn += Arrays.stream(message.getAllRecipients()).map(Address::toString).collect(Collectors.joining(", "));
		toReturn += "\n";
		toReturn += "Para: ";
		toReturn += Arrays.stream(message.getReplyTo()).map(Address::toString).collect(Collectors.joining(", "));
		toReturn += "\n";
		toReturn += "Asunto: " + message.getSubject() + "\n";
		toReturn += "Mensaje: " + (String) message.getContent() + "\n";
		return toReturn;
	}

	public void sendMail(String de, String asunto, String mensaje) {
		// Recipient's email ID needs to be mentioned.
		String para = "noreply.motostiburon@gmail.com";
		String password = "1234567890qwert";
		String cc = de;

		Session session = Session.getInstance(getSMTPProperties(), getAuthenticator(para, password));

		try {
			Message message = getMessage(de, para,cc, asunto, mensaje, session);

			Transport.send(message);
			System.out.println("Email enviado correctamente");

			System.out.println(messageToString(message));

		} catch (MessagingException mex) {
			System.err.println("Se ha producido un error enviando el email");
			mex.printStackTrace();
		} catch (IOException e) {
			System.err.println("No se ha podido recuperar el mensaje para imprimirlo");
			e.printStackTrace();
		}
	}
}