package Core;

import CenterModule.Center;
import CenterModule.CenterHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import java.util.Random;
import java.util.Scanner;

/**
 * Implement manager client including console menu, extending Thread class.
 */
public class ManagerClient extends Thread{
    private String ManagerID;	//store the manager id from args[]
    private String serverName;  //to analyze and store the server name from manager id
    private String myServerHost; //to store the name server host
    private String myServerPort; //to store the name server port

    //operationType and loop count will tell the thread what to do
    private int operationType;  //Be initialized in construct method,
    //indicate what kind of operation to be executed

    private int loopcount;		//store how many loops to do the operation

    public ManagerClient(String ID,int operation,int loop,String host,String port) {
        this.ManagerID = ID;
        this.serverName = ID.substring(0, 3);
        this.operationType = operation;
        this.loopcount = loop;
        this.myServerHost = host;
        this.myServerPort = port;
    }

    public static boolean checkServer(String serverName){
        return ((serverName.equals("MTL"))||(serverName.equals("LVL"))||(serverName.equals("DDO")));
    }

    public void run(){
        logFile mylogfile = new logFile("client\\"+ManagerID+System.currentTimeMillis()+".log");
        try{
            ORB orb = ORB.init(new String[]{"-ORBInitialPort",myServerPort,"-ORBInitialHost",myServerHost},null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContext ncRef = NamingContextHelper.narrow(objRef);

            NameComponent nc = new NameComponent(serverName,"");
            NameComponent path[] = {nc};
            Center ServerRef = CenterHelper.narrow(ncRef.resolve(path));
            int loginResult = ServerRef.login(ManagerID);
            if (loginResult==0){  //login succeed - means ManagerID matches the server and no same ManagerID online
                mylogfile.writeLog("Succeed: "+ManagerID+" login to the "+serverName+" server on "+myServerHost);

                switch(operationType) {
                    case 1:{	//insert operation
                        Random rn = new Random();
                        for (int i=0;i<loopcount;i++){
                            int tt = rn.nextInt(26);
                            char a = (char)((int)'A'+tt);
                            try{
                                System.out.println(mylogfile.writeLog(ServerRef.createTRecord("John", a + "en", "6215 Couperin","438962921","maths", serverName,ManagerID,0,"")));
                            }catch (Exception e){
                                System.out.println(mylogfile.writeLog(e.getMessage()));
                            }
                            sleep(100);
                            try{
                                System.out.println(mylogfile.writeLog(ServerRef.createSRecord("Mary", a + "ang", "maths french","active","2015-01-04",ManagerID,0,"")));
                            }catch (Exception e){
                                System.out.println(mylogfile.writeLog(e.getMessage()));
                            }
                            sleep(100);
                        }
                        break;}
                    case 2:{ //edit records
                        for (int i=0;i<loopcount;i++){
                            if ((i%2)==0) {
                                try {
                                    System.out.println(mylogfile.writeLog(ServerRef.editRecord("TR00001","location","MTL",ManagerID)));
                                }catch (Exception e){
                                    System.out.println(mylogfile.writeLog(e.getMessage()));
                                }
                                sleep(100);
                                try {
                                    System.out.println(mylogfile.writeLog(ServerRef.editRecord("SR00001","status","ACTIVE",ManagerID)));
                                }catch (Exception e){
                                    System.out.println(mylogfile.writeLog(e.getMessage()));
                                }
                                sleep(100);
                            }
                            else {
                                try {
                                    System.out.println(mylogfile.writeLog(ServerRef.editRecord("TR00001","location","DDO",ManagerID)));
                                }catch (Exception e){
                                    System.out.println(mylogfile.writeLog(e.getMessage()));
                                }
                                sleep(100);
                                try {
                                    System.out.println(mylogfile.writeLog(ServerRef.editRecord("SR00001","status","Terminated",ManagerID)));
                                }catch (Exception e){
                                    System.out.println(mylogfile.writeLog(e.getMessage()));
                                }
                                sleep(100);
                            }
                        }
                        break;}
                    case 3:{  //get counts
                        for (int i=0;i<loopcount;i++){
                            try{
                                System.out.println(mylogfile.writeLog(ServerRef.getRecordCounts("BO",ManagerID)));
                            }catch (Exception e){
                                System.out.println(mylogfile.writeLog(e.getMessage()));
                            }
                            sleep(10);
                        }
                        break;}
                }// end of switch
                ServerRef.logout(ManagerID);
                mylogfile.writeLog("Succeed: "+ManagerID+" logout from the "+serverName+" server on "+myServerHost);
            }
            else {
                if (loginResult==-2){
                    System.out.println(mylogfile.writeLog("Login failed, MangerID "+ManagerID+" is already online."));
                }
                else System.out.println(mylogfile.writeLog("Login failed, MangerID "+ManagerID+" is invalid."));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void showMenu(String grade)
    {
        if (grade.equals("0")){
            System.out.println("\n****Welcome to ManagerClient****\n");
            System.out.println("Please select an option (1-5)");
            System.out.println("1. Create a Teacher record.");
            System.out.println("2. Create a Student record");
            System.out.println("3. Edit a record");
            System.out.println("4. Get records count");
            System.out.println("5. Transfer a record");
            System.out.println("6. Load test cases");
            System.out.println("7. Exit");
        }
        else if (grade.equals("03")){
            System.out.println("	Please select a 'FieldName' (1-7)");
            System.out.println("	1. Address for Teacher");
            System.out.println("	2. Phone for Teacher");
            System.out.println("	3. Location for Teacher");
            System.out.println("	4. CoursesRegistered for Student");
            System.out.println("	5. Status for Student");
            System.out.println("	6. Status Date for Student");
            System.out.println("	7. Back to upper Student");
        }
        else if (grade.equals("04")){
            System.out.println("	Please select a recordType' (1-4)");
            System.out.println("	1. Teacher records");
            System.out.println("	2. Student records");
            System.out.println("	3. Both records");
            System.out.println("	4. Back to upper menu.");
        }
        else if (grade.equals("05")){
            System.out.println("	Please select a test case' (1-4)");
            System.out.println("	1. 600 threads to insert records");
            System.out.println("	2. 400 threads insert, while 200 threads edit same records");
//            System.out.println("	3. Other test case, reserved");
            System.out.println("	3. Back to upper menu.");
        }
    }
    public static void main(String[] argv) {
        String ManagerID;
        String serverName;
        String myServerHost = argv[1];
        String myServerPort = argv[2];
        ManagerID = argv[0];				//obtain ManagerID
        serverName = ManagerID.substring(0,3);	//Obtain serverName -"MTL LVL DDO"
        //Begin to validate ManagerID
        if ((ManagerID.length()!=7)||(!checkServer(serverName))){
            System.out.println("ManagerID "+ManagerID+" is invalid");
            return;
        }
        try {
            Integer.parseInt(ManagerID.substring(3));
        }catch  (NumberFormatException e) {
            System.out.println("ManagerID "+ManagerID+" is invalid");
            return;
        }
        //initial log file
        logFile mylogfile = new logFile("client\\"+ManagerID+System.currentTimeMillis()+".log");

        //Begin the main part
        try {
            ORB orb = ORB.init(new String[]{"-ORBInitialPort",myServerPort,"-ORBInitialHost",myServerHost},null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContext ncRef = NamingContextHelper.narrow(objRef);

            NameComponent nc = new NameComponent(serverName,"");
            NameComponent path[] = {nc};
            Center ServerRef = CenterHelper.narrow(ncRef.resolve(path));
            //Try to login to the server,avoid Multi-online of same ManagerID
            int loginResult = ServerRef.login(ManagerID);

            if (loginResult==0){
                //login succeed - means ManagerID matches the server and no same ManagerID online
                mylogfile.writeLog("Succeed: "+ManagerID+" login to the "+serverName+" server on "+myServerHost);
                int userChoice=0;
                Scanner keyboard = new Scanner(System.in);

                showMenu("0");	//Display main menu

                while(true)
                {
                    Boolean valid = false;
                    // Enforces a valid integer input.
                    while(!valid)
                    {
                        try{
                            userChoice=keyboard.nextInt();
                            valid=true;
                        }
                        catch(Exception e)
                        {
                            System.out.println("Invalid Input, please enter an Integer");
                            valid=false;
                            keyboard.nextLine();
                        }
                    }
                    // Manage user selection.
                    switch(userChoice)
                    {
                        case 1: { //create a new Teacher record
                            System.out.println("Firstname:");
                            String firstName=keyboard.next();
                            System.out.println("Lastname:");
                            String lastName=keyboard.next();
                            System.out.println("Address:");
                            String address=keyboard.next();
                            System.out.println("Phone:");
                            String phone=keyboard.next();
                            System.out.println("Specialization(e.g. maths,french,etc):");
                            String spec=keyboard.next();
                            System.out.println("location(MTL,LVL,DDO):");
                            String location=keyboard.next();
                            System.out.println(mylogfile.writeLog(ServerRef.createTRecord(firstName,lastName,address,phone,spec,location,ManagerID,0,"")));
                            showMenu("0");
                            break;}
                        case 2:{ //create a new student record
                            System.out.println("Firstname:");
                            String firstName=keyboard.next();
                            System.out.println("Lastname:");
                            String lastName=keyboard.next();
                            keyboard.nextLine();
                            System.out.println("coursesRegistered(Maths/French/Science):");
                            String desig=keyboard.nextLine();
                            System.out.println("Status(active/inactive):");
                            String status=keyboard.next();
                            System.out.println("StatusDate(yyyy-mm-dd)");
                            String statusDate=keyboard.next();
                            System.out.println(mylogfile.writeLog(ServerRef.createSRecord(firstName,lastName,desig,status,statusDate,ManagerID,0,"")));
                            showMenu("0");
                            break;}
                        case 3:{  //edit exiting records
                            System.out.println("RecordID(TR/SR+5 digits):");
                            String recordID=keyboard.next();

                            keyboard.nextLine();

                            showMenu("03"); //show the sub menu to choose field name
                            Boolean valid2 = false;
                            int userChoice2 = 0;
                            // Enforces a valid integer input.
                            while(!valid2)
                            {
                                try{
                                    userChoice2=keyboard.nextInt();

                                    keyboard.nextLine();

                                    valid2=true;
                                }
                                catch(Exception e)
                                {
                                    System.out.println("Invalid Input, please enter an Integer");
                                    valid2=false;
                                    keyboard.nextLine();
                                }
                            }
                            String fieldName="invalid";
                            switch(userChoice2){
                                case 1: fieldName="Address";
                                    break;
                                case 2: fieldName="phoneNum";
                                    break;
                                case 3: fieldName="location";
                                    break;
                                case 4: fieldName="coursesRegistered";
                                    break;
                                case 5: fieldName="status";
                                    break;
                                case 6: fieldName="statusDate";
                                    break;
                                case 7: break;
                                default:
                                    System.out.println("Invalid Input, please try again.");
                            }
                            if (!fieldName.equals("invalid")) {
                                System.out.println("New Value:");
                                String newValue=keyboard.nextLine();
                                System.out.println(mylogfile.writeLog(ServerRef.editRecord(recordID,fieldName,newValue,ManagerID)));
                            }
                            showMenu("0");
                            break;}

                        case 4:{	//get record counts
                            showMenu("04"); //show sub menu to choose record type of get count

                            Boolean valid2 = false;
                            int userChoice2 = 0;
                            // Enforces a valid integer input.
                            while(!valid2)
                            {
                                try{
                                    userChoice2=keyboard.nextInt();
                                    valid2=true;
                                }
                                catch(Exception e)
                                {
                                    System.out.println("Invalid Input, please enter an Integer");
                                    valid2=false;
                                    keyboard.nextLine();
                                }
                            }
                            String recordType="invalid";
                            switch(userChoice2){
                                case 1: recordType="TR";
                                    break;
                                case 2: recordType="SR";
                                    break;
                                case 3: recordType="BO";
                                    break;
                                case 4: break;
                                default:
                                    System.out.println("Invalid Input, please try again.");
                            }
                            if (!recordType.equals("invalid")) {
                                System.out.println(mylogfile.writeLog(ServerRef.getRecordCounts(recordType,ManagerID)));
                            }
                            showMenu("0");
                            break;}

                        case 5:{  //transfer exiting records
                            System.out.println("RecordID(TR/SR+5 digits):");
                            String recordID=keyboard.next();
                            System.out.println("RemoteServer(MTL,LVL,DDO):");
                            String remote = keyboard.next();
                            System.out.println(mylogfile.writeLog(ServerRef.transferRecord(recordID,remote,ManagerID)));
                            showMenu("0");
                            break;}


                        case 6:{	//load pre-defined test cases
                            showMenu("05");
                            Boolean valid2 = false;
                            int userChoice2 = 0;
                            // Enforces a valid integer input.
                            while(!valid2)
                            {
                                try{
                                    userChoice2=keyboard.nextInt();
                                    valid2=true;
                                }
                                catch(Exception e)
                                {
                                    System.out.println("Invalid Input, please enter an Integer");
                                    valid2=false;
                                    keyboard.nextLine();
                                }
                            }
                            String testCaseId="invalid";
                            switch(userChoice2){
                                case 1: testCaseId="1";
                                    for (int i=0;i<200;i++){
                                        (new ManagerClient("MTL"+String.format("%04d",i+1),1,100,myServerHost,myServerPort)).start();
                                        sleep(100);
                                        (new ManagerClient("LVL"+String.format("%04d",i+1),1,100,myServerHost,myServerPort)).start();
                                        sleep(100);
                                        (new ManagerClient("DDO"+String.format("%04d",i+1),1,100,myServerHost,myServerPort)).start();
                                        sleep(100);
                                    }
                                    break;
                                case 2: testCaseId="2";
                                    for (int i=0;i<200;i++){
                                        (new ManagerClient("MTL"+String.format("%04d",i+301),1,150,myServerHost,myServerPort)).start();
                                        sleep(100);
                                    }
                                    for (int i=0;i<200;i=i+2){
                                        (new ManagerClient("MTL"+String.format("%04d",i+501),2,150,myServerHost,myServerPort)).start();
                                        sleep(100);
                                        (new ManagerClient("MTL"+String.format("%04d",i+502),2,150,myServerHost,myServerPort)).start();
                                        sleep(100);
                                    }
                                    break;
                                case 3: break;

                                default:
                                    System.out.println("Invalid Input, please try again.");
                            }
                            if (!testCaseId.equals("invalid")) {
                                System.out.println("Done running a test case!");
                            } else {
                                System.out.println("Failed to start a test case!");
                            }
                            showMenu("0");
                            break;}

                        case 7:	//exit
                            ServerRef.logout(ManagerID);
                            mylogfile.writeLog("Succeed: "+ManagerID+" logout from the "+serverName+" server on "+myServerHost);
                            System.out.println("Have a nice day!");
                            keyboard.close();
                            System.exit(0);
                        default:
                            System.out.println("Invalid Input, please try again.");
                    }//end of switch
                }//end of while
            }//end of if loginResult==0
            else {
                if (loginResult==-2){
                    System.out.println(mylogfile.writeLog("Login failed, MangerID "+ManagerID+" is already online."));
                }
                else System.out.println(mylogfile.writeLog("Login failed, MangerID "+ManagerID+" is invalid."));
            }
        }catch (Exception e){
            mylogfile.writeLog("ERROR: "+ManagerID+" unknown error;");
            System.err.println("ERROR: "+e);
            e.printStackTrace(System.out);
        }

    }
}
