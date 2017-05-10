package application;

import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Mappage_Controller {
	
	@FXML
	private WebView FX_mappage_webview;
	
	@FXML
	private AnchorPane FX_mappage_anchorpane;
	
//	private WebEngine webEngine = browser.getEngine();
	
//insert and display the HTML file in the webview
	@FXML
	public void initialize() {
		FX_mappage_webview.setMaxSize(640, 360);
        //apply the styles
	    WebEngine webEngine = FX_mappage_webview.getEngine();
        // load the web page
	    URL url = getClass().getResource("/view/MAP.html");
        webEngine.load(url.toExternalForm());
        }

}