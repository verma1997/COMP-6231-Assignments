package dcms;

import java.net.*;
import java.io.*;

/**
 * Thread class of UDPClient used by CenterServer to send getCount request to other servers
 */
public class UDPClient extends Thread {
    private int UDPPort; // Port of remote UDP server
    private String remoteHost;  // IP address of remote UDP server
    private String localServer;	// Name of server who sends the request
    public String memberCount;	// Record the staff count from reply, used by server who create this thread
    public String remoteServer;	// Name of remote server, for management purpose only
    public String recordType;   // getCount(recordType), Teacher, Studnet, Both

    // Construction method
    public UDPClient (String local, String remote, String host, int port, String type){
        this.localServer = local;
        this.remoteServer = remote;
        this.remoteHost = host;
        this.UDPPort = port;
        this.recordType = type;
    }

    // Body of thread
    @Override
    public void run(){
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            String m = localServer+recordType+"getCount";
            InetAddress aHost = InetAddress.getByName(remoteHost);
            DatagramPacket request = new DatagramPacket(m.getBytes(), m.length(), aHost, UDPPort);
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            // Set time out interval for cases
            aSocket.setSoTimeout(5000);
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
            if(aSocket != null) {
                aSocket.close();
            }
        }
    }
}
