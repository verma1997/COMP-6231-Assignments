package dcms;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * Server Main Class implements Member Webservice interface
 */
@WebService(endpointInterface = "dcms.Member_Webservice")
public class CenterServer implements Member_Webservice {
    public String serverLocation;       // MTL,LVL,DDO
    public String hostName;             //The host name for naming service.
    private int recordIDTR;             // Records the last recordID assigned to Teachers, initialize in Constructor.
    private int recordIDSR;             // Records the last recordID assigned to Students, initialize in Constructor.
    private int upLimit;                // The maximum ID for this server,initialize in Constructor.
    public String portNumber;             // The portNumber for naming service.
    // The main data structure to store Teacher and Student's records
    private Map<String, ArrayList<MemberRecord>> memberRecords = new HashMap<String, ArrayList<MemberRecord>>(26,1);
    // A simple way to store the login records of managers
    private byte[] loginRecord = new byte [10000];
    // To configure the servers' host, need to be modified manually
    public static String hostMTL = "localhost";
    public static String hostLVL = "localhost";
    public static String hostDDO = "localhost";
    // Configure the UDP ports used by servers
    private static int udpPortMTL = 2230;
    private static int udpPortLVL = 2231;
    private static int udpPortDDO = 2232;
    //The UDP server(extends Thread) being used to accept 'getCount' operations from other servers
    private UDPServer udpServer;
    // The file object for logging
    public LogFile logFile;

    // Dir for IDE Run
//    private String server_logs_path = "logs/server_logs/";
    // Dir for Terminal Run
    private String server_logs_path = "../logs/server_logs/";

    //Constructor without parameters
    public CenterServer() {}

    //Constructor with parameters
    public CenterServer (String serverrName, String host) {
        serverLocation = serverrName;
        hostName = host;

        logFile = new LogFile(server_logs_path + "server_" + serverLocation + "_" + System.currentTimeMillis() + ".log");

        if (serverLocation.equals("MTL")) {
            recordIDTR = recordIDSR = 0;
            upLimit = 33333;    //MTL Server uses 1-33333 ID resources for members
            udpServer = new UDPServer(this, udpPortMTL);
        } else if (serverLocation.equals("LVL")){
            recordIDTR = recordIDSR = 33333;
            upLimit = 66666;    //LVL Server uses 33334-66666 ID resources for members
            udpServer = new UDPServer(this, udpPortLVL);
        } else {
            recordIDTR = recordIDSR = 66666;
            upLimit = 99999;    //DDO Server uses 66667-99999 ID resources for members
            udpServer = new UDPServer(this, udpPortDDO);
        }
        //start the UDP server
        udpServer.start();

        for(char a = 'A'; a <= 'Z'; a ++) {
            memberRecords.put(String.valueOf(a), new ArrayList<MemberRecord>());
        }

        //log the create server event
        logFile.writeLog("[" + serverLocation + " Server]: created on port " + portNumber + ", member ID range is " + String.valueOf(recordIDTR + 1) + " - " + upLimit + ", UDP port is " + udpServer.UDPPort);
    }

