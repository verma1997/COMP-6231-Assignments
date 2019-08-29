
package dcms;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "Member_Webservice", targetNamespace = "http://dcms/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface MemberWebservice {


    /**
     * 
     * @param arg3
     * @param arg2
     * @param arg5
     * @param arg4
     * @param arg1
     * @param arg0
     * @param arg7
     * @param arg6
     * @param arg8
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "createTRecord", targetNamespace = "http://dcms/", className = "dcms.CreateTRecord")
    @ResponseWrapper(localName = "createTRecordResponse", targetNamespace = "http://dcms/", className = "dcms.CreateTRecordResponse")
    @Action(input = "http://dcms/Member_Webservice/createTRecordRequest", output = "http://dcms/Member_Webservice/createTRecordResponse")
    public String createTRecord(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        String arg4,
        @WebParam(name = "arg5", targetNamespace = "")
        String arg5,
        @WebParam(name = "arg6", targetNamespace = "")
        String arg6,
        @WebParam(name = "arg7", targetNamespace = "")
        String arg7,
        @WebParam(name = "arg8", targetNamespace = "")
        int arg8);

    /**
     * 
     * @param arg3
     * @param arg2
     * @param arg5
     * @param arg4
     * @param arg1
     * @param arg0
     * @param arg7
     * @param arg6
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "createSRecord", targetNamespace = "http://dcms/", className = "dcms.CreateSRecord")
    @ResponseWrapper(localName = "createSRecordResponse", targetNamespace = "http://dcms/", className = "dcms.CreateSRecordResponse")
    @Action(input = "http://dcms/Member_Webservice/createSRecordRequest", output = "http://dcms/Member_Webservice/createSRecordResponse")
    public String createSRecord(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        String arg4,
        @WebParam(name = "arg5", targetNamespace = "")
        String arg5,
        @WebParam(name = "arg6", targetNamespace = "")
        String arg6,
        @WebParam(name = "arg7", targetNamespace = "")
        int arg7);

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getRecordsCount", targetNamespace = "http://dcms/", className = "dcms.GetRecordsCount")
    @ResponseWrapper(localName = "getRecordsCountResponse", targetNamespace = "http://dcms/", className = "dcms.GetRecordsCountResponse")
    @Action(input = "http://dcms/Member_Webservice/getRecordsCountRequest", output = "http://dcms/Member_Webservice/getRecordsCountResponse")
    public String getRecordsCount(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1);

    /**
     * 
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "editRecord", targetNamespace = "http://dcms/", className = "dcms.EditRecord")
    @ResponseWrapper(localName = "editRecordResponse", targetNamespace = "http://dcms/", className = "dcms.EditRecordResponse")
    @Action(input = "http://dcms/Member_Webservice/editRecordRequest", output = "http://dcms/Member_Webservice/editRecordResponse")
    public String editRecord(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3);

    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "transferRecord", targetNamespace = "http://dcms/", className = "dcms.TransferRecord")
    @ResponseWrapper(localName = "transferRecordResponse", targetNamespace = "http://dcms/", className = "dcms.TransferRecordResponse")
    @Action(input = "http://dcms/Member_Webservice/transferRecordRequest", output = "http://dcms/Member_Webservice/transferRecordResponse")
    public String transferRecord(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2);

    /**
     * 
     * @param arg0
     * @return
     *     returns int
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "login", targetNamespace = "http://dcms/", className = "dcms.Login")
    @ResponseWrapper(localName = "loginResponse", targetNamespace = "http://dcms/", className = "dcms.LoginResponse")
    @Action(input = "http://dcms/Member_Webservice/loginRequest", output = "http://dcms/Member_Webservice/loginResponse")
    public int login(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns int
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "logout", targetNamespace = "http://dcms/", className = "dcms.Logout")
    @ResponseWrapper(localName = "logoutResponse", targetNamespace = "http://dcms/", className = "dcms.LogoutResponse")
    @Action(input = "http://dcms/Member_Webservice/logoutRequest", output = "http://dcms/Member_Webservice/logoutResponse")
    public int logout(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

}