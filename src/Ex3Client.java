import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Ex3Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		try (Socket socket = new Socket("codebank.xyz", 38103)) {
		 	System.out.println("Connected to server...");
		 	int value;
		 	InputStream is = socket.getInputStream();
		 	OutputStream out = socket.getOutputStream();
		 	value = is.read();
		 	System.out.println("Number of bytes: " + value);
		 	byte [] arr = new byte [value];
		 	System.out.print("Data received: ");
		 	for (int i = 0; i < value; i++) {
		 		arr[i] = (byte) is.read();
		 	}
		 	for (int i = 0; i < arr.length; i++) {
		 		if (i % 20 == 0) {
		 			System.out.println();
		 		}
		 		System.out.print(String.format("%02x", arr[i]));
		 		
		 	}
		 	short checkSum = checksum (arr);
		 	byte [] data = new byte[2];
		 	System.out.println(checkSum);
		 	int tmp = checkSum >> 8;
			tmp = tmp & 0xFF;
			data[0]=(byte) tmp;
			tmp = (int)checkSum; 
			tmp = tmp & 0xFF;
			data[1] = (byte)tmp;
			System.out.println("Checksum Calculated: " + String.format("%02x", data[0]) + String.format("%02x", data[1]));
			out.write(data);
			value = is.read();
			if (value == 1) {
				System.out.println("Response Good!");
			}
			else
				System.out.println("Incorrect checksum...");
		 	
		}

	}
	public static short checksum(byte[] b) {
		int checkSum = 0;
		int value, length;
		if (b.length % 2 == 1)
			length = b.length / 2 + 1;
		else
			length = b.length / 2;
		for (int i = 0; i < length; i++) {
				
			try  {
				value = (((b[i * 2] << 8) & 0xFF00) | ((b[i * 2 + 1]) & 0xFF));
				checkSum += value;
			}
			catch (Exception IndexOutOfBoundsException) {
				checkSum += (b[i * 2] << 8 & 0xFF00);
				
			}
			if ((checkSum & 0xFFFF0000) > 0) {
				//checkSum += (b[i] << 8 & 0xFF00);
				checkSum = checkSum & 0xFFFF;
				checkSum++;
			}
					
		}
		return (short)(~checkSum & 0xFFFF);
		
	}
}
