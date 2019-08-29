import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Create RMI registry, start RMI service, and then register remote objects in the registry
 */
public class CenterServer implements ClassServiceInterface {
    public String serverLocation;
    private int recordIDTR;
    private int recordIDSR;
    private int upLimit;
    private int portNumber;
    private Map<String, ArrayList<Member>> memberRecords = new HashMap<String, ArrayList<Member>>(26,1);

    private byte [] loginRecord = new byte [10000];

    private static String hostMTL = "localhost";
    private static String hostLVL = "localhost";
    private static String hostDDO = "localhost";

    private static int udpPortMTL = 2230;
    private static int udpPortLVL = 2231;
    private static int udpPortDDO = 2232;

    private UDPServer udpServer;

    public LogFile logFile;

    private String server_logs_path = "logs/server_logs/";

    public CenterServer (String serverrName) {
        serverLocation = serverrName;

        logFile = new LogFile(server_logs_path + "server_" + serverLocation + "_" + System.currentTimeMillis() + ".log");

        if (serverLocation.equals("MTL")) {
            recordIDTR = recordIDSR = 0;
            upLimit = 99999;
            portNumber = 2021;
            udpServer = new UDPServer(this, udpPortMTL);
        } else if (serverLocation.equals("LVL")){
            recordIDTR = recordIDSR = 0;
            upLimit = 99999;
            portNumber = 2022;
            udpServer = new UDPServer(this, udpPortLVL);
        } else {
            recordIDTR = recordIDSR = 0;
            upLimit = 99999;
            portNumber = 2023;
            udpServer = new UDPServer(this, udpPortDDO);
        }
        //start the UDP server
        udpServer.start();

        for(char a = 'A'; a <= 'Z'; a ++) {
            memberRecords.put(String.valueOf(a), new ArrayList<Member>());
        }

        //log the create server event
        logFile.writeLog("[" + serverLocation + " Server]: created on port " + portNumber + ", member ID range is " + String.valueOf(recordIDTR + 1) + " - " + upLimit + ", UDP port is " + udpServer.udpPort);
    }

