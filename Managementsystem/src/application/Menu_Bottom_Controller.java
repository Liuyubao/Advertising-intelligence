
package application;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Menu_Bottom_Controller {

	//define buttons from JavaFX
	@FXML
	private TextField FX_search;

	@FXML
	public void initialize() 
	{
		FX_search.setOnMouseClicked(e -> {
			try {
				URL searchPageUrl = getClass().getResource("../view/DataTable.fxml");
				AnchorPane searchPage = FXMLLoader.load(searchPageUrl);
				BorderPane border = Main.getRoot();
				border.setCenter(searchPage);
				
				URL bottomEmptyUrl = getClass().getResource("../view/Menu_Bottom_Empty.fxml");
				AnchorPane bottomEmpty = FXMLLoader.load(bottomEmptyUrl);
				//BorderPane border = Main.getRoot();
				border.setBottom(bottomEmpty);

			} catch (IOException exception) {
				exception.printStackTrace();
				System.out.println("SWITCH TO SEARCH PAGE ERROR!");
			}
		});
	}

}