package lab7out;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Database
{
  private Connection conn;
  private Statement stmt;
  private ResultSet rs;
  //private ResultSetMetaData rmd;
  private ArrayList<String> list;
  
  public Database() throws IOException
  {
	  Properties prop = new Properties();
	  FileInputStream fis = new FileInputStream("lab7out/db.properties");
	  prop.load(fis);
	  String url = prop.getProperty("url");
	  String user = prop.getProperty("user");
	  String pass = prop.getProperty("password");    
	    
	    
	  try
	  {
	    conn = DriverManager.getConnection(url,user,pass);
	  } 
	  catch (SQLException e)
	  {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	  } 
	
	
  }
  
  public ArrayList<String> query(String query)
  {
	  try
	    {
	      //Read the connection properties as Strings
	    	    
	      //Create a statement
		  System.out.println("er1");
	      stmt=conn.createStatement();  
	      System.out.println("er2");
	      //Execute a query
	      rs=stmt.executeQuery(query);  
	      System.out.println("er3");
	      //Get metadata about the query
	      /*rmd = rs.getMetaData();
	      
	      //Get the # of columns
	      int no_columns = rmd.getColumnCount();
	    
	      //Get a column name
	      String name = rmd.getColumnName(1);*/
	      
	      //Fetch each row (use numeric numbering
	      while(rs.next()) 
	      {
	    	String record;
	        record = rs.getString(1)+","+rs.getString(2);
	        
	        list.add(record);
	      }
	      System.out.println("er4");
	      conn.close();  
	    
	      System.out.println("Success");
	      return list;
	    } 
	    catch (SQLException e)
	    {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	      return null;
	    }
  }
  
  public void executeDML(String dml) throws SQLException
  {
	//Create a statement
    stmt=conn.createStatement();  
      
    //Execute a DML statement
    stmt.execute(dml);
  }
  
  
  public boolean verifyAccount(String username, String password)
  {    
	  String st = "select username from user where username='"+username+"'";
	  System.out.println("er8");
	  ArrayList<String> r = query(st);
	  System.out.println("er9");
	  String str = "select aes_decrypt(password,'key') from user where username='"+username+"'";
	  ArrayList<String> r2 = query(str);
	  System.out.println("er10");
	
    // Stop if this account doesn't exist.
    if (r == null)
      return false;
    
    // Check the username and password.
    if (password.equals(r2))
      return true;
    else
      return false;
  }

  // Method for creating a new account.
  public boolean createNewAccount(String username, String password)
  {    
	String st = "select username from user where username='"+username+"'";
	
	ArrayList<String> r = query(st);
	
    // Stop if this account already exists.
    if (r != null)
      return false;
    
    // Add the new account.
    String str = "insert into user values('"+username+"',aes_encrypt('"+password+"','key'))";
    try {
		executeDML(str);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	}
    
    return true;
  }
  
  
}

