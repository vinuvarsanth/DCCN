import java.net.*;
import java.io.*;

class exp6SRReceiver {
    public static void main(String[] a) throws Exception {
        Socket s = new Socket(InetAddress.getLocalHost(), 10);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintStream p = new PrintStream(s.getOutputStream());

        int expectedFrame = 0, totalFrames;
        String rbuf[] = new String[100];
        boolean received[] = new boolean[100]; // To track which frames have been received
        int damagedFrame = 2;  // Assume frame 2 is damaged on first attempt
        boolean damagedFrameReceived = false;  // Flag to track if the damaged frame has been received

        System.out.println("Receiver is ready...");

        // Read the total number of frames to be received (sent by the sender)
        totalFrames = Integer.parseInt(in.readLine());
        System.out.println("Total number of frames to receive: " + totalFrames);

        while (expectedFrame < totalFrames) {
            // Receive the frame from the sender
            String frameMessage = in.readLine();

            // Split the incoming message to get the frame number and content
            String[] parts = frameMessage.split(":");
            int frameNumber = Integer.parseInt(parts[0]);
            String frameContent = parts[1];

            // Simulate frame damage for the first attempt
            if (frameNumber == damagedFrame && !damagedFrameReceived) {
                System.out.println("Frame " + frameNumber + " is damaged. No acknowledgment sent.");
                damagedFrameReceived = true;  // Mark that the damaged frame has been received
                continue;  // Don't send an acknowledgment for the damaged frame
            }

            // If the frame is in range and not already received
            if (frameNumber >= expectedFrame && !received[frameNumber]) {
                System.out.println("Received Frame " + frameNumber + ": " + frameContent);
                rbuf[frameNumber] = frameContent; // Store the frame content
                received[frameNumber] = true; // Mark the frame as received

                // Send acknowledgment for the received frame
                System.out.println("Sending acknowledgment for Frame " + frameNumber);
                p.println(frameNumber);

                // Slide the expected frame pointer to the next missing frame
                while (received[expectedFrame]) {
                    expectedFrame++;
                }
            }
        }

        System.out.println("All frames received and acknowledged.");
        s.close();  // Close the socket when done
    }
}