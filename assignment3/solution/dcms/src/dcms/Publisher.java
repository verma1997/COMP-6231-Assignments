package dcms;

import javax.xml.ws.Endpoint;

/**
 * Publisher CLass for wsdl files
 */
public class Publisher {
    public static void main(String[] args){
        Endpoint endpoint = Endpoint.publish("http://localhost:8080/MTL", new CenterServer("MTL","localhost"));
        isPublished(endpoint, "MTL");
        endpoint = Endpoint.publish("http://localhost:8080/LVL", new CenterServer("LVL","localhost"));
        isPublished(endpoint, "LVL");
        endpoint = Endpoint.publish("http://localhost:8080/DDO", new CenterServer("DDO","localhost"));
        isPublished(endpoint, "DDO");
    }

    private static void isPublished(Endpoint endpoint, String serverName) {
        if(endpoint.isPublished()) {
            System.out.println(serverName + " web service is published successfully!\n");
        } else {
            System.out.println(serverName + "web service failed to be published!\n");
        }
    }
}
