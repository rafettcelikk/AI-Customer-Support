package com.rafetcelik.aicustomersupport.email;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.rafetcelik.aicustomersupport.model.Customer;
import com.rafetcelik.aicustomersupport.model.Ticket;
import com.rafetcelik.aicustomersupport.service.customer.ICustomerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService {
	private final EmailService emailService;
	
	private final ICustomerService customerService;
	
	public void sendTicketNotificationEmail(Ticket ticket) {
		Customer customer = customerService.getCustomerByEmail(ticket.getConversation().getCustomer().getEmailAddress());
		String customerName = customer.getFullName();
		String customerEmail = customer.getEmailAddress();
		String customerPhone = customer.getPhoneNumber();
		String senderName = "Yapay Zeka Destek Ekibi";
		String subject = "Yeni Destek Talebi";
		String ticketDetails = ticket.getConversation().getConversationSummary();
		String ticketTitle = ticket.getConversation().getConversationTitle();
		String referenceNumber = ticket.getReferenceNumber();
		String htmlBody = null;
		try {
			htmlBody = loadEmailTemplate(customerName, customerEmail, customerPhone, ticketDetails, ticketTitle, referenceNumber);
		} catch (IOException e) {
			log.error("E-posta şablonu yüklenirken hata oluştu: {}", e.getMessage());
		}
		try {
			emailService.sendNotificationEmail(customerEmail, subject, senderName, htmlBody);
		} catch (MessagingException | UnsupportedEncodingException e) {
			log.error("E-posta gönderilirken hata oluştu: {}", e.getMessage());
		}
	}
	
	public String loadEmailTemplate(String customerName, String customerEmail, String customerPhone, String ticketDetails, String ticketTile, String referenceNumber) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/ticket-notification-template.html");
        String template = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        template = template.replace("{{customerName}}", customerName);
        template = template.replace("{{customerEmail}}", customerEmail);
        template = template.replace("{{customerPhone}}", customerPhone);
        template = template.replace("{{ticketTile}}", ticketTile);
        template = template.replace("{{ticketDetails}}", ticketDetails);
        template = template.replace("{{ticketReferenceNumber}}", referenceNumber);
        return template;
    }
	
	/* private static String getHtmlBody(Ticket ticket, String userName, String senderName) {
	    String ticketDetails = ticket.getConversation().getConversationSummary();
	    String ticketTitle = ticket.getConversation().getConversationTitle();
	    String referenceNumber = ticket.getReferenceNumber();

	    // Java Text Blocks (""") ile HTML'i direkt olarak yazıyoruz. 
	    // %s kısımları, en alttaki .formatted() metodundan gelen değişkenlerle sırasıyla dolacak.
	    return """
	    <!DOCTYPE html>
	    <html lang="tr">
	    <head>
	        <meta charset="UTF-8">
	        <style>
	            body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7f6; margin: 0; padding: 20px; color: #333333; }
	            .container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); overflow: hidden; }
	            .header { background-color: #2563eb; color: #ffffff; padding: 20px; text-align: center; }
	            .header h1 { margin: 0; font-size: 24px; font-weight: 600; letter-spacing: 0.5px; }
	            .content { padding: 30px; line-height: 1.6; }
	            .greeting { font-size: 18px; font-weight: 600; margin-bottom: 20px; color: #1e293b; }
	            .info-box { background-color: #f8fafc; border-left: 4px solid #2563eb; padding: 20px; margin: 25px 0; border-radius: 0 4px 4px 0; }
	            .info-row { margin-bottom: 15px; }
	            .info-row:last-child { margin-bottom: 0; }
	            .label { font-weight: 600; color: #64748b; font-size: 12px; text-transform: uppercase; letter-spacing: 0.5px; }
	            .value { font-size: 15px; color: #0f172a; margin-top: 4px; }
	            .ref-badge { display: inline-block; background-color: #e2e8f0; padding: 4px 12px; border-radius: 20px; font-family: monospace; font-weight: bold; color: #475569; font-size: 14px; }
	            .footer { background-color: #f8fafc; padding: 20px; text-align: center; font-size: 13px; color: #64748b; border-top: 1px solid #e2e8f0; }
	        </style>
	    </head>
	    <body>
	        <div class="container">
	            <div class="header">
	                <h1>Destek Talebiniz Alındı</h1>
	            </div>
	            <div class="content">
	                <div class="greeting">Merhaba %s,</div>
	                <p>Bizimle iletişime geçtiğiniz için teşekkür ederiz. Destek talebiniz sistemimize başarıyla kaydedilmiştir. Destek uzmanlarımız konuyu inceleyip en kısa sürede sizinle iletişime geçecektir.</p>
	                
	                <div class="info-box">
	                    <div class="info-row">
	                        <div class="label">Referans Numarası</div>
	                        <div class="value"><span class="ref-badge">#%s</span></div>
	                    </div>
	                    <div class="info-row">
	                        <div class="label">Konu Başlığı</div>
	                        <div class="value"><strong>%s</strong></div>
	                    </div>
	                    <div class="info-row">
	                        <div class="label">Talep Detayları</div>
	                        <div class="value">%s</div>
	                    </div>
	                </div>
	                
	                <p>Sürecinizle ilgili tüm güncellemeleri bu e-posta adresi üzerinden sizinle paylaşıyor olacağız.</p>
	            </div>
	            <div class="footer">
	                <p>Bu otomatik bir mesajdır, lütfen bu e-postayı yanıtlamayınız.</p>
	                <p>&copy; 2026 %s</p>
	            </div>
	        </div>
	    </body>
	    </html>
	    """.formatted(userName, referenceNumber, ticketTitle, ticketDetails, senderName);
	}*/
}
