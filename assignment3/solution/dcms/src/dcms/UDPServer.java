package dcms;

import java.net.*;
import java.io.*;

/**
 * Thread class of UDPServer used by CenterServer to accept getCount request from other servers
 */
public class UDPServer extends Thread {
    private CenterServer CenterObj; // Reference the server who creates this UDP server thread
    public int UDPPort;	// Port to listen request

    // Construction method
    public UDPServer (CenterServer Obj, int port){
        this.CenterObj = Obj;
        this.UDPPort = port;
    }

    // Body of thread
    @Override
    public void run(){
        DatagramSocket aSocket = null;
        try{
            //Create socket and buffer
            aSocket = new DatagramSocket(UDPPort);
            byte[] buffer = new byte[1000];

            //loop infinitely
            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request); // Listen to request
                // Analyze request
                DatagramPacket reply;
                String result;
                String recordType = (new String(request.getData(),0,request.getLength())).substring(3,5);
                if ((new String(request.getData(),0,request.getLength())).substring(5).equals("getCount")){
                    //right request
                    result = CenterObj.getMemberCount(recordType);
                    reply = new DatagramPacket(result.getBytes(),result.length(), request.getAddress(), request.getPort());
                }
                else{
                    //invalid request
                    result = "Invalid request";
                    reply = new DatagramPacket(result.getBytes(),result.length(), request.getAddress(), request.getPort());
                }
                //send result
                aSocket.send(reply);
                CenterObj.logFile.writeLog("[" + CenterObj.serverLocation + " Server]: Received getCount(" + recordType + ") request from " + (new String(request.getData(),0,request.getLength())).substring(0,3) + " server and replied \"" + result + "\"");
            }
        } catch (SocketException e){
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if(aSocket != null) {
                aSocket.close();
            }
        }
    }
}
