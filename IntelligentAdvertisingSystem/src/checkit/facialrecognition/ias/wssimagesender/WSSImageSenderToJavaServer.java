package checkit.facialrecognition.ias.wssimagesender;
/*
 * This class was used to connect to a temporary java server
 * but is not used anymore because the server is now implemented in python.
 */

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.imageio.ImageIO;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class WSSImageSenderToJavaServer {

	private final SSLSocket socket;
	private final BufferedInputStream socketInputSteam;
	private final BufferedOutputStream socketOutputStream;

	public WSSImageSenderToJavaServer(String truststore, String truststorePassword, String certificatePassword,
			String adress, int port) throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException {
		System.setProperty("javax.net.ssl.trustStore", truststore);
		System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);
		KeyStore keystore = KeyStore.getInstance("JKS");
		keystore.load(new FileInputStream(truststore), truststorePassword.toCharArray());
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(keystore, certificatePassword.toCharArray());
		SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
		sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
		SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
		this.socket = (SSLSocket) sslSocketFactory.createSocket(adress, port);

		System.out.println("Socket created successfully");
		printSocketInfo(this.socket);
		System.out.print("Starting Handshake...");
		this.socket.startHandshake();
		this.socketOutputStream = new BufferedOutputStream(this.socket.getOutputStream());
		this.socketInputSteam = new BufferedInputStream(this.socket.getInputStream());
		System.out.println("CONNECTED!");
	}

	public void writeImage(BufferedImage image, int currentAdvertisementID) throws IOException {
		// turn the BufferedImage into a byte array
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", byteArrayOutputStream);
		byte[] imageByteArray = byteArrayOutputStream.toByteArray();

		// send the image to the server
		this.writeImageByteArray(imageByteArray, currentAdvertisementID);
	}

	public void writeImageByteArray(byte[] imageByteArray, int currentAdvertisementID) throws IOException {
		// calculate length of the image and send it to the server
		System.out.println("Sending image to the server of size: " + imageByteArray.length + " bytes...");
		byte[] messagelength = my_int_to_bb_le(imageByteArray.length);
		this.socketOutputStream.write(messagelength);

		// write the image with 8192 bytes at a time
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageByteArray);

		int count;
		byte[] buffer = new byte[8192];
		while ((count = byteArrayInputStream.read(buffer)) > 0) {
			this.socketOutputStream.write(buffer, 0, count);
			System.out.println("Sending " + count + " bytes...");
		}

		this.socketOutputStream.write(my_int_to_bb_le(currentAdvertisementID));

		this.socketOutputStream.flush();
		System.out.println("Image Sent!");

	}

	public void receive() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public void close() throws IOException {
		this.socketInputSteam.close();
		this.socketOutputStream.close();
		this.socket.close();
	}

	private static void printSocketInfo(SSLSocket s) {
		System.out.println("Socket class: " + s.getClass());
		System.out.println("   Remote address = " + s.getInetAddress().toString());
		System.out.println("   Remote port = " + s.getPort());
		System.out.println("   Local socket address = " + s.getLocalSocketAddress().toString());
		System.out.println("   Local address = " + s.getLocalAddress().toString());
		System.out.println("   Local port = " + s.getLocalPort());
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

	public static byte[] my_int_to_bb_le(int myInteger) {
		return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(myInteger).array();
	}

	public static int my_bb_to_int_le(byte[] byteBarray) {
		return ByteBuffer.wrap(byteBarray).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
}
