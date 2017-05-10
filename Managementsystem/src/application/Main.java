package application;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	private static BorderPane root = new BorderPane();
	
	//main display where all the pages will be shown.
	  public static BorderPane getRoot() {
		    return root;
		  }

	@Override
	public void start(Stage primaryStage) throws IOException {

		//add URL to toolbar on the top.
		URL menuBarUrl = getClass().getResource("../view/Menu_Top.fxml");
		AnchorPane top = FXMLLoader.load( menuBarUrl );

		//add URL to content page in the middle.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/HomePage.fxml"));
		BorderPane homePage = loader.load();
		homePage.setMaxSize(1055, 425);

		//add URL to toolbar on the bottom.
		URL menuBottomUrl = getClass().getResource("../view/Menu_Bottom.fxml");
		AnchorPane bottom = FXMLLoader.load(menuBottomUrl);

		//set which pages is on the top, middle, bottom.
		root.setTop(top);
		root.setCenter(homePage);
		root.setBottom(bottom);
		BorderPane.setMargin(root, new Insets(0,0,0,0)); // no margin

		//display the scene in the stage
		Scene scene = new Scene(root, 1055, 670);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.sizeToScene(); // resize stage to scene size
		primaryStage.show();

		//shutdown controller
		Homepage_Controller controller = loader.getController();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				controller.shutDown();
			}
		});
	}

	//load whole project
	public static void main(String[] args) {
		launch(args);
	}
}