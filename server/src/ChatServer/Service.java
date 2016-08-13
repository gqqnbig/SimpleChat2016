package ChatServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

public class Service implements Runnable{
	private Socket socket;
    private BufferedReader in = null;

    
    ArrayList<String> onlineID=new ArrayList<String>();
	Connection conn;
	String driver="com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/simplechat";/*NEED TO BE CHANGE!*/
	String user = "root";
	String password = "123456";

	
	public Service(Socket client){
		this.socket=client;
		try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          
        } catch (IOException e) {
        	System.out.println("Socket Failed");
            e.printStackTrace();
        }
	}

	@Override
	public void run() {
		String first=null;
		try {
			first = in.readLine();
			System.out.println(first);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(first.equals("##11")){
			//send()
			send();
			
		}else if(first.equals("##22")){
			
			//get()
			get();
			System.out.println("get start");
			
		}else if(first.equals("##33")){
			//getContact()
			getContact();
			
		}else if(first.equals("##44")){
			//received()
			received();
			
		}else if (first.equals("##55")){
			addContact();
			
		}else {System.out.println("Wrong CMD Mesg!");
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}return;} 
  }
		  
        
	
	
	private void send(){
		
		String senderID=null;
		String receiverID=null;
		String message=null;
		try {
			senderID =in.readLine();
			receiverID=in.readLine(); 
			 message=in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(senderID==null||receiverID==null){
			System.out.println("lost mesgs#1send");
			return ;
		}
		if(message==null)message="";
		
		long timeStamp =System.currentTimeMillis()/1000L;
		try{
			 Class.forName(driver);
			 conn=DriverManager.getConnection(url,user,password);//connect db
		           PreparedStatement psql=conn.prepareStatement("INSERT INTO conversation VALUE(?,?,?,?,?)");
	               psql.setNull(1, Types.LONGVARBINARY);
		           psql.setString(2,senderID);
		           psql.setString(3,receiverID);
		           psql.setLong(4,timeStamp);
		           psql.setString(5,message);
		           psql.executeUpdate();

	         }catch (SQLException e) {
		        	e.printStackTrace();
		     } catch (ClassNotFoundException e){
				e.printStackTrace();
			}
	}
	
	private void get(){
		String receiverID=null;
		String senderID=null;
		System.out.println("get start*");
		try {
			receiverID=in.readLine();
			senderID=in.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(receiverID==null||senderID==null){
			System.out.println("lost mesgs#2get");
			return ;
		}
		long SCID=-1;
		String SSID=null;
		long Stime=-1;
		String Smessage=null;
		try{
			Class.forName(driver);
			conn=DriverManager.getConnection(url,user,password);
			Statement statement=conn.createStatement();
			String sql="SELECT conversationID,senderID,time,message FROM conversation WHERE conversationID IN (SELECT MIN(conversationID) FROM conversation WHERE time IN (SELECT MIN(time) FROM conversation WHERE receiverID ="+receiverID+" AND senderID = "+senderID+"))" ;	        
			ResultSet rs=statement.executeQuery(sql);
			rs.next();
	        SCID=rs.getLong("conversationID");
	        SSID=rs.getString("senderID");
	        Stime=rs.getLong("time");
	        Smessage=rs.getString("message");
	        
	        
	        System.out.println("get start**");
			 }catch (SQLException e) {
	           System.out.println("The record can't be found ");
	           try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		     } catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		if(SCID==-1||SSID==null||Stime==-1||Smessage==null){
			System.out.println("Wrong data in database#2get");
			
			return;
		}
		
		PrintWriter pout = null;
        try {
        	System.out.println("get start***");
            pout = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),true);
            pout.println(""+SCID);
            pout.println(SSID);
            pout.println(""+Stime);
            pout.println(Smessage);
            pout.flush();
            pout.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        try {
         Thread.sleep(5000);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
		
		
	}
	
	
    private void getContact(){
    	String host=null;
    	try {
			host=in.readLine();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	String hostID=null;
    	String guestID=null;
    	if(host==null){
    		System.out.println("Lost Mesg #3getContact");
    		return ;
    	}
    	
    	PrintWriter pout = null;
    	ResultSet rs=null;
			try {
				Class.forName(driver);
				conn=DriverManager.getConnection(url,user,password);
				Statement statement=conn.createStatement();
				String sql="SELECT * FROM contact WHERE host = " +host;	        
				System.out.println("get start***");
	            pout = new PrintWriter(new BufferedWriter(
	                    new OutputStreamWriter(socket.getOutputStream())),true);
				
				 rs=statement.executeQuery(sql);
				 int count=0;
				 if(rs!=null){
				 while(rs.next()){
					 
					 System.out.println(count++);
						hostID=rs.getString("host");
						guestID=rs.getString("guest");
						pout.println(hostID);
						pout.println(guestID);
					}
				 }
				 
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e) {
				pout.println("##33");
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			
			pout.println("##33");

	
    }
    
    
    private void received(){
    	String conversationID=null;
    	
    	try {
    		conversationID=in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	if(conversationID==null){
    		System.out.println("Lost Mesg #4Received");
    		return ;
    	}
    	try{
    	    Class.forName(driver);
    		conn=DriverManager.getConnection(url,user,password);
    		Statement statement=conn.createStatement();
    		String sql="DELETE FROM conversation WHERE conversationID = " +conversationID;
    		statement.executeUpdate(sql);
    		conn.close();
    		 }catch (SQLException e) {
    	        	e.printStackTrace();
    	     } catch (ClassNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	
    }
    
    public void addContact(){//##55
         String host=null;
         String guest=null;
    	
    	try {
    		host=in.readLine();
    		guest=in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	if(host==null||guest==null){
    		System.out.println("Lost Mesg #5addContact");
    		return ;
    	}
    	try{
			 Class.forName(driver);
			 conn=DriverManager.getConnection(url,user,password);//connect db
		           PreparedStatement psql=conn.prepareStatement("INSERT INTO contact VALUE(?,?)");
		           psql.setString(1,host);
		           psql.setString(2,guest);
		           psql.executeUpdate();

	         }catch (SQLException e) {
		        	e.printStackTrace();
		     } catch (ClassNotFoundException e){
				e.printStackTrace();
			}
    	
    	
    	
    	
    	
    }
    
    private int checkOnlineList(String ID){
	    int index=0;
	    
		for(String List:onlineID){
		   String LID=List.substring(0,15);
		   if(LID.equals(ID))return index; 
		   index++;
	   }
		return -1;
	}
}
