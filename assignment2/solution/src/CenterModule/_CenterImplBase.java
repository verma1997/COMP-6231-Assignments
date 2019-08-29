package CenterModule;

public abstract class _CenterImplBase extends org.omg.CORBA.portable.ObjectImpl
                implements CenterModule.Center, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors
  public _CenterImplBase(){}

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("createTRecord", new java.lang.Integer (0));
    _methods.put ("createSRecord", new java.lang.Integer (1));
    _methods.put ("getRecordCounts", new java.lang.Integer (2));
    _methods.put ("editRecord", new java.lang.Integer (3));
    _methods.put ("transferRecord", new java.lang.Integer (4));
    _methods.put ("login", new java.lang.Integer (5));
    _methods.put ("logout", new java.lang.Integer (6));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:
       {
         String firstName = in.read_string ();
         String lastName = in.read_string ();
         String address = in.read_string ();
         String phone = in.read_string ();
         String specialization = in.read_string ();
         String location = in.read_string ();
         String ManagerID = in.read_string ();
         int mode = in.read_long ();
         String recordID = in.read_string ();
         String $result = null;
         $result = this.createTRecord (firstName, lastName, address, phone, specialization, location, ManagerID, mode, recordID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 1:
       {
         String firstName = in.read_string ();
         String lastName = in.read_string ();
         String coursesRegistered = in.read_string ();
         String status = in.read_string ();
         String statusDate = in.read_string ();
         String ManagerID = in.read_string ();
         int mode = in.read_long ();
         String recordID = in.read_string ();
         String $result = null;
         $result = this.createSRecord (firstName, lastName, coursesRegistered, status, statusDate, ManagerID, mode, recordID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 2:
       {
         String recordType = in.read_string ();
         String ManagerID = in.read_string ();
         String $result = null;
         $result = this.getRecordCounts (recordType, ManagerID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 3:
       {
         String recordID = in.read_string ();
         String fieldName = in.read_string ();
         String newValue = in.read_string ();
         String ManagerID = in.read_string ();
         String $result = null;
         $result = this.editRecord (recordID, fieldName, newValue, ManagerID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 4:
       {
         String recordID = in.read_string ();
         String remoteCenterServer = in.read_string ();
         String ManagerID = in.read_string ();
         String $result = null;
         $result = this.transferRecord (recordID, remoteCenterServer, ManagerID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 5:
       {
         String ManagerID = in.read_string ();
         int $result = (int)0;
         $result = this.login (ManagerID);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 6:
       {
         String ManagerID = in.read_string ();
         int $result = (int)0;
         $result = this.logout (ManagerID);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:CenterModule/Center:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }


}
