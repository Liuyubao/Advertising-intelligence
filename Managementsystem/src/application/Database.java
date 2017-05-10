package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
	public static final String url = "jdbc:mysql://172.16.19.252/sadb?autoReconnect=true&useSSL=false";  
    public static final String name = "com.mysql.jdbc.Driver";  
    public static final String user = "StatisticsRetrievement";  
    public static final String password = "1234";  
  
    public Connection conn = null;  
    public PreparedStatement pst = null;  
  
    public Database(String sql) {  
        try {  
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);
            pst = conn.prepareStatement(sql);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void close() {  
        try {  
            this.conn.close();  
            this.pst.close();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }
}
