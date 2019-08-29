package Core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * logFile class used by both server and client
 */
public class logFile {
    private BufferedWriter out;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //Construction method
    public logFile (String fileName){
        try {
            out = new BufferedWriter(new FileWriter(fileName));
        }catch (Exception e){
            System.out.println("Logfile open failed.");
        }
    }

    @Override
    protected void finalize(){
        try{
            out.close();
        }catch (Exception e){
            System.out.println("Logfile close failed.");
        }
    }

    //simple method to synchronized write log, only add date and time before the message
    public synchronized String  writeLog(String msg){
        try{
            out.write(df.format(new Date())+": "+msg+"\r\n");
            out.flush();
        }catch (Exception e){
            System.out.println("Write to logfile failed.");
        }
        return(msg);
    }
}
