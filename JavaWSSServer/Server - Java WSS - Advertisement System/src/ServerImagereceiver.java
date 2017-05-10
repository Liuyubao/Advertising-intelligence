import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteOrder;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public class ServerImagereceiver {
	private String ksName;
	private char[] ksPass;
	private char[] ctPass;
	private SSLServerSocketFactory sslServerSocketFactory;
	private BufferedInputStream inputStream;

	public ServerImagereceiver(String ksName, String ksPass, String ctPass) throws Exception {
		this.ksName = ksName;
		this.ksPass = ksPass.toCharArray();
		this.ctPass = ctPass.toCharArray();
		System.setProperty("javax.net.ssl.trustStore", ksName);
		System.setProperty("javax.net.ssl.trustStorePassword", ksPass);	

		setupSocketFactory();
	}




	private void setupSocketFactory() throws Exception {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream(ksName), ksPass);
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(keyStore, ctPass);
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
		this.sslServerSocketFactory = sslContext.getServerSocketFactory();		
	}

	public void createSocket(int port) throws IOException {
		SSLServerSocket serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);
		serverSocket.setNeedClientAuth(true);
		printServerSocketInfo(serverSocket);
		SSLSocket socket = (SSLSocket) serverSocket.accept();
		printSocketInfo(socket);

		this.inputStream = new BufferedInputStream(socket.getInputStream());			
	}

	public void receiveImageAndWriteToFile() throws IOException{
		// receive size of image to be received
		byte[] messagelength = new byte[4];
		inputStream.read(messagelength);
		int length = my_bb_to_int_le(messagelength);
		System.out.println("Receiving Image!");
		System.out.println("Size: " + length+" bytes");


		//receive image in chunks of 8kb
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		byte[] buffer = new byte[8192]; 

		while (length > 8192) {
			inputStream.read(buffer);
			byteArrayOutputStream.write(buffer);
			System.out.println("Receiving 8192 bytes...");
			length -= 8192;
		}
		inputStream.read(buffer, 0, length);
		byteArrayOutputStream.write(buffer, 0, length);
		System.out.println("Receiving " + length + " bytes...");

		//receive AdvertisementID
		byte[] bAdvertisementID = new byte[4];
		inputStream.read(bAdvertisementID);
		int advertisementID = my_bb_to_int_le(bAdvertisementID);
		System.out.println("Image received!");

		//write Image to drive
		byte[] imageArray = byteArrayOutputStream.toByteArray();

		BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageArray));

		ImageIO.write(image, "jpg",
				new File("/home/server/ReceivedImages/"
						+ new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()) + ":" + advertisementID
						+ ".jpg"));
	}


	private static void printSocketInfo(SSLSocket s) {
		System.out.println("Socket class: " + s.getClass());
		System.out.println("   Remote address = " + s.getInetAddress().toString());
		System.out.println("   Remote port = " + s.getPort());
		System.out.println("   Local socket address = " + s.getLocalSocketAddress().toString());
		System.out.println("   Local address = " + s.getLocalAddress().toString());
		System.out.println("   Local port = " + s.getLocalPort());
		System.out.println("   Need client authentication = " + s.getNeedClientAuth());
		SSLSession ss = s.getSession();
		try {
			System.out.println("Session class: " + ss.getClass());
			System.out.println("   Cipher suite = " + ss.getCipherSuite());
			System.out.println("   Protocol = " + ss.getProtocol());
			System.out.println("   PeerPrincipal = " + ss.getPeerPrincipal().getName());
			System.out.println("   LocalPrincipal = " + ss.getLocalPrincipal().getName());
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	private static void printServerSocketInfo(SSLServerSocket s) {
		System.out.println("Server socket class: " + s.getClass());
		System.out.println("   Socket address = " + s.getInetAddress().toString());
		System.out.println("   Socket port = " + s.getLocalPort());
		System.out.println("   Need client authentication = " + s.getNeedClientAuth());
		System.out.println("   Want client authentication = " + s.getWantClientAuth());
		System.out.println("   Use client mode = " + s.getUseClientMode());
	}

	public static byte[] my_int_to_bb_le(int myInteger) {
		return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(myInteger).array();
	}

	public static int my_bb_to_int_le(byte[] byteBarray) {
		return ByteBuffer.wrap(byteBarray).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

}
