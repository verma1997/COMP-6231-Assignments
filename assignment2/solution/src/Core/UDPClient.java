package Core;
import java.net.*; 
import java.io.*;

/**
 * Thread class of UDPClient used by CenterServer to send getCount request to other server
 */
public class UDPClient extends Thread{
	private int UDPport;
	private String remoteHost;
	private String localServer;
	public String memberCount;
	public String remoteServer;
	public String recordType;
	
	//Construction method
	public UDPClient (String local,String remote,String host,int port,String type){
		this.localServer = local;
		this.remoteServer = remote;
		this.remoteHost = host;
		this.UDPport = port;
		this.recordType = type;
	}	
	
	//Body of thread
	public void run(){ 
		DatagramSocket aSocket = null;  
		try { 
			aSocket = new DatagramSocket();    
			String m = localServer+recordType+"getCount"; 
			InetAddress aHost = InetAddress.getByName(remoteHost); 
			DatagramPacket request = new DatagramPacket(m.getBytes(),m.length(),aHost,UDPport); 
			aSocket.send(request);
			byte[] buffer = new byte[1000]; 
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length); 
			aSocket.setSoTimeout(5000); //set time out interval for cases
			try {
				aSocket.receive(reply);
				memberCount = new String(reply.getData(),0,reply.getLength());
			}catch (SocketTimeoutException e) {
                // timeout exception.
				memberCount = "time out";
            }
		} catch (SocketException e){
			System.out.println("Socket: " + e.getMessage()); 
		} catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		} finally {
			if(aSocket != null) aSocket.close();
		} 
	} 
}
