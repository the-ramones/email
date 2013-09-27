package email;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 * JavaMail vs Apache Commons Mail evaluation
 *
 * @author paul
 */
public class EmailSender {

    static String username = "areports@mail.ru";
    static String password = "reports2010";
    /*
     * Outcomming..
     */
    static String smtpHostName = "smtp.mail.ru";
    /*
     * Incomming..
     */
    static String imapHostName = "imap.mail.ru";
    static String pop3HostName = "pop.mail.ru";
    /*
     * Ports
     */
    static String smtpDefaultPort = "25";
    // cipher    
    static String smtpPort = "465";
    // cipher
    static String pop3Port = "995";
    static String imapPortStartTSL = "143";
    static String imapPortSSL = "993";

    public static void main(String[] args) throws EmailException, MalformedURLException {
        // sendSimpleEmail();
        sendEmailAttachment();
    }
    static String[] emails = new String[]{
        "p.kulitski@gmail.com",
        "kulickipavel@gmail.com",
        "delite_007@mail.ru"
    };

    private static void sendSimpleEmail() throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName(smtpHostName);
        email.setSmtpPort(Integer.parseInt(smtpPort));
        /*
         * basic authentication
         * Every time JavaMail Session will be created. So use set session (Spring
         * Framework) or set-from-jndi (Containers, Tomcat, JBoss).
         */
        email.setAuthenticator(new DefaultAuthenticator(username, password));
        email.setSSLOnConnect(true);
        email.setSSLCheckServerIdentity(true);
        email.setCharset("utf-8");
        email.setFrom(username, "Reports! administrator");
        email.setSubject("Reports! checklist statistics (" + getFormattedDate() + ')');
        email.setMsg("Your statistics");
        email.addTo(emails);
        email.send();
    }

    private static void sendEmailAttachment() throws EmailException, MalformedURLException {
        EmailAttachment emailAttachment = new EmailAttachment();
        emailAttachment.setPath("files/statistics.pdf");
        emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
        emailAttachment.setDescription("PDF file of Reports! statistics");
        emailAttachment.setName("Report! checklist statistics " + getFormattedDate()
                + " in PDF format");

        EmailAttachment emailAttachment2 = new EmailAttachment();
        emailAttachment2.setURL(
                new URL("http://localhost:8091/sp/resources/img/logo/logo.png"));
//        emailAttachment2.setURL(
//                new URL("http://www.google.ru/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&ved=0CCwQFjAA&url=http%3A%2F%2Fwww.nasa.gov%2Fpdf%2F703154main_earth_art-ebook.pdf&ei=n6ZEUpDyJobLswam_oDADA&usg=AFQjCNGiKIEa3_kINKsprxQpUn9jVF8FeA&bvm=bv.53217764,d.Yms&cad=rjt"));

        emailAttachment2.setDisposition(EmailAttachment.ATTACHMENT);
        emailAttachment2.setDescription("Reports! logo");
        emailAttachment2.setName("Report! logo");

        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(smtpHostName);
        email.addTo(emails);
        email.setFrom(username, "Reports! administrator");
        email.setAuthentication(username, password);
        email.setSSLOnConnect(true);
        email.setSSLCheckServerIdentity(true);
        email.setCharset("utf-8");
        email.setSubject("Reports! checklist statistics (" + getFormattedDate() + ')');
        email.setMsg("Your statistics");
        email.attach(emailAttachment);
        email.attach(emailAttachment2);
        // MIME-encoded
        email.send();
    }

    private static String getFormattedDate() {
        DateFormat format = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");
        return format.format(new Date());
    }
}
