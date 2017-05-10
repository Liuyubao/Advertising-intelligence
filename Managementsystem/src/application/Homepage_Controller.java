package application;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Homepage_Controller {

	@FXML
	private ImageView FX_adView;

	@FXML
	private GridPane FX_Grid;
	
	public int measurementID;
	public String advertisementID;
	private static final long maxDisplayTimeMS = 2000; // 2 seconds
	public long lastFixAttemptTime;
	private long startOfDisplayingImage = 0;
	private ScheduledExecutorService timer; 

	//initialize() will always be active after the views are loaded.
	@FXML
	public void initialize() {
		FX_adView.setFitWidth(600);
		FX_adView.setFitHeight(400);

		lastFixAttemptTime = System.currentTimeMillis();
		this.timer = Executors.newSingleThreadScheduledExecutor();
		this.timer.scheduleAtFixedRate(new MainLoop(), 0, 1, TimeUnit.SECONDS);
	}

	//run() function will be active 
	private class MainLoop implements Runnable {
		@Override
		public void run() {
			maybeChangeAdvertisement();
		}

		//insert a condition about to change an ad or not.
		//there is a check in the database after every 2 seconds.
		private void maybeChangeAdvertisement() {

			if (enoughTimePassed()) {
				selectNextAdvertisement();
			}
		}

		//check if the time has passed after maxDisplayTimeMS = 2000, 2 seconds.
		private boolean enoughTimePassed() {
			if ((System.currentTimeMillis() - startOfDisplayingImage) > maxDisplayTimeMS)
				return true;
			return false;
		}
//get the latest picture number AdvertisementID from the latest measurementID.
		//return picture name, measurement ID, local time.
		private void selectNextAdvertisement() {
			Check_Advertisement newAdViewController = new Check_Advertisement();
			newAdViewController.initializedata();
			advertisementID = newAdViewController.getAdid();
			change_advertisement(advertisementID);

			System.out.println("Picture number: " + advertisementID);
			System.out.println("Current time is: " + LocalTime.now());

			startOfDisplayingImage = System.currentTimeMillis();
		}

		//after the run function, set the latest AdvertisementID number into the imagepath to change the advertisement for the imageview.
		private void change_advertisement(String advertisementID) {
			Platform.runLater(() -> {  
				String imagePath = "/Advertisement/" + advertisementID + ".jpg";
				System.out.println("print the image path: " + imagePath);
				System.out.println("*****show ad*****");
				FX_adView.setImage(new Image(imagePath, true));
			});

		}
	}

	//this can help you stop the terminate process.
	public void shutDown() {
		System.out.println(" Shutdown called");
		timer.shutdown();

	}

}