package Core;

import CenterModule.Center;
import CenterModule.CenterHelper;
import CenterModule._CenterImplBase;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * CenterServant Class extends CenterImplBase Class to implement the main methods.
 */
public class CenterServant extends _CenterImplBase {

    private static final long serialVersionUID = 1L;
    public String serverLocation;	//MTL,LVL,DDO
    private int recordIdTR;			//Records the last recordID assigned to Teacher,initialize in Constructor.
    private int recordIdSR;			//Records the last recordID assigned to Student,initialize in Constructor.
    private int upLimit;			//The maximum ID for this server,initialize in Constructor.

    public String hostName;         //The host name for naming service.
    public String portNum;			//The portNumber for naming service.

    //The main data structure to store Teacher's and Student's records
    private Map<String,ArrayList<memberRecord>> memberRecords = new HashMap<String,ArrayList<memberRecord>>(26,1);

    //A simple way to store the login records of managers
    private byte [] loginRecord = new byte [10000];

    //To configure the servers' host ,need to be modified manually
    public static String hostMTL = "127.0.0.1";
    public static String hostLVL = "127.0.0.1";
    public static String hostDDO = "127.0.0.1";

    //Configure the UDP ports used by servers
    private static int udpPortMTL = 2230;
    private static int udpPortLVL = 2231;
    private static int udpPortDDO = 2232;

    //The UDP server(extends Thread) being used to accept 'getCount' operations from other servers
    private UDPServer myUDPServer;

    //The file object for logging
    public logFile mylogfile;

    //Constructor
    public CenterServant(String location, String host, String port){
        //Indicate which server it is MTL,LVL,DDO
        serverLocation = location;
        hostName = host;
        portNum = port;

        //Initialize log file object
        mylogfile = new logFile("server\\"+serverLocation+System.currentTimeMillis()+".log");

        //Initialize ID range, portNum for exporting object and UDPserver
        if (serverLocation.equals("MTL")){
            recordIdTR = recordIdSR = 0;
            upLimit = 33333;		//MTL Server uses 1-33333 ID resources for members
            myUDPServer = new UDPServer(this,udpPortMTL);
        }
        else if (serverLocation.equals("LVL")){
            recordIdTR = recordIdSR = 33333;
            upLimit = 66666;		//LVL Server uses 33334-66666 ID resources for members
            myUDPServer = new UDPServer(this,udpPortLVL);
        }
        else {
            recordIdTR = recordIdSR = 66666;
            upLimit = 99999;		//DDO Server uses 66667-99999 ID resources for members
            myUDPServer = new UDPServer(this,udpPortDDO);
        }
        //Start the UDP server
        myUDPServer.start();

        //Initialize main data structure with 26 keys('A'-'Z')
        //the last name with initial 'w' and 'W' will be treated as the same key and stored in the same array list
        for (char a = 'A';a <= 'Z'; a ++){
            memberRecords.put(String.valueOf(a), new ArrayList<memberRecord>());
        }

        //Log the create server event
        mylogfile.writeLog("["+serverLocation+" Server]: created, member ID range is "+String.valueOf(recordIdTR+1)+" - "+upLimit+", UDP port is "+myUDPServer.UDPport);
    }