    //Implementation of createDRecord to create teacher's records
    @Override
    public String createTRecord(String teacherID, String firstName, String lastName, String address, String phoneNumber, String specializations, String location, String managerID, int mode) {
        // TODO Auto-generated method stub
        try {
            int newID;
            if(mode == 0) {
                newID = getRecordIDTR();  //try to get ID resource(synchronized)
                if(newID == -1) {
                    return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] ERROR: createTRecord failed, No ID resource for a new Teacher.");
                }
            } else {
                newID = Integer.parseInt(teacherID.substring(2));
            }

            //Create and initialize new Doctor record
            Teacher teacher = new Teacher();
            int result = teacher.init(String.format("%05d", newID), firstName, lastName, address, phoneNumber, specializations, location);

            if(result == 0) {   //0--initialize Successfully
                //synchronized put new record into array list according to the key
                String tempKey = lastName.substring(0, 1).toUpperCase();
                ArrayList<MemberRecord> tempList = memberRecords.get(tempKey);
                synchronized(tempList){
                    tempList.add(teacher);
                }
                return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] Succeed: createTRecord: " + teacher.getMemberRecord() + " " + String.valueOf(tempList.size()));
            } else {        //-1,-2 -- initialize failed
                teacher = null;

                if (result == -1){	//Data is not valid(location)
                    return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] ERROR: createTRecord failed, The location can only be MTL,LVL or DDO.");
                } else {
                    return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] ERROR: createTRecord failed, Internal Error.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] ERROR: createTRecord failed, Other error.");
    }

    @Override
    public String createSRecord(String studentID, String firstName, String lastName, String courseRegistered, String status, String statusDate, String managerID, int mode) {
        // TODO Auto-generated method stub
        try {
            int newID;
            if(mode == 0) {
                newID = getRecordIDSR();	//try to get ID resource(synchronized)
                if(newID == -1) {
                    return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] ERROR: createSRecord failed, No ID resource for a new Student.");
                }
            } else {
                newID = Integer.parseInt(studentID.substring(2));
            }

            //Create and initialize new nurse record
            Student student = new Student();
            int result = student.init(String.format("%05d",newID), firstName, lastName, courseRegistered, status, statusDate);
            if(result == 0) {
                String tempKey = lastName.substring(0, 1).toUpperCase();
                ArrayList<MemberRecord> tempList = memberRecords.get(tempKey);
                synchronized(tempList) {
                    tempList.add(student);
                }
                return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] Succeed: createSRecord: " + student.getMemberRecord() + " " + String.valueOf(tempList.size()));
            } else {				//-1,-2 -- initialize failed
                student = null;
                if(result == -1) {	//Data is not valid(location)
                    return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] ERROR: createSRecord failed, Invalid courses registered or stauts value.");
                } else {
                    return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] ERROR: createSRecord failed, Internal Error.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] ERROR: createSRecord failed, Other error.");
    }

    @Override
    public String getRecordsCount(String recordType, String memberID) {
        // TODO Auto-generated method stub
        String resultStr = "";
        //UDPclient threads used to getReordCount from the other servers
        UDPClient thread1;
        UDPClient thread2;

        //get record count from three servers, create thread of UDPClient as needed
        if (serverLocation.equals("MTL")) {
            thread1 = new UDPClient(serverLocation,"LVL", hostLVL, udpPortLVL, recordType);
            thread2 = new UDPClient(serverLocation,"DDO", hostDDO, udpPortDDO, recordType);
            logFile.writeLog("[" + serverLocation + " Server]: send getCount(" + recordType + ") request to LVL and DDO server.");
        } else if(serverLocation.equals("LVL")) {
            thread1 = new UDPClient(serverLocation,"MTL", hostMTL, udpPortMTL, recordType);
            thread2 = new UDPClient(serverLocation,"DDO", hostDDO, udpPortDDO, recordType);
            logFile.writeLog("[" + serverLocation + " Server]: send getCount(" + recordType + ") request to MTL and DDO server.");
        } else {
            thread1 = new UDPClient(serverLocation,"MTL", hostMTL, udpPortMTL, recordType);
            thread2 = new UDPClient(serverLocation,"LVL", hostLVL, udpPortLVL, recordType);
            logFile.writeLog("[" + serverLocation + " Server]: send getCount(" + recordType + ") request to MTL and LVL server.");
        }

        //Start the two threads
        thread1.start();
        thread2.start();

        try {
            //wait for the first thread end
            thread1.join();
            logFile.writeLog("[" + serverLocation + " Server]: Got the reply of getCount(" + recordType + ") request from " + thread1.remoteServer + " server. \"" + thread1.memberCount + "\"");

            //wait for the second thread end
            thread2.join();
            logFile.writeLog("[" + serverLocation + " Server]: Got the reply of getCount(" + recordType + ") request from " + thread2.remoteServer + " server. \"" + thread2.memberCount + "\"");

            //create return message
            resultStr =  "[" + serverLocation + " Server: " + memberID + "] getRecordCounts(" + recordType + ") " + String.valueOf(getMemberCount(recordType)) + " " + thread1.memberCount + " " + thread2.memberCount;

            //release thread object
            thread1 = null;
            thread2 = null;

            return logFile.writeLog(resultStr);
        } catch (InterruptedException e)	{
            e.printStackTrace();
            return logFile.writeLog("[" + serverLocation + " Server: " + memberID + "] ERROR: getRecordCounts failed, Internal error.");
        }
    }
    // Implementation of editRecord to modify member records
    @Override
    public String editRecord(String recordID, String fieldName, String newValue, String memberID) {
        // TODO Auto-generated method stub

        //navigate in hash map for 26 keys
        for (char a = 'A'; a <='Z'; a ++){
            //get the array list corresponding to the key
            ArrayList<MemberRecord> tempList = memberRecords.get(String.valueOf(a));

            //lock the code block for further operation within the array list
            synchronized(tempList) {
                //navigate in array list to find the record
                Iterator<MemberRecord> itr = tempList.iterator();
                while(itr.hasNext()) {
                    MemberRecord tempMember = itr.next();

                    if (tempMember.getMemberID().equals(recordID)){
                        //find the record
                        int result;

                        //For Teacher record
                        if (recordID.substring(0, 2).equals("TR")) {
                            result = ((Teacher)tempMember).editRecord(fieldName, newValue);
                            if (result==0) {
                                return logFile.writeLog("[" + serverLocation + " Server: " + memberID + "] Succeed: Edit " + recordID + "'s " + fieldName + " to " + newValue+": " + ((Teacher)tempMember).getMemberRecord());
                            }
                            else if (result == -1){
                                return logFile.writeLog("[" + serverLocation + " Server: " + memberID + "] Error: editRecord failed, Fieldname '" + fieldName + "' invalid or can't be edited.");
                            }
                            else if (result == -2){
                                return logFile.writeLog("[" + serverLocation + " Server: " + memberID + "] Error: editRecord failed, The location can only be MTL,LVL or DDO.");
                            }
                            else {
                                return logFile.writeLog("[" + serverLocation + " Server: " + memberID + "] Error: editRecord failed, Internal error.");
                            }
                        } else { //For Student record
                            result = ((Student)tempMember).editRecord(fieldName, newValue);
                            if (result==0) {
                                return logFile.writeLog("[" + serverLocation + " Server: " + memberID + "] Succeed: Edit " + recordID + "'s " + fieldName + " to " + newValue + ": " + ((Student)tempMember).getMemberRecord());
                            }
                            else if (result == -1){
                                return logFile.writeLog("[" + serverLocation + " Server: " + memberID + "] Error: editRecord failed, Fieldname '" + fieldName + "' invalid or can't be edited.");
                            }
                            else if (result == -2){
                                return logFile.writeLog("[" + serverLocation + " Server: " + memberID + "] Error: editRecord failed, Invalid '" + fieldName + "' value.");
                            }
                            else {
                                return logFile.writeLog("[" + serverLocation + " Server: " + memberID + "] Error: editRecord failed, Internal error.");
                            }
                        }
                    }
                }    //end of one array list
            }//end of the array list lock
        }//end of navigation throughout all hash map

        //if there is no this record
        return logFile.writeLog("[" + serverLocation + " Server: " + memberID + "] ERROR: editRecord failed, Can't find record with recordID=" + String.valueOf(recordID));
    }

    @Override
    public String transferRecord(String recordID, String remoteCenterServer, String managerID) {
        // TODO Auto-generated method stub
        //navigate in hash map for 26 keys
        if (remoteCenterServer.equals(serverLocation)) {
            return ("[" + serverLocation + " Server: " + managerID + "] ERROR: transferRecord failed, Can't transfer to the same server, may cause deadlock");
        }
        for (char a = 'A'; a <='Z'; a ++){
            //get the array list corresponding to the key
            ArrayList<MemberRecord> tempList = memberRecords.get(String.valueOf(a));
            synchronized(tempList) {
                //lock the code block for further operation within the array list
                //navigate in array list to find the record
                Iterator<MemberRecord> itr= tempList.iterator();
                while (itr.hasNext()){
                    MemberRecord tempMember = itr.next();
                    if (tempMember.getMemberID().equals(recordID)){
                        //find the record
                        String result = "";
                        try {
                            URL url	= new URL("http://localhost:8080/" + remoteCenterServer + "?wsdl");
                            QName qName = new QName	("http://dcms/","CenterServerService");
                            Service service = Service.create(url,qName);
                            Member_Webservice ServerRef = service.getPort(Member_Webservice.class);

                            //For Teacher record
                            if (recordID.substring(0,2).equals("TR")) {
                                result = logFile.writeLog(ServerRef.createTRecord(recordID, ((Teacher) tempMember).firstName, ((Teacher) tempMember).lastName, ((Teacher) tempMember).address, ((Teacher) tempMember).phoneNumber, ((Teacher) tempMember).specialization, ((Teacher) tempMember).location, managerID, 1));
                            } else if (recordID.substring(0,2).equals("SR")) {
                                result = logFile.writeLog(ServerRef.createSRecord(recordID, ((Student) tempMember).firstName, ((Student) tempMember).lastName, ((Student) tempMember).courseRegistered, ((Student) tempMember).status, ((Student) tempMember).statusDate, managerID, 1));
                            }
                        } catch (Exception e) {
                            System.err.println("ERROR: " + e);
                            e.printStackTrace(System.out);
                        }
                        if (result.indexOf("Succeed") == -1){
                            return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] ERROR: transferRecord failed, Can't create record on remote server " + remoteCenterServer);
                        }
                        else{
                            tempList.remove(tempMember);
                            return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] Succeed: transferRecord " + recordID + " to " + remoteCenterServer);
                        }
                    }//end of find the record
                }//end of one array list
            }//end of lock array list
        }//end of navigation throughout all hash map

        //if there is no this record
        return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] ERROR: transferRecord failed, Can't find record with recordID=" + String.valueOf(recordID));
    }

    //Implementation of login with managerID
    //only record the online status, to avoid multi-login with same managerID
    @Override
    public int login(String managerID) {
        int loginIndex;
        //Check the managerID format
        if ((managerID.length() != 7)||(!managerID.substring(0, 3).equals(serverLocation))) {
            return (-1);    //Invalid ManagerID
        }
        try {
            loginIndex = Integer.parseInt(managerID.substring(3));
        } catch (NumberFormatException e) {
            return (-1);   //Invalid ManagerID
        }
        //Try to login,check the corresponding byte in loginRcord array
        synchronized(loginRecord) {
            if(loginRecord[loginIndex] == 1) {
                return (-2);  //ManagerID is already online
            } else {
                loginRecord[loginIndex] = 1;   //successful login
            }
        }
        logFile.writeLog("[" + serverLocation + " Server]: " + managerID + " login");
        return (0);
    }

    @Override
    public int logout(String managerID) {
        int loginIndex;
        //Check the managerID format
        if((managerID.length() != 7)||(!managerID.substring(0, 3).equals(serverLocation))) {
            return (-1);    //Invalid ManagerID
        }
        try {
            loginIndex=Integer.parseInt(managerID.substring(3));
        } catch (NumberFormatException e) {
            return (-1);   //Invalid ManagerID
        }
        //Try to logout, set to 0 anyway
        synchronized(loginRecord) {
            loginRecord[loginIndex] = 0;   //successful logout
        }

        logFile.writeLog("[" + serverLocation + " Server]: " + managerID + " logout");
        return (0);
    }

    // Synchronized get the ID resource for new teacher record
    public synchronized int getRecordIDTR() throws Exception {
        // TODO Auto-generated method stub
        if (recordIDTR < upLimit) {
            return (++ recordIDTR);
        } else {
            return (-1);     //ID is out of range
        }
    }

    // Synchronized get the ID resource for new student record
    public synchronized int getRecordIDSR() throws Exception {
        // TODO Auto-generated method stub
        if (recordIDSR < upLimit) {
            return (++ recordIDSR);
        } else {
            return (-1);    //ID is out of range
        }
    }

    //The real method to synchronized get member count can be referenced by object itself or the UDP Server
    public String getMemberCount(String recordType){
        int result = 0;
        //navigate in hash map for 26 keys
        for (char a = 'A'; a <= 'Z'; a ++){

            //get the array list according to key
            ArrayList<MemberRecord> tempList = memberRecords.get(String.valueOf(a));
            //lock the code block for further operation within the array list
            synchronized(tempList) {
                //get both Doctor and nurse, simply get the size
                if(recordType.equals("BO")) {
                    result += tempList.size();
                } else { //get Doctor or nurse, need to navigate through all records
                    //navigate in array list
                    Iterator<MemberRecord> itr= tempList.iterator();
                    while(itr.hasNext()) {
                        MemberRecord tempMember = itr.next();
                        //Compare the first two characters of recordID with the recordType
                        if(tempMember.getMemberID().substring(0,2).equals(recordType)) {
                            result++;
                        }
                    }
                }
            }//end of lock
        }//end of hash map
        return serverLocation + " " + String.valueOf(result);
    }
}