    @Override
    public String createTRecord(String managerID, String firstName, String lastName, String address, String phoneNumber, String specializations, String location) throws RemoteException{
        // TODO Auto-generated method stub
        try {
            int newID = getRecordIDTR();
            if(newID == -1) {
                return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] ERROR: createTRecord failed, No ID resource for a new Teacher.");
            }
            //Create and initialize new Doctor record
            Teacher teacher = new Teacher();
            int result = teacher.init(String.format("%05d", newID), firstName, lastName, address, phoneNumber, specializations, location);

            if(result == 0) {
                //synchronized put new record into array list according to the key
                String tempKey = lastName.substring(0, 1).toUpperCase();
                ArrayList<Member> tempList = memberRecords.get(tempKey);
                synchronized(tempList){
                    tempList.add(teacher);
                }
                return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] Succeed: createTRecord: " + teacher.getMemberRecord() + " " + String.valueOf(tempList.size()));
            } else {
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
    public String createSRecord(String managerID, String firstName, String lastName, String coursesRegistered, String status, String statusDate) throws RemoteException {
        // TODO Auto-generated method stub
        try {
            int newID = getRecordIDSR();
            if(newID == -1) {
                return logFile.writeLog("[" + serverLocation + " Server: " + managerID + "] ERROR: createSRecord failed, No ID resource for a new Student.");
            }
            //Create and initialize new nurse record
            Student student = new Student();
            int result = student.init(String.format("%05d",newID), firstName, lastName, coursesRegistered, status, statusDate);
            if(result == 0) {
                String tempKey = lastName.substring(0, 1).toUpperCase();
                ArrayList<Member> tempList = memberRecords.get(tempKey);
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
    public String getRecordsCount(String recordType, String memberID) throws RemoteException {
        // TODO Auto-generated method stub
        String resultStr = "";
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
    //implementation of editRecord to modify staff records
    @Override
    public String editRecord(String id, String fieldName, String newValue, String memberID) throws RemoteException{
        // TODO Auto-generated method stub

        //navigate in hash map for 26 keys
        for (char a = 'A'; a <='Z'; a ++){
            //get the array list corresponding to the key
            ArrayList<Member> tempList = memberRecords.get(String.valueOf(a));

            //lock the code block for further operation within the array list
            synchronized(tempList) {
                //navigate in array list to find the record
                Iterator<Member> itr = tempList.iterator();
                while(itr.hasNext()) {
                    Member tempMember = itr.next();

                    if (tempMember.getMemberID().equals(id)){
                        //find the record
                        int result;

                        //For Doctor record
                        if (id.substring(0, 2).equals("TR")) {
                            result = ((Teacher)tempMember).editRecord(fieldName, newValue);
                            if (result==0) {
                                return logFile.writeLog("[" + serverLocation + " Server: " + memberID + "] Succeed: Edit " + id + "'s " + fieldName + " to " + newValue+": " + ((Teacher)tempMember).getMemberRecord());
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
                        } else {
                            result = ((Student)tempMember).editRecord(fieldName, newValue);
                            if (result==0) {
                                return logFile.writeLog("[" + serverLocation + " Server: " + memberID + "] Succeed: Edit " + id + "'s " + fieldName + " to " + newValue + ": " + ((Student)tempMember).getMemberRecord());
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
        return logFile.writeLog("[" + serverLocation + " Server: " + memberID + "] ERROR: editRecord failed, Can't find record with recordID=" + String.valueOf(id));
    }

    //Implementation of login with managerID
    //only record the online status, to avoid multi-login with same managerID
    @Override
    public int login(String ManagerID) throws RemoteException {
        int loginIndex;
        //Check the managerID format
        if ((ManagerID.length() != 7)||(!ManagerID.substring(0, 3).equals(serverLocation))) {
            return (-1);    //Invalid ManagerID
        }
        try {
            loginIndex = Integer.parseInt(ManagerID.substring(3));
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
        logFile.writeLog("[" + serverLocation + " Server]: " + ManagerID + " login");
        return (0);
    }

    @Override
    public int logout(String ManagerID) throws RemoteException {
        int loginIndex;
        //Check the managerID format
        if((ManagerID.length() != 7)||(!ManagerID.substring(0, 3).equals(serverLocation))) {
            return (-1);    //Invalid ManagerID
        }
        try {
            loginIndex=Integer.parseInt(ManagerID.substring(3));
        } catch (NumberFormatException e) {
            return (-1);   //Invalid ManagerID
        }
        //Try to logout,set to 0 anyway
        synchronized(loginRecord) {
            loginRecord[loginIndex] = 0;   //successful logout
        }

        logFile.writeLog("[" + serverLocation + " Server]: " + ManagerID + " logout");
        return (0);
    }

    // Synchronized get the ID resource for new teacher record
    public synchronized int getRecordIDTR() throws Exception {
        // TODO Auto-generated method stub
        if (recordIDTR < upLimit) {
            return (++ recordIDTR);
        } else {
            return (-1);     //Id is out of range
        }
    }

    // Synchronized get the ID resource for new student record
    public synchronized int getRecordIDSR() throws Exception {
        // TODO Auto-generated method stub
        if (recordIDSR < upLimit) {
            return (++ recordIDSR);
        } else {
            return (-1);    //Id is out of range
        }
    }

    //The real method to synchronized get member count can be referenced by object itself or the UDP Server
    public String getMemberCount(String recordType){
        int result = 0;
        //navigate in hash map for 26 keys
        for (char a = 'A';a <= 'Z';a ++){

            //get the array list according to key
            ArrayList<Member> tempList = memberRecords.get(String.valueOf(a));
            //lock the code block for further operation within the array list
            synchronized(tempList) {
                //get both Doctor and nurse, simply get the size
                if(recordType.equals("BO")) {
                    result += tempList.size();
                } else { //get Doctor or nurse, need to navigate through all records
                    //navigate in array list
                    Iterator<Member> itr= tempList.iterator();
                    while(itr.hasNext()) {
                        Member tempMember = itr.next();
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

    public void exportServer(String location) throws Exception {
        Remote obj = UnicastRemoteObject.exportObject(this, this.portNumber);
        Registry registry;
        try{
            registry = LocateRegistry.createRegistry(2020);
        } catch (ExportException e) {
            registry = LocateRegistry.getRegistry(2020);
        }
        registry.rebind(location, obj);
        logFile.writeLog("["+serverLocation+" Server]: registered on port 2020.");
    }

    //no use temporarily, try to register the object to the remote registry, failed
    public void exportServer(String location, String host) throws Exception {
        Remote obj = UnicastRemoteObject.exportObject(this, this.portNumber);
        Registry registry = LocateRegistry.getRegistry(host,2020);
        registry.rebind(location, obj);
    }

    public static void main(String[] args) {
        try {
            //Judge the running mode from the first argument
            int runMode = Integer.parseInt(args[0]);

            if(runMode == 0) {
                //local mode with all servers and registry on one machine
                (new CenterServer("MTL")).exportServer("MTL");
                (new CenterServer("LVL")).exportServer("LVL");
                (new CenterServer("DDO")).exportServer("DDO");
                System.out.println("Successfully registered and all three servers (MTL/LVL/DDO) are up and running!");
            } else if(runMode == 1) {
                //local mode with one server and registry on one machine
                //need to indicate which server you want to create with the second argument
                String serverLocation = args[1].toUpperCase();
                if(!Teacher.isBelongList(serverLocation, Teacher.validLocations)) {
                    System.out.println("ERROR: Server location can only be MTL,LVL or DDO!");
                } else {
                    (new CenterServer(serverLocation)).exportServer(serverLocation);
                    System.out.println("Successfully registered and "+ serverLocation + " server are up and running!");
                }
            } else if(runMode == 2) {
                //remote mode with one server created and registered to registry on other machine,
                //need to indicate which server you want to create with the second argument,
                //and remote registry host with the third argument, no use temporarily
                String serverLocation = args[1].toUpperCase();
                if (!Teacher.isBelongList(serverLocation, Teacher.validLocations)){
                    System.out.println("ERROR: Server location can only be MTL,LVL or DDO!");
                } else {
                    (new CenterServer(serverLocation)).exportServer(serverLocation,args[2]);
                    System.out.println(serverLocation + " server is up and running!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
