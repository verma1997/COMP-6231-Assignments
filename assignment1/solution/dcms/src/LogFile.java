import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

//logFile class used by both server and client 
public class LogFile {
	private BufferedWriter out;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd E HH:mm:ss");
	
	//Construction method
	public LogFile (String fileName) {
		try {
			out = new BufferedWriter(new FileWriter(fileName));
		} catch (Exception e) {
            System.out.println("Failed to open the logfile.");
		}	
	}

	//Destruction method
	@Override
	protected void finalize(){
	     try{
	    	 out.close();
	     }catch (Exception e){
	    	 System.out.println("Failed to close the logfile.");
	     }	
	}
	
	//simple method to synchronized write log, only add date and time before the message 
	public synchronized String  writeLog(String msg) {
		try{
			out.write(df.format(new Date()) + ": " + msg + "\r\n");
	    	out.flush(); 
	    }catch (Exception e){
	    	 System.out.println("Failed to write data into the logfile.");
	    }	
		return(msg);
	}
}
