package dcms;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface Member_Webservice {
    @WebMethod
    // The methode is to create a teacher's record
    public String createTRecord(String teacherID, String firstName, String lastName, String address, String phoneNumber, String specializations, String location, String managerID, int mode);
    @WebMethod
    // The methode is to create a student's record
    public String createSRecord(String studentID, String firstName, String lastName, String courseRegistered, String status, String statusDate, String managerID, int mode);
    @WebMethod
    // The methode is to get the number of records on the server
    public String getRecordsCount(String recordType, String memberID);
    @WebMethod
    // The methode is to edit a record on the specified server
    public String editRecord(String recordID, String fieldName, String newValue, String memberID);
    @WebMethod
    // The methode is to transfer a record from one server to another server
    public String transferRecord(String recordID, String remoteCenterServer, String managerID);
    @WebMethod
    public int login(String managerID);
    @WebMethod
    public int logout(String managerID);
}
