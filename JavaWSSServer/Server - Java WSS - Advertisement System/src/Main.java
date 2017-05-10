import java.io.IOException;

public class Main {

	public static void main(String[] args) throws Exception {
		// check arguments
		if (args.length < 3) {
			System.out.println("Usage:");
			System.out.println("   java ServerImageReceiver ksName ksPass ctPass");
			return;
		}
		ServerImagereceiver server = new ServerImagereceiver(args[0], args[1], args[2]);
		
		server.createSocket(8888);
		
		while(true){
			try {
				server.receiveImageAndWriteToFile();
			} catch (IOException e) {
				System.out.println("CONNECTION INTERUPTED!");
				e.printStackTrace();
				server.createSocket(8888);
			}
		}
		
		
	}
}
