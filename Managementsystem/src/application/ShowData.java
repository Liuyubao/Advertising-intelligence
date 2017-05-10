package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;



public class ShowData extends Application{

	private Stage primaryStage;
    private BorderPane rootLayout;
    private String AdId;
    
    @Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		initRootLayout();
		System.out.println("Adid:"+AdId);
		if(AdId==null)
			showAdvertisementOverview();
		else
			showChart();

	}
    
    public BorderPane getRoot() {
	    return rootLayout;
	  }
    
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ShowData.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public void showAdvertisementOverview() {
        try {
            // Load ad overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ShowData.class.getResource("DataTable.fxml"));
            AnchorPane DataTable = (AnchorPane) loader.load();

            // Set ad overview into the center of root layout.
            rootLayout.setCenter(DataTable);

            // Give the controller access to the main app.
            DataController controller = loader.getController();
            controller.initialize();   
            AdId=controller.select_adid;
            System.out.println("Adid:"+AdId);
          
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public void showChart(){
		try {
            // Load ad overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ShowData.class.getResource("Chart.fxml"));
            AnchorPane Chart = (AnchorPane) loader.load();


			
            rootLayout.setCenter(Chart);


        } catch (Exception e) {
            e.printStackTrace();
        }
	}
    public static void main(String[] args) {
        launch(args);
    }
}
