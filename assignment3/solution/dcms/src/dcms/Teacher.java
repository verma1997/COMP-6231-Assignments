package dcms;

/**
 * Teacher Model
 */
public class Teacher extends MemberRecord {
    public String address;
    public String phoneNumber;
    public String specialization;
    public String location;

    private static String[] validFields = {"ADDRESS",
            "PHONENUMBER",
            "SPECIALIZATION",
            "LOCATION"};
    private static String[] validSpecializations = {"MATHS", "FRENCH", "SCIENCE"};
    public static String[] validLocations = {"MTL", "LVL", "DDO"};

    // Initialize a new teacher record
    public int init(String id, String firstName, String lastName, String address, String phoneNumber, String specialization, String location) {

        if (!isBelongList(location.toUpperCase(), validLocations)) {
            return -1;						// Invalid location
        }

        if (id.length() != 5) {
            return -2;						// Invalid ID
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.specialization = specialization.toLowerCase();
        this.location = location.toUpperCase();	// Location will be modified to upper case automatically
        this.recordID = "TR" + id;
        return 0;
    }

    // Referenced by ClassServer.editRecord
    public int editRecord (String fieldName, String newValue) {
        if (!isBelongList(fieldName.toUpperCase(), validFields)) {
            return -1;		// Field invalid or can't be edited
        }

        if(fieldName.equals("location")) {
            if (!isBelongList(newValue.toUpperCase(), validLocations)) {
                return -2;      // Address value is invalid
            } else {
                newValue = newValue.toUpperCase();
            }
        } else if(fieldName.equals("specialization")) {
            if (!isBelongList(newValue.toUpperCase(), validSpecializations)) {
                return -2;      // PhoneNumber value is invalid
            } else {
                newValue = newValue.toUpperCase();
            }
        }

        try{
            this.getClass().getDeclaredField(fieldName).set(this, newValue);
        }catch (Exception e){
            return -3;
        }
        return 0;
    }

    public String getMemberRecord(){
        return recordID + " " + firstName + " " + lastName + " " + address + " " + phoneNumber + " " + specialization + " " + location;
    }
}