    //Implementation of createTRecord to create teacher's records
    @Override
    public String createTRecord(String firstName, String lastName, String address, String phone, String specialization, String location, String ManagerID, int mode, String recordID) {
        try {
            int newID;
            if (mode == 0){
                newID = getRecordIdDR();  //try to get ID resource(synchronized)
                if (newID == -1){
                    return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] ERROR: createTRecord failed, No ID resource for a new Teacher.");
                }
            }
            else newID = Integer.parseInt(recordID.substring(2));

            //Create and initialize new Teacher record
            TeacherRecord newDR = new TeacherRecord();
            int result = newDR.init(firstName, lastName, address, phone,
                    specialization, location, String.format("%05d",newID));

            if (result == 0){   //0--initialize Successfully
                //synchronized put new record into array list according to the key
                String tempKey = lastName.substring(0,1).toUpperCase();
                ArrayList<memberRecord> tempList = memberRecords.get(tempKey);
                synchronized(tempList){
                    tempList.add(newDR);
                }
                return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] Succeed: createTRecord: "+newDR.getRecord()+" "+String.valueOf(tempList.size()));
            }
            else {//-1,-2 -- initialize failed
                newDR = null;
                if (result == -1){	//Data is not valid(location)
                    return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] ERROR: createTRecord failed, The location can only be MTL,LVL or DDO.");
                }
                else {
                    return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] ERROR: createTRecord failed, Internal Error.");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] ERROR: createTRecord failed, Other error.");

    }

    //Implementation of createSRecord to create student's records
    @Override
    public String createSRecord(String firstName, String lasName, String coursesRegistered, String status, String statusDate, String ManagerID, int mode, String recordID) {
        try {
            int newID;
            if (mode == 0){
                newID = getRecordIdNR();	//try to get ID resource(synchronized)
                if (newID == -1){
                    return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] ERROR: createSRecord failed, No ID resource for a new Student.");
                }
            }
            else newID = Integer.parseInt(recordID.substring(2));
            //Create and initialize new student record
            StudentRecord newNR = new StudentRecord();
            String[] courses = coursesRegistered.split(" ");
            int result = newNR.init(firstName, lasName, courses,
                    status, statusDate, String.format("%05d",newID));
            if (result == 0){   //0--initialize Successfully
                //synchronized put new record into array list according to the key
                String tempKey = lasName.substring(0,1).toUpperCase();
                ArrayList<memberRecord> tempList = memberRecords.get(tempKey);
                synchronized(tempList){
                    tempList.add(newNR);
                }
                return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] Succeed: createSRecord: "+newNR.getRecord()+" "+String.valueOf(tempList.size()));
            }
            else {//-1,-2 -- initialize failed
                newNR = null;
                if (result == -1){	//Data is not valid(location)
                    return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] ERROR: createSRecord failed, Invalid coursesRegistered or stauts value.");
                }
                else {
                    return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] ERROR: createSRecord failed, Internal Error.");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] ERROR: createSRecord failed, Other error.");

    }

    //Implementation of getRecordCounts to get the number of records
    @Override
    public String getRecordCounts(String recordType, String ManagerID) {
        String resultStr = "";
        UDPClient th1,th2;		//UDPclient threads used to getReordCount from the other servers

        //get record count from three servers, create thread of UDPClient as needed
        if (serverLocation.equals("MTL")){
            th1 = new UDPClient(serverLocation,"LVL",hostLVL,udpPortLVL,recordType);
            th2 = new UDPClient(serverLocation,"DDO",hostDDO,udpPortDDO,recordType);
            mylogfile.writeLog("["+serverLocation+" Server]: send getCount("+recordType+") request to LVL and DDO server.");
        }
        else if(serverLocation.equals("LVL")){
            th1 = new UDPClient(serverLocation,"MTL",hostMTL,udpPortMTL,recordType);
            th2 = new UDPClient(serverLocation,"DDO",hostDDO,udpPortDDO,recordType);
            mylogfile.writeLog("["+serverLocation+" Server]: send getCount("+recordType+") request to MTL and DDO server.");
        }
        else{
            th1 = new UDPClient(serverLocation,"MTL",hostMTL,udpPortMTL,recordType);
            th2 = new UDPClient(serverLocation,"LVL",hostLVL,udpPortLVL,recordType);
            mylogfile.writeLog("["+serverLocation+" Server]: send getCount("+recordType+") request to MTL and LVL server.");
        }

        //Start the two threads
        th1.start();
        th2.start();

        try {
            //wait for the first thread end
            th1.join();
            mylogfile.writeLog("["+serverLocation+" Server]: Got the reply of getCount("+recordType+") request from "+th1.remoteServer+" server. \""+th1.memberCount+"\"");

            //wait for the second thread end
            th2.join();
            mylogfile.writeLog("["+serverLocation+" Server]: Got the reply of getCount("+recordType+") request from "+th2.remoteServer+" server. \""+th2.memberCount+"\"");

            //create return message
            resultStr =  "["+serverLocation+" Server: "+ManagerID+"] getRecordCounts("+recordType+") "+String.valueOf(getMemberCount(recordType))+" "+th1.memberCount+" "+th2.memberCount;

            //release thread object
            th1 = null;
            th2 = null;

            return mylogfile.writeLog(resultStr);
        }catch (InterruptedException e)	{
            e.printStackTrace();
            return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] ERROR: getRecordCounts failed, Internal error.");
        }
    }

    //Implementation of editRecord to modify member records
    @Override
    public String editRecord(String recordID, String filedName, String newValue, String ManagerID) {
        for (char a = 'A'; a <='Z'; a ++){
            //get the array list corresponding to the key
            ArrayList<memberRecord> tempList = memberRecords.get(String.valueOf(a));
            synchronized(tempList) {
                //lock the code block for further operation within the array list
                //navigate in array list to find the record
                Iterator<memberRecord> itr= tempList.iterator();
                while (itr.hasNext()){
                    memberRecord tempMember = itr.next();

                    if (tempMember.getID().equals(recordID)){
                        //find the record

                        int result;

                        //For Teacher record
                        if (recordID.substring(0,2).equals("TR")) {
                            result = ((TeacherRecord)tempMember).editRecord(filedName, newValue);
                            if (result==0) {
                                return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] Succeed: Edit "+recordID+"'s "+filedName+" to "+newValue+": "+((TeacherRecord)tempMember).getRecord());
                            }
                            else if (result == -1){
                                return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] Error: editRecord failed, Fieldname '"+filedName+"' invalid or can't be edited.");
                            }
                            else if (result == -2){
                                return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] Error: editRecord failed, The location can only be MTL,LVL or DDO.");
                            }
                            else {
                                return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] Error: editRecord failed, Internal error.");
                            }
                        }
                        //For student record
                        else {
                            result = ((StudentRecord)tempMember).editRecord(filedName, newValue);
                            if (result==0) {
                                return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] Succeed: Edit "+recordID+"'s "+filedName+" to "+newValue+": "+((StudentRecord)tempMember).getRecord());
                            }
                            else if (result == -1){
                                return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] Error: editRecord failed, Fieldname '"+filedName+"' invalid or can't be edited.");
                            }
                            else if (result == -2){
                                return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] Error: editRecord failed, Invalid '"+filedName+"' value.");
                            }
                            else {
                                return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] Error: editRecord failed, Internal error.");
                            }
                        }
                    }//end of find the record
                }//end of one array list
            }//end of lock array list
        }//end of navigation throughout all hash map

        //if there is no this record
        return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] ERROR: editRecord failed, Can't find record with recordID="+String.valueOf(recordID));

    }

    //synchronized get the ID resource for new teacher record
    public synchronized int getRecordIdDR() throws Exception {
        if (recordIdTR < upLimit)
            return (++ recordIdTR);
        else return (-1);     //Id is out of range
    }

    //synchronized get the ID resource for new student record
    public synchronized int getRecordIdNR() throws Exception {
        if (recordIdSR < upLimit)
            return (++ recordIdSR);
        else return (-1);    //Id is out of range
    }

    //The real method to synchronized get member count
    //can be referenced by object itself or the UDP Server
    public String getMemberCount(String recordType){
        int result = 0;
        //navigate in hash map for 26 keys
        for (char a = 'A';a <= 'Z';a ++){

            //get the array list according to key
            ArrayList<memberRecord> tempList = memberRecords.get(String.valueOf(a));
            //lock the code block for further operation within the array list
            synchronized(tempList) {
                //get both Teacher and student, simply get the size
                if (recordType.equals("BO")){
                    result += tempList.size();
                }
                //get Teacher or student, need to navigate through all records
                else {
                    //navigate in array list
                    Iterator<memberRecord> itr= tempList.iterator();
                    while (itr.hasNext()){
                        memberRecord tempMember = itr.next();
                        //Compare the first two characters of recordID with the recordType
                        if (tempMember.recordID.substring(0,2).equals(recordType)){
                            result++;
                        }
                    }
                }
            }//end of lock
        }//end of hash map
        return serverLocation+" "+String.valueOf(result);
    }

    //Implementation of login with managerID
    //only record the online status, to avoid invalid operation (wrong server),
    //and multi-login with same managerID
    @Override
    public int login(String ManagerID) {
        int loginIndex;
        //Check the managerID format
        if ((ManagerID.length()!=7)||(!ManagerID.substring(0,3).equals(serverLocation))){
            return (-1);    //Invalid ManagerID
        }
        try {
            loginIndex=Integer.parseInt(ManagerID.substring(3));
        }catch  (NumberFormatException e) {
            return (-1);   //Invalid ManagerID
        }
        //Try to login,check the corresponding byte in loginRcord array
        synchronized(loginRecord) {
            if (loginRecord[loginIndex]==1)
                return (-2);  //ManagerID is already online
            else {
                loginRecord[loginIndex]=1;   //successful login
            }
        }
        mylogfile.writeLog("["+serverLocation+" Server]: "+ManagerID+" login");
        return (0);
    }

    //Implementation of login with managerID
    //only record the online status, to avoid multi-login with same managerID
    @Override
    public int logout(String ManagerID) {
        int loginIndex;
        //Check the managerID format
        if ((ManagerID.length()!=7)||(!ManagerID.substring(0,3).equals(serverLocation))){
            return (-1);    //Invalid ManagerID
        }
        try {
            loginIndex=Integer.parseInt(ManagerID.substring(3));
        }catch  (NumberFormatException e) {
            return (-1);   //Invalid ManagerID
        }
        //Try to logout,set to 0 anyway
        synchronized(loginRecord) {
            loginRecord[loginIndex]=0;   //successful logout
        }

        mylogfile.writeLog("["+serverLocation+" Server]: "+ManagerID+" logout");
        return (0);
    }

    //Implementation of transferRecord
    @Override
    public String transferRecord(String recordID, String remoteCenterServer, String ManagerID) {
        if (remoteCenterServer.equals(serverLocation))
            return ("["+serverLocation+" Server: "+ManagerID+"] ERROR: transferRecord failed, Can't transfer to the same server, may cause deadlock");
        for (char a = 'A'; a <='Z'; a ++){
            //get the array list corresponding to the key
            ArrayList<memberRecord> tempList = memberRecords.get(String.valueOf(a));
            synchronized(tempList) {
                //lock the code block for further operation within the array list
                //navigate in array list to find the record
                Iterator<memberRecord> itr= tempList.iterator();
                while (itr.hasNext()){
                    memberRecord tempMember = itr.next();
                    if (tempMember.getID().equals(recordID)){
                        //find the record
                        String result = "";
                        try {
                            ORB orb = ORB.init(new String[]{"-ORBInitialPort",portNum,"-ORBInitialHost",hostName},null);
                            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
                            NamingContext ncRef = NamingContextHelper.narrow(objRef);

                            NameComponent nc = new NameComponent(remoteCenterServer,"");
                            NameComponent path[] = {nc};
                            Center ServerRef = CenterHelper.narrow(ncRef.resolve(path));
                            //For Teacher record
                            if (recordID.substring(0,2).equals("TR"))
                                result = mylogfile.writeLog(ServerRef.createTRecord(((TeacherRecord)tempMember).firstName,((TeacherRecord)tempMember).lastName, ((TeacherRecord)tempMember).Address,((TeacherRecord)tempMember).phoneNum,((TeacherRecord)tempMember).specialization, ((TeacherRecord)tempMember).location, ManagerID, 1, recordID));
                            else if (recordID.substring(0,2).equals("SR")){

                                String courseLog = String.join(" ", ((StudentRecord)tempMember).coursesRegistered);
                                result = mylogfile.writeLog(ServerRef.createSRecord(((StudentRecord)tempMember).firstName,((StudentRecord)tempMember).lastName,((StudentRecord)tempMember).coursesRegistered,((StudentRecord)tempMember).status, ((StudentRecord)tempMember).statusDate, ManagerID, 1, recordID));

                            }
                        }catch (Exception e){
                            System.err.println("ERROR: "+e);
                            e.printStackTrace(System.out);
                        }
                        if (result.indexOf("Succeed")==-1){
                            return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] ERROR: transferRecord failed, Can't create record on remote server "+remoteCenterServer);
                        }
                        else{
							/*try {
								Thread.currentThread();
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
                            tempList.remove(tempMember);
                            return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] Succeed: transferRecord "+recordID+" to "+remoteCenterServer);
                        }
                    }//end of find the record
                }//end of one array list
            }//end of lock array list
        }//end of navigation throughout all hash map

        //if there is no this record
        return mylogfile.writeLog("["+serverLocation+" Server: "+ManagerID+"] ERROR: transferRecord failed, Can't find record with recordID="+String.valueOf(recordID));

    }
}
