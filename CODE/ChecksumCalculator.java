import java.util.Scanner;

public class ChecksumCalculator {

    // Method to calculate the checksum
    public static String calculateChecksum(String[] frames, int bitsPerFrame) {
        int sum = 0;
        
        // Convert each frame to an integer and sum them up
        for (String frame : frames) {
            sum += Integer.parseInt(frame, 2);
        }
        
        // Handle overflow: Keep the sum within the range of frame size (2^bitsPerFrame)
        int modValue = (1 << bitsPerFrame); // This is 2^bitsPerFrame
        while (sum >= modValue) {
            sum = (sum % modValue) + (sum / modValue); // Handle overflow
        }

        // Calculate one's complement of the sum
        String checksum = Integer.toBinaryString(~sum & (modValue - 1)); // Only take bitsPerFrame bits
        return String.format("%" + bitsPerFrame + "s", checksum).replace(' ', '0');
    }

    // Method to verify the checksum at the receiver
    public static boolean verifyChecksum(String[] receivedFrames, int bitsPerFrame) {
        int sum = 0;

        // Convert each received frame to an integer and sum them up
        for (String frame : receivedFrames) {
            sum += Integer.parseInt(frame, 2);
        }

        // Handle overflow just like before
        int modValue = (1 << bitsPerFrame);
        while (sum >= modValue) {
            sum = (sum % modValue) + (sum / modValue); // Handle overflow
        }

        // Check if the sum is 111...1 (which means 0 in one’s complement)
        return sum == (modValue - 1);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get number of frames and bits per frame
        System.out.print("Enter the number of frames: ");
        int numFrames = scanner.nextInt();
        
        System.out.print("Enter the number of bits per frame: ");
        int bitsPerFrame = scanner.nextInt();

        // Get the frames from the user
        String[] frames = new String[numFrames];
        System.out.println("Enter the frames (each frame should be " + bitsPerFrame + " bits):");
        for (int i = 0; i < numFrames; i++) {
            frames[i] = scanner.next();
        }

        // Calculate checksum at sender
        String checksum = calculateChecksum(frames, bitsPerFrame);
        System.out.println("Calculated checksum at sender: " + checksum);

        // Get received frames (including the checksum)
        System.out.println("Enter received frames (including the checksum):");
        String[] receivedFrames = new String[numFrames + 1];
        for (int i = 0; i < numFrames + 1; i++) {
            receivedFrames[i] = scanner.next();
        }

        // Verify the checksum at the receiver
        if (verifyChecksum(receivedFrames, bitsPerFrame)) {
            System.out.println("No error detected in received data.");
        } else {
            System.out.println("Error detected in received data.");
        }

        scanner.close();
    }
}
