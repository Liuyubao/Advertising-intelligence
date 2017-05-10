package application;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

public class DataController {
	private final ObservableList<Advertisement> data = FXCollections.observableArrayList();
    private final ObservableList<Advertisement> Querydata = FXCollections.observableArrayList();
    private final ObservableList<Advertisement> Genderdata = FXCollections.observableArrayList();
    private final ObservableList<Advertisement> Agedata = FXCollections.observableArrayList();
    private final ObservableList<Advertisement> Adiddata = FXCollections.observableArrayList();
    private final ObservableList<Advertisement> Adtypedata = FXCollections.observableArrayList();
    private String chosen_age;
    private String chosen_gender;
    private String chosen_adid;
    private String chosen_adtype;
    private int search = 0;
    
    public String select_adid;
    
    final CategoryAxis xAxis_gender = new CategoryAxis();
    final NumberAxis yAxis_gender = new NumberAxis();
    final StackedBarChart<String, Number> gender_chart = new StackedBarChart<String, Number>(xAxis_gender, yAxis_gender);
    final XYChart.Series<String, Number> series_gender = new XYChart.Series<String, Number>();
    ObservableList<String> genderName = FXCollections.observableArrayList("Female","Male");
    int num_f = 0, num_m = 0;
    final CategoryAxis xAxis_age = new CategoryAxis();
    final NumberAxis yAxis_age = new NumberAxis();
    final StackedBarChart<String, Number> age_chart = new StackedBarChart<String, Number>(xAxis_age, yAxis_age);
    final XYChart.Series<String, Number> series_age = new XYChart.Series<String, Number>();
    ObservableList<String> ageName = FXCollections.observableArrayList("Young","Old");
    int num_y = 0, num_o = 0;
    
    int pageIndex = 1;
    int query = 0;
    int datasize;
     
    @FXML
    private TableView<Advertisement> table = new TableView<Advertisement>();
    @FXML
    private Label agelabel;
    @FXML
    private Label curpage;
    @FXML
    private Label tolpage;
    @FXML
    private ChoiceBox age_cb;
    @FXML
    private ChoiceBox adtype_cb;
    //@FXML
    private ToggleGroup group;
    @FXML
    private RadioButton rb1;
    @FXML
    private RadioButton rb2;
    @FXML    
    private Label adidlabel;
    @FXML
    private TextField idtext;
    @FXML
    private TextField pagetext;
    @FXML
    private Button searchbutton;
    @FXML
    private Button detailbutton;
    @FXML
    private Button previousbutton;
    @FXML
    private Button nextbutton;
    @FXML
    private Button gotobutton;
    @FXML
    private TableColumn AdIDCol;
    @FXML
    private TableColumn AdTypeCol;
    @FXML
    private TableColumn AgeCol;
    @FXML
    private TableColumn GenderCol;
    @FXML
    private TableColumn ChubbyCol;
    @FXML
    private TableColumn EyeglassesCol;
    @FXML
    private TableColumn Heavy_MakeupCol;
    @FXML
    private TableColumn SmilingCol;

    private Stage primaryStage;
    
