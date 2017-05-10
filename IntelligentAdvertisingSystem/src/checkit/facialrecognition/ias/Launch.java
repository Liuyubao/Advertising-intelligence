package checkit.facialrecognition.ias;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Launch extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		//primaryStage.setFullScreen(true);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/View.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

		// set the proper behavior on closing the application
		// this makes sure the executorservice gets stopped 
		Controller controller = loader.getController();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				controller.shutDown();
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
