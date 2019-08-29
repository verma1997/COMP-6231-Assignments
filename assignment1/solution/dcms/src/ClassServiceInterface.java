import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Define the interface of the distributed class management system service
 * It must inherit Remote and methods must throw RemoteException
 */
public interface ClassServiceInterface extends Remote {

    public String createTRecord(String teacherID, String firstName, String lastName, String address, String phoneNumber, String specializations, String location) throws RemoteException;

    public String createSRecord(String studentID, String firstName, String lastName, String courseRegistered, String status, String statusDate) throws RemoteException;

    public String getRecordsCount(String recordType, String memberID) throws RemoteException;

    public String editRecord(String recordID, String fieldName, String newValue, String memberID) throws RemoteException;

    public int login(String managerID) throws RemoteException;

    public int logout(String managerID) throws RemoteException;

}
