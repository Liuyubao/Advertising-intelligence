package application;

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
import javafx.geometry.Insets;
import javafx.scene.Group;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ChartController {
	private String select_adid;
    private String chosen_v;
    private String chosen_h;

    final XYChart.Series<String, Integer> series_age = new XYChart.Series<String, Integer>();
    final XYChart.Series<String, Integer> series_gender = new XYChart.Series<String, Integer>();
    final XYChart.Series<String, Integer> series_chubby = new XYChart.Series<String, Integer>();
    final XYChart.Series<String, Integer> series_eyeglasses = new XYChart.Series<String, Integer>();
    final XYChart.Series<String, Integer> series_heavymakeup = new XYChart.Series<String, Integer>();
    final XYChart.Series<String, Integer> series_smiling = new XYChart.Series<String, Integer>();
    ObservableList<String> AgeName = FXCollections.observableArrayList("Young","Old");
    ObservableList<String> GenderName = FXCollections.observableArrayList("Female","Male");
    ObservableList<String> ChubbyName = FXCollections.observableArrayList("Chubby","Not Chubby");
    ObservableList<String> EyeGlassesName = FXCollections.observableArrayList("EyeGlasses","No EyeGlasses");
    ObservableList<String> HeavyMakeupName = FXCollections.observableArrayList("HeavyMakeup","No HeavyMakeup");
    ObservableList<String> SmilingName = FXCollections.observableArrayList("Smiling","No Smiling");
   

    @FXML
    private Label adidlabel; 
    @FXML
    private Label adnamelabel;
    @FXML
    private Label companylabel;
    @FXML
    private Label adlengthlabel;

    @FXML
    private BarChart<String, Integer> chart;
    @FXML
    private BarChart<String, Integer> chart_smiling;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis; 
    //@FXML
    private ToggleGroup group_v;
    private ToggleGroup group_h;
    @FXML
    private RadioButton rb_number;
    @FXML
    private RadioButton rb_age;
    @FXML
    private RadioButton rb_gender;
    @FXML
    private RadioButton rb_chubby;
    @FXML
    private RadioButton rb_eyeglasses;
    @FXML
    private RadioButton rb_heavymakeup;
    @FXML
    private RadioButton rb_smiling;
    @FXML
    private Button detailbutton;
    @FXML
    private Button showchartbutton;


	public void initialize(){
		group_v = new ToggleGroup();
        group_h = new ToggleGroup();
		rb_number.setToggleGroup(group_v);
        rb_age.setToggleGroup(group_h);
        rb_gender.setToggleGroup(group_h);
        rb_chubby.setToggleGroup(group_h);
        rb_eyeglasses.setToggleGroup(group_h);
        rb_heavymakeup.setToggleGroup(group_h);
        rb_smiling.setToggleGroup(group_h);
        
        rb_number.setUserData("number");
        rb_age.setUserData("age");
        rb_gender.setUserData("gender");
        rb_chubby.setUserData("chubby");
        rb_eyeglasses.setUserData("eyeglasses");
        rb_heavymakeup.setUserData("heavymakeup");
        rb_smiling.setUserData("smiling");
        
        //xAxis = new CategoryAxis();
        //yAxis = new NumberAxis();
        series_age.setName("Age");
        series_gender.setName("Gender");
        series_chubby.setName("Chubby");
        series_eyeglasses.setName("Eyeglasses");
        series_heavymakeup.setName("Heavymakeup");
        series_smiling.setName("Smiling");
        chart.setCategoryGap(150);
        
        showdetail();
        chose_attributes();
    }
	
	public void setAdid(String adid) {
		select_adid = adid;
	}
	   
	static String sql = null;
    static Database db = null;
    static ResultSet ret = null;
    
    public void showdetail()
	{
    	//AdID
    	adidlabel.setText(select_adid);
    	//ProductName
    	sql="select ProductName from Advertisements where AdvertisementID = " + select_adid;
        db = new Database(sql);
        try {  
            ret = db.pst.executeQuery();// Get the result
            		
            while (ret.next()) {  
                adnamelabel.setText(ret.getString(1));
          
            }//Show Data
            ret.close();  
            db.close();//Close the connection
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
        //Company
        sql="select Company from Advertisements where AdvertisementID = " + select_adid;
        db = new Database(sql);
        try {  
            ret = db.pst.executeQuery();// Get the result
            		
            while (ret.next()) {  
                companylabel.setText(ret.getString(1));
          
            }//Show Data
            ret.close();  
            db.close();//Close the connection
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
        //AdLength
        
        
        
	}
    
    public void chose_attributes()
    {
    	group_v.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){  
            public void changed(ObservableValue<? extends Toggle> ov,  
                Toggle old_toggle, Toggle new_toggle) {  
                    if(group_v.getSelectedToggle() != null) {  
                    	chosen_v=group_v.getSelectedToggle().getUserData().toString();
                    	}
                    }
            });
    	
    	group_h.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){  
            public void changed(ObservableValue<? extends Toggle> ov,  
                Toggle old_toggle, Toggle new_toggle) {  
                    if(group_h.getSelectedToggle() != null) {
                    	chosen_h=group_h.getSelectedToggle().getUserData().toString();
                    	}
                    }
            });
    	showchartbutton.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) {
    	    	group_v.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){  
    	            public void changed(ObservableValue<? extends Toggle> ov,  
    	                Toggle old_toggle, Toggle new_toggle) {  
    	                    if(group_v.getSelectedToggle() != null) {  
    	                    	chosen_v=group_v.getSelectedToggle().getUserData().toString();
    	                    	}
    	                    }
    	            });
    	    	
    	    	group_h.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){  
    	            public void changed(ObservableValue<? extends Toggle> ov,  
    	                Toggle old_toggle, Toggle new_toggle) {  
    	                    if(group_h.getSelectedToggle() != null) {
    	                    	chosen_h=group_h.getSelectedToggle().getUserData().toString();
    	                    	}
    	                    }
    	            });
    	    	//System.out.println(chosen_v+","+chosen_h);
    	    	
    	    	if(chosen_v=="number")
    	    	{
    	    		if(chosen_h=="age")
        	    		Draw_number_age();
        	    	else if(chosen_h=="gender")
        	    		Draw_number_gender();
        	    	else if(chosen_h=="chubby")
        	    		Draw_number_chubby();
        	    	else if(chosen_h=="eyeglasses")
        	    		Draw_number_eyeglasses();
        	    	else if(chosen_h=="heavymakeup")
        	    		Draw_number_heavymakeup();
        	    	else if(chosen_h=="smiling")
        	    		Draw_number_smiling();
    	    	}
    	    		 
    	    	rb_number.setSelected(false);
    	        rb_age.setSelected(false);
    	        rb_gender.setSelected(false);
    	        rb_chubby.setSelected(false);
    	        rb_eyeglasses.setSelected(false);
    	        rb_heavymakeup.setSelected(false);
    	        rb_smiling.setSelected(false);
    	    	chosen_v=null;
    	    	chosen_h=null;
    	    }
    	});
    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
  
    public void Draw_number_age(){
    	int num_y = 0, num_o = 0;
    	chart.getData().clear();
    	
        //Counting the number of people in different age watching the same advertisement
        sql="select count(*) from Measurements where Age='1' AND AdvertisementID = " + select_adid;
        db = new Database(sql);
		
        try {  
            ret = db.pst.executeQuery();// Get the result
            		
            while (ret.next()) {  
                num_y = ret.getInt(1);
            }//Show Data
            ret.close();  
            db.close();//Close the connection
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
        
        sql="select count(*) from Measurements where Age='2' AND AdvertisementID = " + select_adid;
        db = new Database(sql);
		
        try {  
            ret = db.pst.executeQuery();// Get the result
            		
            while (ret.next()) {  
                num_o = ret.getInt(1);
            }//Show Data
            ret.close();  
            db.close();//Close the connection
        } catch (SQLException e) {  
            e.printStackTrace();  
        }

        //xAxis.setLabel("Age");
        xAxis.setCategories(AgeName);
        yAxis.setLabel("Number");
        series_age.getData().add(new XYChart.Data<String, Integer>("Young", num_y));
        series_age.getData().add(new XYChart.Data<String, Integer>("Old", num_o));
        chart.getData().clear();
        chart.getData().add(series_age);
    }
    
    public void Draw_number_gender(){  
    	int num_f = 0, num_m = 0;
    	chart.getData().clear();
    	
        //Counting the number of people in different gender watching the same advertisement
        sql="select count(*) from Measurements where gender='m' AND AdvertisementID = " + select_adid;
        db = new Database(sql);
		
        try {  
            ret = db.pst.executeQuery();// Get the result
            		
            while (ret.next()) {  
                num_f = ret.getInt(1);
            }//Show Data
            ret.close();  
            db.close();//Close the connection
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
        
        sql="select count(*) from Measurements where gender='v' AND AdvertisementID = " + select_adid;
        db = new Database(sql);
		
        try {  
            ret = db.pst.executeQuery();// Get the result
            		
            while (ret.next()) {  
                num_m = ret.getInt(1);
            }//Show Data
            ret.close();  
            db.close();//Close the connection
        } catch (SQLException e) {  
            e.printStackTrace();  
        }

        //xAxis.setLabel("Gender");
        xAxis.setCategories(GenderName);
        chart.setCategoryGap(120);
        yAxis.setLabel("Number");
        series_gender.getData().add(new XYChart.Data<String, Integer>("Female", num_f));
        series_gender.getData().add(new XYChart.Data<String, Integer>("Male", num_m));
        chart.getData().clear();
        chart.getData().addAll(series_gender);
    }

    public void Draw_number_chubby(){                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
        int num_nc = 0, num_c = 0;
        chart.getData().clear();
        
        sql="select count(*) from Measurements where chubby=NUll AND AdvertisementID = " + select_adid;
        db = new Database(sql);
		
        try {  
            ret = db.pst.executeQuery();// Get the result
            		
            while (ret.next()) {  
                num_nc = ret.getInt(1);
            }//Show Data
            ret.close();  
            db.close();//Close the connection
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
        
        sql="select count(*) from Measurements where chubby=1 AND AdvertisementID = " + select_adid;
        db = new Database(sql);
		
        try {  
            ret = db.pst.executeQuery();// Get the result
            		
            while (ret.next()) {  
                num_c = ret.getInt(1);
            }//Show Data
            ret.close();  
            db.close();//Close the connection
        } catch (SQLException e) {  
            e.printStackTrace();  
        }

        //xAxis.setLabel("Chubby");
        xAxis.setCategories(ChubbyName);
        chart.setCategoryGap(120);
        yAxis.setLabel("Number");
        series_chubby.getData().add(new XYChart.Data<String, Integer>("Chubby", num_nc));
        series_chubby.getData().add(new XYChart.Data<String, Integer>("Not Chubby", num_c));
        chart.getData().addAll(series_chubby);
    }
    
    public void Draw_number_eyeglasses(){                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
        int num_ng=0;
        int num_g=0;
        chart.getData().clear();
        
        sql="select count(*) from Measurements where Glasses=NUll AND AdvertisementID = " + select_adid;
        db = new Database(sql);
		
        try {  
            ret = db.pst.executeQuery();// Get the result
            		
            while (ret.next()) {  
                num_ng = ret.getInt(1);
            }//Show Data
            ret.close();  
            db.close();//Close the connection
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
        
        sql="select count(*) from Measurements where Glasses=1 AND AdvertisementID = " + select_adid;
        db = new Database(sql);
		
        try {  
            ret = db.pst.executeQuery();// Get the result
            		
            while (ret.next()) {  
                num_g = ret.getInt(1);
            }//Show Data
            ret.close();  
            db.close();//Close the connection
        } catch (SQLException e) {  
            e.printStackTrace();  
        }

        //xAxis.setLabel("EyeGlasses");
        xAxis.setCategories(EyeGlassesName);
        chart.setCategoryGap(120);
        yAxis.setLabel("Number");
        series_eyeglasses.getData().add(new XYChart.Data<String, Integer>("EyeGlasses", num_ng));
        series_eyeglasses.getData().add(new XYChart.Data<String, Integer>("No EyeGlasses", num_g));
        chart.getData().addAll(series_eyeglasses);
    }
    
    public void Draw_number_heavymakeup(){                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
        int num_nh=0;
        int num_h=0;
        chart.getData().clear();
        
        sql="select count(*) from Measurements where Makeup=NUll AND AdvertisementID = " + select_adid;
        db = new Database(sql);
		
        try {  
            ret = db.pst.executeQuery();// Get the result
            		
            while (ret.next()) {  
                num_nh = ret.getInt(1);
            }//Show Data
            ret.close();  
            db.close();//Close the connection
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
        
        sql="select count(*) from Measurements where Makeup=1 AND AdvertisementID = " + select_adid;
        db = new Database(sql);
		
        try {  
            ret = db.pst.executeQuery();// Get the result
            		
            while (ret.next()) {  
                num_h = ret.getInt(1);
            }//Show Data
            ret.close();  
            db.close();//Close the connection
        } catch (SQLException e) {  
            e.printStackTrace();  
        }

        //xAxis.setLabel("HeavyMakeup");
        xAxis.setCategories(HeavyMakeupName);
        yAxis.setLabel("Number");
        series_heavymakeup.getData().add(new XYChart.Data<String, Integer>("HeavyMakeup", num_nh));
        series_heavymakeup.getData().add(new XYChart.Data<String, Integer>("No HeavyMakeup", num_h));
        chart.getData().addAll(series_heavymakeup);
    }
    
    public void Draw_number_smiling(){                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
        int num_ns=0;
        int num_s=0;
        chart.getData().clear();
        
        sql="select count(*) from Measurements where Glasses=NUll AND AdvertisementID = " + select_adid;
        db = new Database(sql);
		
        try {  
            ret = db.pst.executeQuery();// Get the result
            		
            while (ret.next()) {  
                num_ns = ret.getInt(1);
            }//Show Data
            ret.close();  
            db.close();//Close the connection
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
        
        sql="select count(*) from Measurements where Glasses=1 AND AdvertisementID = " + select_adid;
        db = new Database(sql);
		
        try {  
            ret = db.pst.executeQuery();// Get the result
            		
            while (ret.next()) {  
                num_s = ret.getInt(1);
            }//Show Data
            ret.close();  
            db.close();//Close the connection
        } catch (SQLException e) {  
            e.printStackTrace();  
        }

        //xAxis.setLabel("Smiling");
        xAxis.setCategories(SmilingName);
        yAxis.setLabel("Number");
        series_smiling.getData().add(new XYChart.Data<String, Integer>("Smiling", num_ns));
        series_smiling.getData().add(new XYChart.Data<String, Integer>("No Smiling", num_s));
        chart.getData().addAll(series_smiling);
    }
}
