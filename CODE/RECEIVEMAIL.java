import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

class RECEIVEMAIL {
    private static final String POP3_SERVER = "pop.gmail.com"; // POP3 server for Gmail
    private static final int POP3_PORT = 995; // POP3 port with SSL
    private static final String USER = "vinuvarsanth01@gmail.com";
    private static final String PASSWORD = "ieyr yirj mrjn slle"; // Use App Password if using 2FA


    public static void main(String[] args) {
        try {
            // Create an SSL socket to connect to the POP3 server
            SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) ssf.createSocket(POP3_SERVER, POP3_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Read server greeting
            System.out.println("Response: " + reader.readLine());

            // Send USER command
            writer.write("USER " + USER + "\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            // Send PASS command
            writer.write("PASS " + PASSWORD + "\r\n");
            writer.flush();
            String response = reader.readLine();
            System.out.println("Response: " + response);

            // Check if authentication was successful
            if (response.startsWith("-ERR")) {
                System.out.println("Authentication failed. Please check your username and password.");
                return; // Exit the program
            }

            // Send STAT command to get the number of messages
            writer.write("STAT\r\n");
            writer.flush();
            response = reader.readLine();
            System.out.println("Response: " + response);

            // Extract the number of messages
            int totalMessages = Integer.parseInt(response.split(" ")[1]);
            System.out.println("Total messages: " + totalMessages);

            // Determine how many messages to fetch (last 5)
            int messagesToFetch = Math.min(5, totalMessages);

            // Fetch the latest emails
            for (int i = totalMessages; i > totalMessages - messagesToFetch; i--) {
                // Send RETR command to fetch the email
                writer.write("RETR " + i + "\r\n");
                writer.flush();
                response = reader.readLine();
                System.out.println("Response: " + response); // This shows the email content

                // Read the email content
                StringBuilder emailContent = new StringBuilder();
                String line;
                while (!(line = reader.readLine()).equals(".")) {
                    emailContent.append(line).append("\n");
                }

                // Parse the email content for headers
                String[] headers = emailContent.toString().split("\n");
                String subject = "", from = "", to = "", date = "";

                // Extract headers
                for (String header : headers) {
                    if (header.startsWith("Subject:")) {
                        subject = header.substring(9).trim();
                    } else if (header.startsWith("From:")) {
                        from = header.substring(6).trim();
                    } else if (header.startsWith("To:")) {
                        to = header.substring(4).trim();
                    } else if (header.startsWith("Date:")) {
                        date = header.substring(6).trim();
                    }
                }

                // Display extracted fields
                System.out.println("Email " + i + ":");
                System.out.println("Subject: " + subject);
                System.out.println("From: " + from);
                System.out.println("To: " + to);
                System.out.println("Date: " + date);
                System.out.println("\n--- End of Email " + i + " ---\n");
            }

            // Send QUIT command
            writer.write("QUIT\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            writer.close();
            reader.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}