
package dcms;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dcms package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetRecordsCountResponse_QNAME = new QName("http://dcms/", "getRecordsCountResponse");
    private final static QName _TransferRecord_QNAME = new QName("http://dcms/", "transferRecord");
    private final static QName _CreateTRecord_QNAME = new QName("http://dcms/", "createTRecord");
    private final static QName _Login_QNAME = new QName("http://dcms/", "login");
    private final static QName _TransferRecordResponse_QNAME = new QName("http://dcms/", "transferRecordResponse");
    private final static QName _LoginResponse_QNAME = new QName("http://dcms/", "loginResponse");
    private final static QName _LogoutResponse_QNAME = new QName("http://dcms/", "logoutResponse");
    private final static QName _EditRecordResponse_QNAME = new QName("http://dcms/", "editRecordResponse");
    private final static QName _CreateTRecordResponse_QNAME = new QName("http://dcms/", "createTRecordResponse");
    private final static QName _GetRecordsCount_QNAME = new QName("http://dcms/", "getRecordsCount");
    private final static QName _Logout_QNAME = new QName("http://dcms/", "logout");
    private final static QName _CreateSRecordResponse_QNAME = new QName("http://dcms/", "createSRecordResponse");
    private final static QName _EditRecord_QNAME = new QName("http://dcms/", "editRecord");
    private final static QName _CreateSRecord_QNAME = new QName("http://dcms/", "createSRecord");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dcms
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LoginResponse }
     * 
     */
    public LoginResponse createLoginResponse() {
        return new LoginResponse();
    }

    /**
     * Create an instance of {@link LogoutResponse }
     * 
     */
    public LogoutResponse createLogoutResponse() {
        return new LogoutResponse();
    }

    /**
     * Create an instance of {@link GetRecordsCountResponse }
     * 
     */
    public GetRecordsCountResponse createGetRecordsCountResponse() {
        return new GetRecordsCountResponse();
    }

    /**
     * Create an instance of {@link TransferRecord }
     * 
     */
    public TransferRecord createTransferRecord() {
        return new TransferRecord();
    }

    /**
     * Create an instance of {@link CreateTRecord }
     * 
     */
    public CreateTRecord createCreateTRecord() {
        return new CreateTRecord();
    }

    /**
     * Create an instance of {@link Login }
     * 
     */
    public Login createLogin() {
        return new Login();
    }

    /**
     * Create an instance of {@link TransferRecordResponse }
     * 
     */
    public TransferRecordResponse createTransferRecordResponse() {
        return new TransferRecordResponse();
    }

    /**
     * Create an instance of {@link GetRecordsCount }
     * 
     */
    public GetRecordsCount createGetRecordsCount() {
        return new GetRecordsCount();
    }

    /**
     * Create an instance of {@link Logout }
     * 
     */
    public Logout createLogout() {
        return new Logout();
    }

    /**
     * Create an instance of {@link CreateSRecordResponse }
     * 
     */
    public CreateSRecordResponse createCreateSRecordResponse() {
        return new CreateSRecordResponse();
    }

    /**
     * Create an instance of {@link EditRecord }
     * 
     */
    public EditRecord createEditRecord() {
        return new EditRecord();
    }

    /**
     * Create an instance of {@link CreateSRecord }
     * 
     */
    public CreateSRecord createCreateSRecord() {
        return new CreateSRecord();
    }

    /**
     * Create an instance of {@link EditRecordResponse }
     * 
     */
    public EditRecordResponse createEditRecordResponse() {
        return new EditRecordResponse();
    }

    /**
     * Create an instance of {@link CreateTRecordResponse }
     * 
     */
    public CreateTRecordResponse createCreateTRecordResponse() {
        return new CreateTRecordResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecordsCountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcms/", name = "getRecordsCountResponse")
    public JAXBElement<GetRecordsCountResponse> createGetRecordsCountResponse(GetRecordsCountResponse value) {
        return new JAXBElement<GetRecordsCountResponse>(_GetRecordsCountResponse_QNAME, GetRecordsCountResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcms/", name = "transferRecord")
    public JAXBElement<TransferRecord> createTransferRecord(TransferRecord value) {
        return new JAXBElement<TransferRecord>(_TransferRecord_QNAME, TransferRecord.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateTRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcms/", name = "createTRecord")
    public JAXBElement<CreateTRecord> createCreateTRecord(CreateTRecord value) {
        return new JAXBElement<CreateTRecord>(_CreateTRecord_QNAME, CreateTRecord.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Login }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcms/", name = "login")
    public JAXBElement<Login> createLogin(Login value) {
        return new JAXBElement<Login>(_Login_QNAME, Login.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferRecordResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcms/", name = "transferRecordResponse")
    public JAXBElement<TransferRecordResponse> createTransferRecordResponse(TransferRecordResponse value) {
        return new JAXBElement<TransferRecordResponse>(_TransferRecordResponse_QNAME, TransferRecordResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcms/", name = "loginResponse")
    public JAXBElement<LoginResponse> createLoginResponse(LoginResponse value) {
        return new JAXBElement<LoginResponse>(_LoginResponse_QNAME, LoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogoutResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcms/", name = "logoutResponse")
    public JAXBElement<LogoutResponse> createLogoutResponse(LogoutResponse value) {
        return new JAXBElement<LogoutResponse>(_LogoutResponse_QNAME, LogoutResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EditRecordResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcms/", name = "editRecordResponse")
    public JAXBElement<EditRecordResponse> createEditRecordResponse(EditRecordResponse value) {
        return new JAXBElement<EditRecordResponse>(_EditRecordResponse_QNAME, EditRecordResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateTRecordResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcms/", name = "createTRecordResponse")
    public JAXBElement<CreateTRecordResponse> createCreateTRecordResponse(CreateTRecordResponse value) {
        return new JAXBElement<CreateTRecordResponse>(_CreateTRecordResponse_QNAME, CreateTRecordResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecordsCount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcms/", name = "getRecordsCount")
    public JAXBElement<GetRecordsCount> createGetRecordsCount(GetRecordsCount value) {
        return new JAXBElement<GetRecordsCount>(_GetRecordsCount_QNAME, GetRecordsCount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Logout }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcms/", name = "logout")
    public JAXBElement<Logout> createLogout(Logout value) {
        return new JAXBElement<Logout>(_Logout_QNAME, Logout.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateSRecordResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcms/", name = "createSRecordResponse")
    public JAXBElement<CreateSRecordResponse> createCreateSRecordResponse(CreateSRecordResponse value) {
        return new JAXBElement<CreateSRecordResponse>(_CreateSRecordResponse_QNAME, CreateSRecordResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EditRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcms/", name = "editRecord")
    public JAXBElement<EditRecord> createEditRecord(EditRecord value) {
        return new JAXBElement<EditRecord>(_EditRecord_QNAME, EditRecord.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateSRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dcms/", name = "createSRecord")
    public JAXBElement<CreateSRecord> createCreateSRecord(CreateSRecord value) {
        return new JAXBElement<CreateSRecord>(_CreateSRecord_QNAME, CreateSRecord.class, null, value);
    }

}
