import java.net.*;
import java.io.*;

public class exp6gobackSender {
    public static void main(String[] a) throws Exception {
        ServerSocket ser = new ServerSocket(10);
        Socket s = ser.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in1 = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String sbuff[] = new String[100];
        PrintStream p;

        int sws, nf, ano, i, base = 0, nextFrame = 0;
        boolean ackReceived = false;

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
        }

        do {
            // Send frames within the window
            while (nextFrame < nf && (nextFrame - base) < sws) {
                p = new PrintStream(s.getOutputStream());
                System.out.println("Sending Frame " + nextFrame + ": " + sbuff[nextFrame]);
                p.println(nextFrame + ":" + sbuff[nextFrame]);  // Send frame number and message
                nextFrame++;
            }

            // Waiting for acknowledgment
            System.out.println("Waiting for acknowledgment...");

            long startTime = System.currentTimeMillis();
            int timeout = 5000;  // 5 seconds timeout

            while (!ackReceived && (System.currentTimeMillis() - startTime) < timeout) {
                // Check if acknowledgment is received
                if (in1.ready()) {
                    ano = Integer.parseInt(in1.readLine());
                    System.out.println("Acknowledgment received for Frame " + ano);

                    // Slide the window base to the next unacknowledged frame
                    base = ano + 1;
                    ackReceived = true;

                    // If all frames are acknowledged, exit the loop
                    if (base >= nf) {
                        System.out.println("All frames acknowledged.");
                        break;
                    }
                }
            }

            // If acknowledgment is not received, timeout occurred
            if (!ackReceived) {
                System.out.println("Timeout! No acknowledgment received for Frame " + base);
                System.out.println("Retransmitting all frames in the window starting from Frame " + base);

                // Resend all frames in the window starting from base
                for (i = base; i < nextFrame; i++) {
                    p = new PrintStream(s.getOutputStream());
                    System.out.println("Resending Frame " + i + ": " + sbuff[i]);
                    p.println(i + ":" + sbuff[i]);  // Send frame number and message
                }
            } else {
                ackReceived = false;
            }

        } while (base < nf);  // Continue until all frames are acknowledged

        s.close();
    }
}