	public void initialize(){
		group = new ToggleGroup();
		rb1.setToggleGroup(group);
        rb2.setToggleGroup(group);
        rb1.setUserData("m");
        rb2.setUserData("v");
        
        age_cb.setItems(FXCollections.observableArrayList("Young", "Old"));
        adtype_cb.setItems(FXCollections.observableArrayList("Technology", "Beautyproducts","Cooking","Bags"));
             
        //Data table
        table.setEditable(true);
        AdIDCol.setCellValueFactory(new PropertyValueFactory<Advertisement, String>("AdID"));
        AdTypeCol.setCellValueFactory(new PropertyValueFactory<Advertisement, String>("AdType"));
        AgeCol.setCellValueFactory(new PropertyValueFactory<Advertisement, String>("Age"));
        GenderCol.setCellValueFactory(new PropertyValueFactory<Advertisement, String>("Gender")); 
        ChubbyCol.setCellValueFactory(new PropertyValueFactory<Advertisement, String>("Chubby"));
        EyeglassesCol.setCellValueFactory(new PropertyValueFactory<Advertisement, String>("Eyeglasses"));
        Heavy_MakeupCol.setCellValueFactory(new PropertyValueFactory<Advertisement, String>("Heavy_Makeup"));
        SmilingCol.setCellValueFactory(new PropertyValueFactory<Advertisement, String>("Smiling"));
        
        initializedata();
        
        MulSearch();
        Detail();
        Page();
    }
    
    
    public void MulSearch(){
    	//AgeSearch
    	age_cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
        	public void changed(ObservableValue ov, Number value, Number new_value){
        		if(new_value.intValue()==0)
        			chosen_age="Young";
        		else if(new_value.intValue()==1)
        			chosen_age="Old";
        	}
        });
        //GenderSearch 
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){  
            public void changed(ObservableValue<? extends Toggle> ov,  
                Toggle old_toggle, Toggle new_toggle) {  
                    if(group.getSelectedToggle() != null) {  
                    	chosen_gender=group.getSelectedToggle().getUserData().toString(); 
                    }                 
                }  
        });

        //AdtypeSearch
        adtype_cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
        	public void changed(ObservableValue ov, Number value, Number new_value){
        		if(new_value.intValue()==0)
        			chosen_adtype="Technology";
        		else if(new_value.intValue()==1)
        			chosen_adtype="Beautyproducts";
        		else if(new_value.intValue()==2)
        			chosen_adtype="Cooking";
        		else if(new_value.intValue()==3)
        			chosen_adtype="Bags";
        		
        	}
        });
        searchbutton.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) {
    	    	if(idtext.getText()!=null){// && !idtext.getText().isEmpty()
    	        	if(!idtext.getText().isEmpty()){
    	        		chosen_adid=idtext.getText();
    	        	}
    	        }
    	    	if(chosen_age!=null||chosen_gender!=null||chosen_adid!=null||chosen_adtype!=null)
    	    	{
    	    		Query();
    	    		chosen_age=null;
    	    		chosen_gender=null;
    	    		chosen_adid=null;
    	    		chosen_adtype=null;
    	    		age_cb.setItems(FXCollections.observableArrayList("Young", "Old"));
    	    		rb1.setSelected(false);
        	    	rb2.setSelected(false);
        	    	adtype_cb.setItems(FXCollections.observableArrayList("Technology", "Beautyproducts","Cooking","Bags"));
        	    	idtext.clear();
        	    	DataInTable();
    	    	}
    	    	else
    	    	{
    	    		query = 0;
    	    		datasize=data.size();
    	    		pageIndex = 1;
    	    		DataInTable();
    	    	}
    	    	
    	    }
    	});
    }
   
    public void DataInTable(){
    	if(query == 0)
    		ShowPage(data);
    	else if(query == 1)
    		ShowPage(Querydata);
    }
    
    public int rowsPerPage() {
        return 6;
    }

    private void ShowPage(ObservableList data) {
        int fromIndex = pageIndex * rowsPerPage();
        int toIndex = Math.min(fromIndex + rowsPerPage(), data.size());
        if(fromIndex < toIndex)
        	table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
        else
        	table.setItems(null);
        curpage.setText(String.valueOf(pageIndex));
        tolpage.setText(String.valueOf(datasize / rowsPerPage()));
    }
    public void Page(){
    	previousbutton.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) { 
    	    	if(pageIndex > 1)
    	    	{
    	    		pageIndex --;
    	    		DataInTable();
    	    	}
    	    }  
    	});
    	nextbutton.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) {
    	    	if(query==0)
    	    		if((pageIndex < datasize / rowsPerPage() + 1))
        	    	{
        	    		pageIndex ++;
        	    		DataInTable();
        	    	}
    	    	if(query==1)
    	    		if((pageIndex < datasize / rowsPerPage()))
        	    	{
        	    		pageIndex ++;
        	    		DataInTable();
        	    	}
    	    	
    	    }  
    	});
    	if(pagetext.getText()!=null){// && !idtext.getText().isEmpty()
        	gotobutton.setOnAction(new EventHandler<ActionEvent>() {
        	    @Override public void handle(ActionEvent e) {
        	    	if(!pagetext.getText().isEmpty()){
        	    		pageIndex=Integer.valueOf(pagetext.getText()).intValue();
        	    		System.out.println("pageIndex:"+pageIndex);
        	    		pagetext.clear();
        	    		DataInTable();
        	    	}
        	    }
        	});
        }
    }
    
    public void Detail() {
    	//Show the chart
        detailbutton.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) { 
    	    	if(table.getSelectionModel().getSelectedIndex()!=-1){
    	    		Advertisement selected = table.getSelectionModel().getSelectedItem();
        	        select_adid = selected.getAdID();
        	        try {

						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/Chart.fxml"));
						fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
						    @Override
						    public Object call(Class<?> controllerClass) {
						        if (controllerClass == ChartController.class) {
						        	ChartController controller = new ChartController();
						            controller.setAdid(select_adid);
						            return controller ;
						        } else {
						            try {
						                return controllerClass.newInstance();
						            } catch (Exception exc) {
						                throw new RuntimeException(exc); // just bail
						            }
						        }
						    }
						});
        				AnchorPane chartPage = fxmlLoader.load();
        				BorderPane border = Main.getRoot();
        				border.setCenter(chartPage);

        			} catch (IOException exception) {
        				exception.printStackTrace();
        				System.out.println("SWITCH TO SEARCH PAGE ERROR!");
        			}
    	        }
    	    }  
    	});
    }
  
    
    static String sql = null;
    static Database db = null;
    static ResultSet ret = null; 

    public void initializedata(){
    	sql = "SELECT* FROM Advertisements,Measurements "
    			+ "WHERE Advertisements.AdvertisementID=Measurements.AdvertisementID";
    	
    	db = new Database(sql);
  
        try {  
            ret = db.pst.executeQuery();// Get the result
            		
            while (ret.next()) {  
                String adid = ret.getString("AdvertisementID");
                String adtype = ret.getString("Cathegorie");  
                String age = ret.getString("Age");  
                String gender = ret.getString("Gender");
                String chubby = ret.getString("Chubby");
                String eyeglasses = ret.getString("Glasses");
                String heavy_makeup = ret.getString("Makeup");
                String smiling = ret.getString("Smiling");
                if(ret.getInt("Age")==0) 
                	age="Young";
                else if(ret.getInt("Age")==1)
                	age="Old";
                if(gender.charAt(0)=='m')
                	gender="Male";
                else 
                	gender="Female";
                if(ret.getInt("Chubby")==1)
                	chubby="Yes";
                else
                	chubby="No";
                if(ret.getInt("Glasses")==1)
                	eyeglasses="Yes";
                else
                	eyeglasses="No";
                if(ret.getInt("Makeup")==1)   
                	heavy_makeup="Yes";
                else
                	heavy_makeup="No";
                if(ret.getInt("Smiling")==1)
                	smiling="Yes";
                else
                	smiling="No";
                data.add(new Advertisement(adid,adtype,age,gender,chubby,eyeglasses,heavy_makeup,smiling));
                //System.out.println(adid + "\t" + adtype + "\t" + age+ "\t" + gender + "\t" + chubby+ "\t" + eyeglasses+ "\t" + heavy_makeup+ "\t" + smiling );  
            }//Show Data
            datasize=data.size();
            ret.close();  
            db.close();//Close the connection
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
        ShowPage(data);
    }
   
    public void Query(){//ActionEvent event
    	query = 1;
    	pageIndex = 1;
    	int ageid=0;
    	if(chosen_age=="Young")
    		ageid=0;
    	else if(chosen_age=="Old")
    		ageid=1;
    	sql="SELECT * FROM Advertisements,Measurements "
    			+ "WHERE Advertisements.AdvertisementID=Measurements.AdvertisementID";

    	if(chosen_age!=null)
    		sql +=" AND Age="+ ageid;
    	if(chosen_gender!=null)
    		sql +=" AND Gender='" + chosen_gender + "'";
    	if(chosen_adid!=null)
    		sql +=" AND Measurements.AdvertisementID='" + chosen_adid + "'";
    	if(chosen_adtype!=null)
    		sql +=" AND Advertisements.Cathegorie='" + chosen_adtype + "'";
    	System.out.println(sql);
    	db = new Database(sql);

    	if(query == 1){
    		Querydata.clear();
      	  	
            try {  
                ret = db.pst.executeQuery();// Get the result
                		
                while (ret.next()) {  
                	String adid = ret.getString("AdvertisementID");
                    String adtype = ret.getString("Cathegorie");  
                    String age = ret.getString("Age");  
                    String gender = ret.getString("Gender");
                    String chubby = ret.getString("Chubby");
                    String eyeglasses = ret.getString("Glasses");
                    String heavy_makeup = ret.getString("Makeup");
                    String smiling = ret.getString("Smiling");
                    if(ret.getInt("Age")==0) 
                    	age="Young";
                    else if(ret.getInt("Age")==1)
                    	age="Old";
                    if(gender.charAt(0)=='m')
                    	gender="Male";
                    else 
                    	gender="Female";
                    if(ret.getInt("Chubby")==1)
                    	chubby="Yes";
                    else
                    	chubby="No";
                    if(ret.getInt("Glasses")==1)
                    	eyeglasses="Yes";
                    else
                    	eyeglasses="No";
                    if(ret.getInt("Makeup")==1)   
                    	heavy_makeup="Yes";
                    else
                    	heavy_makeup="No";
                    if(ret.getInt("Smiling")==1)
                    	smiling="Yes";
                    else
                    	smiling="No";
                    Querydata.add(new Advertisement(adid,adtype,age,gender,chubby,eyeglasses,heavy_makeup,smiling));
                    System.out.println(adid + "\t" + adtype + "\t" + age+ "\t" + gender + "\t" + chubby+ "\t" + eyeglasses+ "\t" + heavy_makeup+ "\t" + smiling );  
                }//Show Data
                datasize = Querydata.size();
                System.out.println("datasize:"+datasize);
                ret.close();  
                db.close();//Close the connection
            } catch (SQLException e) {  
                e.printStackTrace();  
            }
    	}
    	
    }
    

}
