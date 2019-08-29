/**
 * Teacher Model
 */
public class Teacher extends Member {
    private String address;
    private String phoneNumber;
    private String specialization;
    private String location;

    private static String[] validFields = {"ADDRESS",
            "PHONENUMBER",
            "SPECIALIZATION",
            "LOCATION"};
    private static String[] validSpecializations = {"MATHS", "FRENCH", "SCIENCE"};
    public static String[] validLocations = {"MTL", "LVL", "DDO"};

    //initialize a new teacher record
    public int init(String id, String firstName, String lastName, String address, String phoneNumber, String specialization, String location) {

        if (!isBelongList(location.toUpperCase(), validLocations)) {
            return -1;						//invalid location
        }

        if (id.length() != 5) {
            return -2;						//invalid ID
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.specialization = specialization.toLowerCase();
        this.location = location.toUpperCase();	//location will be modified to upper case automatically
        this.recordID = "TR" + id;
        return 0;
    }

    //referenced by ClassServer.editRecord
    public int editRecord (String fieldName, String newValue) {
        if (!isBelongList(fieldName.toUpperCase(), validFields)) {
            return -1;		//field invalid or can't be edited
        }

        if(fieldName.equals("location")) {
            if (!isBelongList(newValue.toUpperCase(), validLocations)) {
                return -2;      // address value is invalid
            } else {
                newValue = newValue.toUpperCase();
            }
        } else if(fieldName.equals("specialization")) {
            if (!isBelongList(newValue.toUpperCase(), validSpecializations)) {
                return -2;      // phoneNumber value is invalid
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
