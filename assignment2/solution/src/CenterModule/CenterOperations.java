package CenterModule;


/**
* CenterModule/CenterOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Center.idl
* Friday, June 8, 2018 5:26:10 PM EDT
*/

public interface CenterOperations 
{
  String createTRecord (String firstName, String lastName, String address, String phone, String specialization, String location, String ManagerID, int mode, String recordID);
  String createSRecord (String firstName, String lasName, String coursesRegistered, String status, String statusDate, String ManagerID, int mode, String recordID);
  String getRecordCounts (String recordType, String ManagerID);
  String editRecord (String recordID, String filedName, String newValue, String ManagerID);
  String transferRecord (String recordID, String remoteCenterServer, String ManagerID);
  int login (String ManagerID);
  int logout (String ManagerID);
} // interface CenterOperations
