
package application;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Menu_Top_Controller {

	//define buttons from JavaFX
	@FXML
	private Button homepagebutton;

	@FXML
	private Button contactbutton; 

	@FXML
	private Button teammembersbutton;

	@FXML
	private Button accountsettingsbutton;

	@FXML
	private Button loginbutton;

	//define action event for the button & call related function from the controller and display it in the middle of the app.
	@FXML
	void switchToHomePage(ActionEvent event) {
		try {
			URL homePageUrl = getClass().getResource("../view/HomePage.fxml");
			BorderPane homePage = FXMLLoader.load(homePageUrl);
			BorderPane border = Main.getRoot();
			border.setCenter(homePage);
			
			URL menuBottomUrl = getClass().getResource("../view/Menu_Bottom.fxml");
			AnchorPane bottom = FXMLLoader.load(menuBottomUrl);
			border.setBottom(bottom);
			
			Homepage_Controller hpcrtl = new Homepage_Controller();
			hpcrtl.initialize();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("SWITCH TO HOME PAGE ERROR!");
		}
	}

	@FXML
	void switchToContactPage(ActionEvent event) {
		try {
			URL mapPageUrl = getClass().getResource("../view/MapPage_3.fxml");
			AnchorPane mapPage = FXMLLoader.load(mapPageUrl);
			BorderPane border = Main.getRoot();
			border.setCenter(mapPage);
			
			URL menuBottomUrl = getClass().getResource("../view/Menu_Bottom.fxml");
			AnchorPane bottom = FXMLLoader.load(menuBottomUrl);
			border.setBottom(bottom);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("SWITCH TO MAP PAGE ERROR!");
		}
	}

	@FXML
	void switchToMemberPage(ActionEvent event) {
		try {
			URL memberPageUrl = getClass().getResource("../view/MemberPage.fxml");
			AnchorPane memberPage = FXMLLoader.load(memberPageUrl);
			BorderPane border = Main.getRoot();
			border.setCenter(memberPage);
			
			URL menuBottomUrl = getClass().getResource("../view/Menu_Bottom.fxml");
			AnchorPane bottom = FXMLLoader.load(menuBottomUrl);
			border.setBottom(bottom);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("SWITCH TO MEMBER PAGE ERROR!");
		}
	}

	@FXML
	void switchToSettingsPage(ActionEvent event) {
		try {
			URL settingsPageUrl = getClass().getResource("../view/SettingsPage.fxml");
			AnchorPane settingsPage = FXMLLoader.load(settingsPageUrl);
			BorderPane border = Main.getRoot();
			border.setCenter(settingsPage);
			
			URL menuBottomUrl = getClass().getResource("../view/Menu_Bottom.fxml");
			AnchorPane bottom = FXMLLoader.load(menuBottomUrl);
			border.setBottom(bottom);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("SWITCH TO SETTINGS PAGE ERROR!");
		}
	}

	@FXML
	void switchToLoginPage(ActionEvent event) {
		try {
			URL loginPageUrl = getClass().getResource("../view/LoginPage.fxml");
			AnchorPane loginPage = FXMLLoader.load(loginPageUrl);
			BorderPane border = Main.getRoot();
			border.setCenter(loginPage);
			
			URL menuBottomUrl = getClass().getResource("../view/Menu_Bottom.fxml");
			AnchorPane bottom = FXMLLoader.load(menuBottomUrl);
			border.setBottom(bottom);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("SWITCH TO LOGIN PAGE ERROR!");
		}
	}

}