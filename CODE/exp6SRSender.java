import java.net.*;
import java.io.*;

public class exp6SRSender {
    public static void main(String[] a) throws Exception {
        ServerSocket ser = new ServerSocket(10);
        Socket s = ser.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in1 = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String sbuff[] = new String[100];
        boolean ack[] = new boolean[100]; // To keep track of acknowledgments for each frame
        PrintStream p;

        int sws, nf, ano, i, base = 0, nextFrame = 0;

        // Get the window size input from the user
        System.out.print("Enter the window size: ");
        sws = Integer.parseInt(in.readLine());

        // Get the total number of frames
        System.out.print("Enter the total number of frames to send: ");
        nf = Integer.parseInt(in.readLine());

        // Inform the receiver about the total number of frames
        p = new PrintStream(s.getOutputStream());
        p.println(nf);

        // Reading all frames from the user and storing them in the buffer
        System.out.println("Enter " + nf + " Messages to send:");
        for (i = 0; i < nf; i++) {
            sbuff[i] = in.readLine();
            ack[i] = false; // Initially no frame is acknowledged
        }

        do {
            // Send frames within the window
            while (nextFrame < nf && (nextFrame - base) < sws) {
                if (!ack[nextFrame]) { // Send only if the frame is not acknowledged
                    p = new PrintStream(s.getOutputStream());
                    System.out.println("Sending Frame " + nextFrame + ": " + sbuff[nextFrame]);
                    p.println(nextFrame + ":" + sbuff[nextFrame]);  // Send frame number and message
                }
                nextFrame++;
            }

            // Waiting for acknowledgment
            System.out.println("Waiting for acknowledgment...");

            long startTime = System.currentTimeMillis();
            int timeout = 5000;  // 5 seconds timeout

            while ((System.currentTimeMillis() - startTime) < timeout) {
                // Check if acknowledgment is received
                if (in1.ready()) {
                    ano = Integer.parseInt(in1.readLine());
                    System.out.println("Acknowledgment received for Frame " + ano);

                    // Mark the frame as acknowledged
                    ack[ano] = true;

                    // Slide the window base if base frame is acknowledged
                    while (ack[base]) {
                        base++;
                    }

                    // If all frames are acknowledged, exit the loop
                    if (base >= nf) {
                        System.out.println("All frames acknowledged.");
                        break;
                    }
                }
            }

            // Check for unacknowledged frames and retransmit them
            for (i = base; i < nextFrame; i++) {
                if (!ack[i]) {
                    System.out.println("Retransmitting Frame " + i + ": " + sbuff[i]);
                    p.println(i + ":" + sbuff[i]);  // Resend unacknowledged frame
                }
            }

        } while (base < nf);  // Continue until all frames are acknowledged

        s.close();
    }
}