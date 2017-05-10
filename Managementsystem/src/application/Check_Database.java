/*package application;

import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.SQLException; 

//make the connection between the app and the server.
public class Check_Database 
{
	public static final String url = "jdbc:mysql://172.16.19.252/sadb";  
    public static final String name = "com.mysql.jdbc.Driver";  
    public static final String user = "server";  
    public static final String password = "1234";  
  
    public Connection conn = null;  
    public PreparedStatement pst = null;  
  
    //try to log in with the login information from the field.
    public Check_Database (String sql)
    {  
        try
        {  
            Class.forName (name);
            conn = DriverManager.getConnection (url, user, password);
            pst = conn.prepareStatement (sql);
        } 
        
        catch (Exception e) 
        { e.printStackTrace(); }  
    }  
  
    //make sure the connection will be closed after the check.
    public void close () 
    {  
        try 
        {  
            this.conn.close();  
            this.pst.close();  
        } 
        
        catch (SQLException e) 
        { e.printStackTrace(); }  
    }
}
*/