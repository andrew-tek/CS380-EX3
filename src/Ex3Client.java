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
		 	System.out.println(value);
		 	byte [] arr = new byte [value];
		 	for (int i = 0; i < value; i++) {
		 		arr[i] = (byte) is.read();
		 	}
		 	short checkSum = checksum (arr);
		 	byte [] returnData = new byte[2];
		 	System.out.println(checkSum);
		 	int temp = checkSum >> 8;
			temp = temp & 0xFF;
			returnData[0]=(byte) temp;
			temp = (int)checkSum; 
			temp = temp & 0xFF;
			returnData[1] = (byte)temp;
			out.write(returnData);
			value = is.read();
			System.out.println(value);
		 	
		}

	}
	public static short checksum(byte[] b) {
		int checkSum = 0;
		int value;
		for (int i = 0; i < b.length / 2; i++) {
			try  {
				value = twoBytesToShort(b[i * 2], b[i * 2 + 1]);
			}
			catch (Exception IndexOutOfBoundsException) {
				value = (((b[i] << 8) & 0xFF00) | ((b[i + 1]) & 0xFF));
			}
			checkSum += (int) value;
			if ((checkSum & 0xFFFF0000) > 0) {
				//checkSum += (b[i] << 8 & 0xFF00);
				checkSum = checkSum & 0xFFFF;
				checkSum++;
			}
			
			/*try {
				Math.addExact(checkSum, (int)value);
			}
			catch (Exception ArithmeticException) {
				
			}*/
					
		}
		return (short)(~checkSum & 0xFFFF);
		
	}
	public static short twoBytesToShort(byte b1, byte b2) {
        return (short) ((b1 << 8) | (b2 & 0xFF));
}

}
