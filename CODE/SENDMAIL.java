import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Base64;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SENDMAIL {
    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final int SMTP_PORT = 465;
    private static final String USER = "727722euit212@skcet.ac.in";
    private static final String PASSWORD = "dalb oqfx pkov hjmc"; // Use App Password if using 2FA

    public static void main(String[] args) {
        try {
            // Create an SSL socket to connect to the SMTP server
            SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) ssf.createSocket(SMTP_SERVER, SMTP_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Read server response
            System.out.println("Response: " + reader.readLine());

            // HELO command
            writer.write("HELO " + SMTP_SERVER + "\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            // AUTH LOGIN command
            writer.write("AUTH LOGIN\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            // Send username (Base64 encoded)
            writer.write(Base64.getEncoder().encodeToString(USER.getBytes()) + "\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            // Send password (Base64 encoded)
            writer.write(Base64.getEncoder().encodeToString(PASSWORD.getBytes()) + "\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            // MAIL FROM command
            writer.write("MAIL FROM:<" + USER + ">\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            // RCPT TO command
            writer.write("RCPT TO:<vinuvarsanth01@gmail.com>\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            // DATA command
            writer.write("DATA\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            // Email content
            writer.write("Subject: Test Email\r\n");
            writer.write("From: " + USER + "\r\n");
            writer.write("To: vinuvarsanth01@gmail.com\r\n");
            writer.write("\r\n");
            writer.write("HI THIS IS A TEST MAIL\r\n");
            writer.write(".\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            // QUIT command
            writer.write("QUIT\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            // Close resources
            writer.close();
            reader.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
