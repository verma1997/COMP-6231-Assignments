package Core;

import java.net.*;
import java.io.*;

/**
 * Thread class of UDPServer used by ClinicServer to accept getCount request from other server
 */
public class UDPServer extends Thread{
	private CenterServant CenterObj; //reference the server who creates this UDP server thread
	public int UDPport;	//port to listen request
	
	//construction method
	public UDPServer (CenterServant Obj, int port){
		this.CenterObj = Obj;
		this.UDPport = port;
	}
	
	//Body of thread
	public void run(){ 
		DatagramSocket aSocket = null;
		try{    
			//Create socket and buffer
			aSocket = new DatagramSocket(UDPport); 
			byte[] buffer = new byte[1000];
			
			//loop infinitely
			while(true){     
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);     
				aSocket.receive(request); //listen to request
				//analyze request
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
				CenterObj.mylogfile.writeLog("["+CenterObj.serverLocation+" Server]: Received getCount("+recordType+") request from "+(new String(request.getData(),0,request.getLength())).substring(0,3)+" server and replied \""+result+"\"");
			}    
		} catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());   
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if(aSocket != null) aSocket.close();
		}
	} 
} 
