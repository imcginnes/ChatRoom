package lab7out;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class ChatServer extends AbstractServer
{
  // Data fields for this chat server.
  private JTextArea log;
  private JLabel status;
  private boolean running = false;
  //private DatabaseFile database1 = new DatabaseFile();
  private Database database;

  // Constructor for initializing the server with default settings.
  public ChatServer()
  {
    super(12345);
    this.setTimeout(500);
  }
  
  void setDatabase(Database database) throws IOException
  {
	  database = new Database();
  }
  
  /*void setResult(String type, String command)
  {
	// Execute a query if Q was specified.
	    if (type.equals("Q"))
	    {
	      // Do the query.
	      ArrayList<String> result = database.query(command);
	     
	      // Print the result.
	      if (result != null)
	      {
	        for (String row : result)
	        {
	          System.out.println(row);
	        }
	      }
	      else
	      {
	        System.out.println("Error executing query.");
	      }
	    }
	    
	    // Execute DML if D was specified.
	    else if (type.equals("D"))
	    {
	      // Run the DML.
	      try
	      {
	         database.executeDML(command);
	       }
	       catch(SQLException sql)
	       {
	           System.out.println("Error executing DML.");
	        }

	     }
  }*/

  // Getter that returns whether the server is currently running.
  public boolean isRunning()
  {
    return running;
  }
  
  // Setters for the data fields corresponding to the GUI elements.
  public void setLog(JTextArea log)
  {
    this.log = log;
  }
  public void setStatus(JLabel status)
  {
    this.status = status;
  }

  // When the server starts, update the GUI.
  public void serverStarted()
  {
    running = true;
    status.setText("Listening");
    status.setForeground(Color.GREEN);
    log.append("Server started\n");
  }
  
  // When the server stops listening, update the GUI.
   public void serverStopped()
   {
     status.setText("Stopped");
     status.setForeground(Color.RED);
     log.append("Server stopped accepting new clients - press Listen to start accepting new clients\n");
   }
 
  // When the server closes completely, update the GUI.
  public void serverClosed()
  {
    running = false;
    status.setText("Close");
    status.setForeground(Color.RED);
    log.append("Server and all current clients are closed - press Listen to restart\n");
  }

  // When a client connects or disconnects, display a message in the log.
  public void clientConnected(ConnectionToClient client)
  {
    log.append("Client " + client.getId() + " connected\n");
  }

  // When a message is received from a client, handle it.
  public void handleMessageFromClient(Object arg0, ConnectionToClient arg1)
  {
	try {
		setDatabase(database);
		log.append("database initialized\n");
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		log.append("failed to set database\n");
	}
	
    // If we received LoginData, verify the account information.
    if (arg0 instanceof LoginData)
    {
      log.append("login requested by user\n");
      // Check the username and password with the database.
      LoginData data = (LoginData)arg0;
      Object result;
      String gu = data.getUsername();
      String gp = data.getPassword();
      System.out.println(gu+"\n"+gp);
      boolean va = database.verifyAccount(gu, gp);
      System.out.println("er7");
      if (va==true)
      {
        result = "LoginSuccessful";
        log.append("Client " + arg1.getId() + " successfully logged in as " + data.getUsername() + "\n");
      }
      else
      {
        result = new Error("The username and password are incorrect.", "Login");
        log.append("Client " + arg1.getId() + " failed to log in\n");
      }
      log.append("here6\n");
      // Send the result to the client.
      try
      {
        arg1.sendToClient(result);
        log.append("here3\n");
      }
      catch (IOException e)
      {
    	log.append("here2\n");
        return;
      }
    }
    
    // If we received CreateAccountData, create a new account.
    else if (arg0 instanceof CreateAccountData)
    {
      // Try to create the account.
      CreateAccountData data = (CreateAccountData)arg0;
      Object result;
      if (database.createNewAccount(data.getUsername(), data.getPassword()))
      {
        result = "CreateAccountSuccessful";
        log.append("Client " + arg1.getId() + " created a new account called " + data.getUsername() + "\n");
      }
      else
      {
        result = new Error("The username is already in use.", "CreateAccount");
        log.append("Client " + arg1.getId() + " failed to create a new account\n");
      }
      
      // Send the result to the client.
      try
      {
        arg1.sendToClient(result);
        log.append("here4\n");
      }
      catch (IOException e)
      {
    	log.append("here\n");
        return;
      }
    }
  }

  // Method that handles listening exceptions by displaying exception information.
  public void listeningException(Throwable exception) 
  {
    running = false;
    status.setText("Exception occurred while listening");
    status.setForeground(Color.RED);
    log.append("Listening exception: " + exception.getMessage() + "\n");
    log.append("Press Listen to restart server\n");
  }
}
