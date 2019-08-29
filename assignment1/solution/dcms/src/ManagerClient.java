import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

public class ManagerClient extends Thread {
    private static String client_logs_path = "logs/client_logs/";
    private static String datePattern = "yyyy-MM-dd E hh:mm:ss";
    private static Calendar calendar = Calendar.getInstance();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

    private static String hostMTL = "localhost";
    private static String hostLVL = "localhost";
    private static String hostDDO = "localhost";

    private String managerID;
    private String serverName;
    private String serverHost;

    private int operationType;

    private int loopCount;

    private static String[] OPERATION_OPTIONS = {"Create a teacher record.",
            "Create a student record.",
            "Check the number of records.",
            "Edit a record.",
            "Load pre-test cases",
            "Exit"};
    private static String[] FIELD_NAME_OPTIONS = {"The Address for Teacher: ",
            "The Phone Number for Teacher: ",
            "The Specializations for Teacher: ",
            "The Location for Teacher: ",
            "The Courses Registered for Student: ",
            "The Status for Student: ",
            "The Status Date ( " + datePattern + " ) for Student: ",
            "Back to previous menu"};
    private static String[] RECORD_TYPE_OPTIONS = {"Teacher records",
            "Student records",
            "Both Teacher & Student records",
            "Back to previous menu"};

    private static String[] PRE_TESTS_OPTIONS = {"900 threads to insert records",
            "400 threads insert, while 200 threads edit same records",
            "Back to previous menu"};

    public ManagerClient(String managerID, int operationType, int loopCount) {
        this.managerID = managerID;
        this.serverName = managerID.substring(0, 3);
        this.operationType = operationType;
        this.loopCount = loopCount;

        if(serverName.equals("MTL")) {
            serverHost = hostMTL;
        } else if(serverName.equals("LVL")) {
            serverHost = hostLVL;
        } else {
            serverHost = hostDDO;
        }
    }

    public static boolean checkServer(String serverName) {
        return ((serverName.equals("MTL"))||(serverName.equals("LVL"))||(serverName.equals("DDO")));
    }


    public static void showMenu(String choice) {
        if(choice.equals("00")) {
            System.out.println("\n--------------------------------------------------");
            System.out.println("\nWelcome to DCMS System. The available options are:\n");

            for(int i = 0; i < OPERATION_OPTIONS.length; i++) {
                System.out.println((i + 1) + "> " + OPERATION_OPTIONS[i]);
            }

            System.out.println("\nPlease select an option (1 - " + OPERATION_OPTIONS.length + " ): ");

        } else if(choice.equals("01")) {
            System.out.println("\nPls select a Record Type. The available options are:\n");

            for(int i = 0; i < RECORD_TYPE_OPTIONS.length; i++) {
                System.out.println((i + 1) + "> " + RECORD_TYPE_OPTIONS[i]);
            }

            System.out.println("\nPlease select an option (1 - " + RECORD_TYPE_OPTIONS.length + " ): ");

        } else if(choice.equals("02")) {
            System.out.println("\nPls select a Field Name. The available options are:\n");

            for(int i = 0; i < FIELD_NAME_OPTIONS.length; i++) {
                System.out.println((i + 1) + "> " + FIELD_NAME_OPTIONS[i]);
            }

            System.out.println("\nPlease select an option (1 - " + FIELD_NAME_OPTIONS.length + " ): ");

        } else if(choice.equals("03")) {
            System.out.println("\nPlease select a pre-test case. The available options are:\n");

            for(int i = 0; i < PRE_TESTS_OPTIONS.length; i++) {
                System.out.println((i + 1) + "> " + PRE_TESTS_OPTIONS[i]);
            }

            System.out.println("\nPlease select an option (1 - " + PRE_TESTS_OPTIONS.length + " ): ");

        }
    }


    public static int optionChoice(Scanner keyboard) {
        int userChoice = 0;
        Boolean valid = false;
        while(!valid) {
            try {
                userChoice = keyboard.nextInt();
                valid = true;
            } catch(Exception e) {
                System.out.println("Invalid Input, please enter an Integer: ");
                valid = false;
                keyboard.nextLine();
            }
        }
        return userChoice;
    }

