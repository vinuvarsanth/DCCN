import java.util.Scanner;
public class BitStuffing {
    
    // Function to perform bit stuffing
    public static String bitStuff(String data) {
        StringBuilder stuffedData = new StringBuilder();
        int count = 0;
        
        for (int i = 0; i < data.length(); i++) {
            char bit = data.charAt(i);
            stuffedData.append(bit);
            
            if (bit == '1') {
                count++;
                if (count == 5) {
                    // Add a '0' after five consecutive '1's
                    stuffedData.append('0');
                    count = 0; // reset count
                }
            } else {
                count = 0; // reset count when bit is '0'
            }
        }
        return stuffedData.toString();
    }
    
    // Function to perform bit unstuffing
    public static String bitUnstuff(String stuffedData) {
        StringBuilder unstuffedData = new StringBuilder();
        int count = 0;
        
        for (int i = 0; i < stuffedData.length(); i++) {
            char bit = stuffedData.charAt(i);
            if (bit == '1') {
                count++;
            } else {
                count = 0; // reset count when bit is '0'
            }
            
            unstuffedData.append(bit);
            
            if (count == 5) {
                // Skip the '0' added by bit stuffing
                if (i + 1 < stuffedData.length() && stuffedData.charAt(i + 1) == '0') {
                    i++; // skip the next bit (the stuffed '0')
                    count = 0; // reset count
                }
            }
        }
        return unstuffedData.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the binary data: ");
        String data = scanner.next();
        
        // Perform bit stuffing
        String stuffedData = bitStuff(data);
        System.out.println("Stuffed Data: " + stuffedData);
        
        // Perform bit unstuffing
        String unstuffedData = bitUnstuff(stuffedData);
        System.out.println("Unstuffed Data: " + unstuffedData);
        
        scanner.close();
    }
}
