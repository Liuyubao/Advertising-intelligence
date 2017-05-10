package application;

import java.sql.ResultSet;
import java.sql.SQLException;
 
public class Check_Advertisement
{
    static String sql = null;
    static Database db = null;
    static ResultSet ret = null; 
    public String adid = "";
    public String msid = "";
    
    //some SQL code to get the latest value
    public void initializedata()
    {
    	sql = " SELECT MeasurementID, AdvertisementID" +
    		  " FROM sadb.Measurements " + 
    		  " ORDER BY MeasurementID DESC LIMIT 1 ";
    	
    	db = new Database (sql);
  
    	//get MeasurementID and AdvertisementID
        try 
        {  
            ret = db.pst.executeQuery();
            		
            while (ret.next())  
            { 
            	msid = ret.getString ("MeasurementID");
        		System.out.println("MeasurementID: " + msid);
        		
            	adid = ret.getString ("AdvertisementID");
        		System.out.println("AdvertisementID: " + adid);
            }
            
            ret.close();  
            db.close();
        } 
        
        catch (SQLException e) 
        {  e.printStackTrace(); }
    }
    
    //define setters and getters, easy to get the value in another class.
    public String getAdid() {
    	return this.adid;
    }
    
    public void setAdid(String adid) {
    	this.adid = adid;
    }
    
    public String getMsid() {
    	return this.msid;
    }
    
    public void setMdid(String msid) {
    	this.msid = msid;
    }
} 