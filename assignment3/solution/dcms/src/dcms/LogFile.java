package dcms;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * LogFile class used by both server and client
 */
public class LogFile {
    private BufferedWriter out;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Construction method
    public LogFile (String fileName){
        try {
            out = new BufferedWriter(new FileWriter(fileName));
        }catch (Exception e){
            System.out.println("Logfile open failed.");
        }
    }
    // Destruction method
    @Override
    protected void finalize(){
        try{
            out.close();
        }catch (Exception e){
            System.out.println("Logfile close failed.");
        }
    }

    // Simple method to synchronized write log, only add date and time before the message
    public synchronized String  writeLog(String msg){
        try{
            out.write(df.format(new Date()) + ": " + msg + "\r\n");
            out.flush();
        }catch (Exception e){
            System.out.println("Write to logfile failed.");
        }
        return(msg);
    }
}
