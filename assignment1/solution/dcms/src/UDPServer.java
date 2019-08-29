import java.net.*;
import java.io.*; 

//Thread class of UDPServer used by CenterServer to accept getCount request from other servers
public class UDPServer extends Thread {
	private CenterServer classObject; //reference the server who creates this UDP server thread
	public int udpPort;	//port to listen request
	
	//construction method
	public UDPServer(CenterServer Obj, int port) {
		this.classObject = Obj;
		this.udpPort = port;
	}
	
	//Body of thread
	public void run() {
		DatagramSocket aSocket = null;    
		try{    
			//Create socket and buffer
			aSocket = new DatagramSocket(udpPort);
			byte[] buffer = new byte[1000];
			
			//loop infinitely
			while(true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);     
				aSocket.receive(request); //listen to request
				//analyze request
				DatagramPacket reply;
				String result;
				String recordType = (new String(request.getData(),0,request.getLength())).substring(3,5);
				if ((new String(request.getData(),0,request.getLength())).substring(5).equals("getCount")) {
					//right request
					result = classObject.getMemberCount(recordType);
					reply = new DatagramPacket(result.getBytes(),result.length(), request.getAddress(), request.getPort());   
				} else {
					//invalid request
					result = "Invalid request";
					reply = new DatagramPacket(result.getBytes(),result.length(), request.getAddress(), request.getPort());   					
				}  
				//send result
				aSocket.send(reply);
				classObject.logFile.writeLog("["+ classObject.serverLocation + " Server]: Received getCount(" + recordType +") request from " + (new String(request.getData(),0,request.getLength())).substring(0,3) + " server and replied \"" + result + "\"");
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
