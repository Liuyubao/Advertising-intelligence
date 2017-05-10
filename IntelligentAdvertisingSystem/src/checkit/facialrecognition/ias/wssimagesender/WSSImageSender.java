package checkit.facialrecognition.ias.wssimagesender;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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

public class WSSImageSender {

	private final SSLSocket socket;
	private final OutputStream outputStream;
	private final InputStream inputStream;

	public WSSImageSender(String truststore, String truststorePassword, String certificatePassword, String adress,
			int port) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
			IOException, UnrecoverableKeyException, KeyManagementException {

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
		System.out.print("Starting Handshake...");
		this.socket.startHandshake();

		printSocketInfo(this.socket);
		this.outputStream = this.socket.getOutputStream();
		this.inputStream = this.socket.getInputStream();

		System.out.println("CONNECTED!");
	}

	public void sendImage(BufferedImage image, int currentAdvertisementID) throws IOException {
		// send AdvertisementID
		System.out.print("Sending the current advertisementID and the captured picture to the server...");
		this.outputStream.write((Integer.toString(currentAdvertisementID) + "\n").getBytes());

		// send a picture
		ImageIO.write(image, "jpg", this.outputStream);
		this.outputStream.write("\r\n".getBytes());

		System.out.println("DONE");
	}

	public int receiveReccomendation() throws IOException {
		byte[] received = new byte[1];
		this.inputStream.read(received);
		String recommendation = new String(received, StandardCharsets.UTF_8);
		if (recommendation.equals("0")) {
			System.out.println("Server couldn't recognise a face");
		} else {
			System.out.println("Server recommends advertisement " + recommendation);
		}
		return Integer.parseInt(recommendation);

	}

	public void close() {
		this.socket.getSession().invalidate();
		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}