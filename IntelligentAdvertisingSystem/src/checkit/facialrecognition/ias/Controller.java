package checkit.facialrecognition.ias;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import checkit.facialrecognition.ias.camera.Camera;
import checkit.facialrecognition.ias.camera.CameraException;
import checkit.facialrecognition.ias.wssimagesender.WSSImageSender;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Controller {
	private static final int advertisementAmount = 4;
	private static final long maxDisplayTimeMS = 5000;
	private static final long fixAttemptDelayTimeMS = 20000;
	private static final int backgroundServiceIntervalMS = 1000;

	private static final String keystorePath = "src/keystore/frclientkeystore.jks";
	private static final String keystorePassword = "coffee";
	private static final String certificatePassword = "coffee";

	private static final String serverAdress = "172.16.19.252";
	private static final int serverPort = 8065;

	@FXML
	private ImageView imageView;

	@FXML
	private StackPane pane;

	private ScheduledExecutorService backgroundService;
	private Camera camera;

	private boolean cameraActive;
	private boolean connectionActive;

	private long lastFixAttemptTime;
	private int currentAdvertisementID = 1;
	private long startOfDisplayingImage = 0;
	private WSSImageSender socket;

	@FXML
	public void initialize() {
		// This makes sure the imageview doesn't resize the rest of the
		// application
		this.imageView.fitWidthProperty().bind(this.pane.widthProperty());
		this.imageView.fitHeightProperty().bind(this.pane.heightProperty());

		this.changeAdvertisement(1);
		this.setupCamera();
		this.setupConnection();

		this.lastFixAttemptTime = System.currentTimeMillis();
		this.backgroundService = Executors.newSingleThreadScheduledExecutor();
		this.backgroundService.scheduleAtFixedRate(new BackgroundService(), 0, backgroundServiceIntervalMS,
				TimeUnit.MILLISECONDS);

	}

	private void setupCamera() {
		try {
			this.camera = new Camera(0);
			this.cameraActive = true;
		} catch (Exception e) {
			System.out.println("CAMERA PROBLEM!");
			e.printStackTrace();
			this.cameraActive = false;
		}
	}

	private void setupConnection() {
		try {
			this.socket = new WSSImageSender(keystorePath, keystorePassword, certificatePassword, serverAdress,
					serverPort);

			this.connectionActive = true;

		} catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException
				| CertificateException e) {

			System.out.println("PROBLEM WITH RECOVERING THE CERTIFICATE!");
			this.connectionActive = false;
			e.printStackTrace();

		} catch (IOException e) {
			System.out.println("CONNECTION PROBLEM!");
			this.connectionActive = false;
			e.printStackTrace();
		}

	}

	private class BackgroundService implements Runnable {
		private int recommendation = 0;

		@Override
		public void run() {
			this.makePictureAndSendToServer();

			this.maybeChangeAdvertisement();

			this.maybeFixProblems();
		}

		private void makePictureAndSendToServer() {
			if (Controller.this.cameraActive && Controller.this.connectionActive) {
				try {
					System.out.print("Capturing picture...");
					BufferedImage image = Controller.this.camera.takePicture();
					System.out.println("DONE");
					try {
						Controller.this.socket.sendImage(image, Controller.this.currentAdvertisementID);
						this.recommendation = Controller.this.socket.receiveReccomendation();
					} catch (IOException e) {
						System.out.println("CONNECTION PROBLEM!");
						Controller.this.connectionActive = false;
						e.printStackTrace();
					}

				} catch (CameraException e) {
					System.out.println("CAMERA PROBLEM!");
					Controller.this.cameraActive = false;
					e.printStackTrace();
				}
			}
		}

		private void maybeChangeAdvertisement() {

			if (this.enoughTimePassed()) {
				this.selectNextAdvertisement();
			}

		}

		private boolean enoughTimePassed() {
			if ((System.currentTimeMillis() - Controller.this.startOfDisplayingImage) > maxDisplayTimeMS)
				return true;
			return false;
		}

		private void selectNextAdvertisement() {
			System.out.println("\n-----Time to switch advertisement-----");
			if ((this.recommendation != 0) && (this.recommendation != Controller.this.currentAdvertisementID)) {
				System.out.println("Switching to the next adertisement based on the suggestion from the server");
				Controller.this.currentAdvertisementID = this.recommendation;
			} else {
				System.out.println("Switching to the next advertisement based on order");
				Controller.this.currentAdvertisementID++;
				if (Controller.this.currentAdvertisementID > advertisementAmount) {
					Controller.this.currentAdvertisementID = 1;
				}
			}
			System.out.println("Displaying advertisement " + Controller.this.currentAdvertisementID + "\n\n");
			Controller.this.changeAdvertisement(Controller.this.currentAdvertisementID);
			Controller.this.startOfDisplayingImage = System.currentTimeMillis();

		}

		private void maybeFixProblems() {
			// check if there is a problem with the camera or connection and if
			// there is enough time elapsed to try again
			if ((!Controller.this.cameraActive || !Controller.this.connectionActive)
					&& ((System.currentTimeMillis() - Controller.this.lastFixAttemptTime) > fixAttemptDelayTimeMS)) {
				this.fixProblems();
			}
		}

		private void fixProblems() {
			Controller.this.lastFixAttemptTime = System.currentTimeMillis();

			if (!Controller.this.cameraActive) {
				Controller.this.setupCamera();
			}
			if (!Controller.this.connectionActive) {
				Controller.this.setupConnection();
			}
		}

	}

	private void changeAdvertisement(int advID) {
		// The JavaFX GUI can only be updated from the main tread.
		Platform.runLater(() -> {
			String imagePath = "advertisements/" + advID + ".jpg";
			this.imageView.setImage(new Image(imagePath, true));
		});
	}

	protected void shutDown() {
		// stop the MainLoop from being called and try to close the socket
		this.backgroundService.shutdown();
		try {
			this.socket.close();
		} catch (Exception e) {
		}
	}

}