    public void run(){
        LogFile logFile = new LogFile(client_logs_path + "client_" + managerID + "_" + System.currentTimeMillis()+".log");
        ClassServiceInterface server;
        try{
//            System.setSecurityManager(new SecurityManager());
//            System.setProperty("java.security.policy", ".");
            server = (ClassServiceInterface)Naming.lookup("rmi://" + serverHost + ":2020/" + serverName);

            int loginResult = server.login(managerID);

            if (loginResult==0){  //login succeed - means ManagerID matches the server and no same ManagerID online
                logFile.writeLog("Succeed: " + managerID + " login to the " + serverName + " server on " + serverHost);

                switch(operationType) {
                    case 1:{	//insert operation
                        Random rn = new Random();
                        for (int i = 0; i < loopCount; i++){
                            int tt = rn.nextInt(26);
                            char a = (char)((int)'A'+tt);
                            try{
                                System.out.println(logFile.writeLog(server.createTRecord(managerID, "Lei", a + "u","abc123","1234567890", "French", serverName)));
                            }catch (Exception e){
                                System.out.println(logFile.writeLog(e.getMessage()));
                            }
                            try{
                                System.out.println(logFile.writeLog(server.createSRecord(managerID,"Chongwen", a + "i", "Maths","active","2018-06-01")));
                            }catch (Exception e){
                                System.out.println(logFile.writeLog(e.getMessage()));
                            }
                            sleep(1);
                        }
                        break;}
                    case 2:{ //edit records
                        for (int i = 0; i < loopCount; i++){
                            if ((i%2)==0) {
                                try {
                                    System.out.println(logFile.writeLog(server.editRecord("TR00001","location","MTL", managerID)));
                                }catch (Exception e){
                                    System.out.println(logFile.writeLog(e.getMessage()));
                                }
                                try {
                                    System.out.println(logFile.writeLog(server.editRecord("SR00001","status","ACTIVE", managerID)));
                                }catch (Exception e){
                                    System.out.println(logFile.writeLog(e.getMessage()));
                                }
                            }
                            else {
                                try {
                                    System.out.println(logFile.writeLog(server.editRecord("TR00001","location","DDO", managerID)));
                                }catch (Exception e){
                                    System.out.println(logFile.writeLog(e.getMessage()));
                                }
                                try {
                                    System.out.println(logFile.writeLog(server.editRecord("SR00001","status","INACTIVE", managerID)));
                                }catch (Exception e){
                                    System.out.println(logFile.writeLog(e.getMessage()));
                                }
                            }
                            sleep(1);
                        }
                        break;}
                    case 3:{  //get counts
                        for (int i = 0; i < loopCount; i++){
                            try{
                                System.out.println(logFile.writeLog(server.getRecordsCount("BO", managerID)));
                            }catch (Exception e){
                                System.out.println(logFile.writeLog(e.getMessage()));
                            }
                            sleep(1);
                        }
                        break;}
                }// end of switch
                server.logout(managerID);
                logFile.writeLog("Succeed: " + managerID+" logout from the " + serverName + " server on " + serverHost);
            }
            else {
                if (loginResult == -2){
                    System.out.println(logFile.writeLog("Login failed, MangerID " + managerID + " is already online."));
                }
                else System.out.println(logFile.writeLog("Login failed, MangerID " + managerID + " is invalid."));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String managerID;
        String serverName;
        String serverHost;
        ClassServiceInterface server;
        managerID = args[0];
        serverName = managerID.substring(0, 3);

        if((managerID.length() != 7) || (!checkServer(serverName))) {
            System.out.println("The Manager ID is invalid");
            return;
        }

        try {
            Integer.parseInt(managerID.substring(3));
        } catch (NumberFormatException e) {
            System.out.println("The Manager ID is invalid");
            return;
        }

        LogFile logFile = new LogFile(client_logs_path + "client_" + managerID + "_" + System.currentTimeMillis() + ".log");

        if(serverName.equals("MTL")) {
            serverHost = hostMTL;
        } else if(serverName.equals("LVL")) {
            serverHost = hostLVL;
        } else {
            serverHost = hostDDO;
        }

        try {
//            System.setSecurityManager(new SecurityManager());
            server = (ClassServiceInterface) Naming.lookup("rmi://" + serverHost + ":2020/" + serverName);
            int loginResult = server.login(managerID);

            if(loginResult == 0) {
                logFile.writeLog("Succeed: " + managerID + " login to the " + serverName + " server on " + serverHost);
                int userChoice = 0;
                Scanner keyboard = new Scanner(System.in);

                // Display Main Menu
                showMenu("00");

                while(true) {
                    userChoice = optionChoice(keyboard);
//                    Boolean valid = false;
//                    while(!valid) {
//                        try {
//                            userChoice = keyboard.nextInt();
//                            valid = true;
//                        } catch(Exception e) {
//                            System.out.println("Invalid Input, please enter an Integer: ");
//                            valid = false;
//                            keyboard.nextLine();
//                        }
//                    }

                    switch(userChoice)
                    {
                        case 1: {
                            System.out.println("\nPls enter following values of a teacher's record: \n");
                            System.out.println("Firstname:");
                            String firstName = keyboard.next();

                            System.out.println("Lastname:");
                            String lastName = keyboard.next();

                            System.out.println("Address:");
                            String address = keyboard.next();

                            System.out.println("Phone Number:");
                            String phoneNumber = keyboard.next();

                            System.out.println("Specialization (e.g. French, Maths, etc): ");
                            String specializations = keyboard.next();

                            System.out.println("Location (MTL,LVL,DDO): ");
                            String location = keyboard.next();

                            System.out.println("\n" + logFile.writeLog(server.createTRecord(managerID, firstName, lastName, address, phoneNumber, specializations, location)));
                            showMenu("00");
                            break;}
                        case 2:{
                            System.out.println("\nPls enter following values of a student's record: \n");
                            System.out.println("Firstname:");
                            String firstName = keyboard.next();

                            System.out.println("Lastname:");
                            String lastName = keyboard.next();
                            keyboard.nextLine();

                            System.out.println("CoursesRegistered (Maths/French/Science, note that student could be registered for multiple courses, separate courses with an space): ");
                            String coursesRegistered = keyboard.nextLine();

                            System.out.println("Status (Active/Inactive): ");
                            String status = keyboard.next();
                            String statusDate = dateFormat.format(calendar.getTime());

//                            System.out.println("StatusDate ( " + datePattern + " ): ");
//                            String statusDate = keyboard.next();

                            System.out.println(logFile.writeLog(server.createSRecord(managerID, firstName, lastName, coursesRegistered, status, statusDate)));
                            showMenu("00");
                            break;}
                        case 3:{ // Get record count
                            showMenu("01"); //show sub menu to choose record type of get count
                            //1-only TR,2-only SR,3-Both

                            int userChoice2 = 0;
                            userChoice2 = optionChoice(keyboard);
                            // Enforces a valid integer input.
//                            Boolean valid2 = false;
//                            while(!valid2)
//                            {
//                                try{
//                                    userChoice2=keyboard.nextInt();
//                                    valid2=true;
//                                }
//                                catch(Exception e)
//                                {
//                                    System.out.println("Invalid Input, please enter an Integer");
//                                    valid2=false;
//                                    keyboard.nextLine();
//                                }
//                            }

                            String recordType="invalid";
                            switch(userChoice2) {
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
                                System.out.println(logFile.writeLog(server.getRecordsCount(recordType, managerID)));
                            }
                            showMenu("00");
                            break;}

                        case 4:{	// Edit record
                            System.out.println("RecordID (TR/SR with 5 digits): ");
                            String recordID = keyboard.next();

                            showMenu("02");

                            int userChoice2 = 0;
                            userChoice2 = optionChoice(keyboard);

                            // Enforces a valid integer input.
//                            Boolean valid2 = false;
//                            while(!valid2)
//                            {
//                                try{
//                                    userChoice2 = keyboard.nextInt();
//                                    valid2 = true;
//                                }
//                                catch(Exception e)
//                                {
//                                    System.out.println("Invalid Input, please enter an Integer: ");
//                                    valid2 = false;
//                                    keyboard.nextLine();
//                                }
//                            }

                            String fieldName="invalid";

                            switch(userChoice2) {
                                case 1:
                                    fieldName = "address";
                                    break;
                                case 2:
                                    fieldName = "phoneNumber";
                                    break;
                                case 3:
                                    fieldName = "specialization";
                                    break;
                                case 4:
                                    fieldName = "location";
                                    break;
                                case 5:
                                    fieldName = "courseRegistered";
                                    break;
                                case 6:
                                    fieldName = "status";
                                    break;
                                case 7:
                                    fieldName = "statusDate";
                                    break;
                                case 8:
                                    break;
                                default:
                                    System.out.println("Invalid Input, please try again.");
                            }
                            if (!fieldName.equals("invalid")) {
                                System.out.println("New Value:");
                                String newValue=keyboard.next();
                                System.out.println(logFile.writeLog(server.editRecord(recordID, fieldName, newValue, managerID)));
                            }
                            showMenu("00");
                            break;}
                        case 5:{
                            showMenu("03");

                            int userChoice2 = 0;
                            userChoice2 = optionChoice(keyboard);

                            String testCaseId="invalid";
                            switch(userChoice2) {
                                case 1:
                                    testCaseId="1";
                                    for (int i=0;i<300;i++){
                                        (new ManagerClient("MTL"+String.format("%04d",i+1),1,112)).run();
                                        (new ManagerClient("LVL"+String.format("%04d",i+1),1,112)).run();
                                        (new ManagerClient("DDO"+String.format("%04d",i+1),1,112)).run();
                                        sleep(2);
                                    }
                                    break;
                                case 2:
                                    testCaseId="2";
                                    for (int i=0;i<200;i++){
                                        (new ManagerClient("MTL"+String.format("%04d",i+301),1,150)).run();
                                        sleep(2);
                                    }
                                    for (int i=0;i<200;i=i+2){
                                        (new ManagerClient("MTL"+String.format("%04d",i+501),2,150)).run();
                                        (new ManagerClient("MTL"+String.format("%04d",i+502),2,150)).run();
                                        sleep(2);
                                    }
                                    break;
                                case 3:
                                    break;
                                default:
                                    System.out.println("Invalid Input, please try again.");
                            }
                            if (!testCaseId.equals("invalid")) {
                                System.out.println("Done running a test case!");
                            } else {
                                System.out.println("Failed to start a test case");
                            }
                            showMenu("00");
                            break;}
                        case 6: //exit
                            server.logout(managerID);
                            logFile.writeLog("Succeed: " + managerID + " logout from the " + serverName + " server on " + serverHost);
                            System.out.printf("\nLogged out the DCMS system. Have a nice day!");
                            keyboard.close();
                            System.exit(0);
                        default:
                            System.out.println("Invalid Input, please try again.");
                    }
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
