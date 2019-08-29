import java.util.Arrays;

/**
 * Student Model
 */
public class Student extends Member {
    private String courseRegistered;
    private String status;
    private String statusDate;
    private static String[] validFields = {"COURSEREGISTERED",
            "STATUS",
            "STATUSDATE"};
    private static String[] validCourses = {"MATHS", "FRENCH", "SCIENCE"};
    private static String[] validStatus = {"ACTIVE", "INACTIVE"};

    //initialize a new student record
    public int init(String id, String firstName, String lastName, String courseRegistered, String status, String statusDate) {
        String[] courses = courseRegistered.toUpperCase().split(" ");
        if ((!(Arrays.asList(validCourses).containsAll(Arrays.asList(courses)))) || (!isBelongList(status.toUpperCase(), validStatus))) {
            return -1;						//invalid courseRegistered or status
        }
        if (id.length()!=5) {
            return -2;						//invalid ID
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.courseRegistered = courseRegistered.toLowerCase();
        this.status = status.toLowerCase();
        this.statusDate = statusDate;
        this.recordID = "SR" + id;
        return 0;
    }

    //referenced by ClassServer.editRecord
    public int editRecord (String fieldName, String newValue) {
        if(!isBelongList(fieldName.toUpperCase(), validFields)) {
            return -1;		//field invalid or can't be edited
        }

        if(fieldName.equals("courseRegistered")) {
            if (!isBelongList(newValue.toUpperCase(), validCourses)) {
                return -2;		// courseRegistered value is invalid
            } else {
                newValue = newValue.toLowerCase();
            }
        } else if(fieldName.equals("status")) {
            if (!isBelongList(newValue.toUpperCase(),validStatus)) {
                return -2;      // status value is invalid
            } else {
                newValue = newValue.toLowerCase();
            }
        }

        try {
            this.getClass().getDeclaredField(fieldName).set(this, newValue);
        } catch(Exception e) {
            return -3;
        }
        return 0;
    }

    public String getMemberRecord() {
        return recordID + " " + firstName + " " + lastName + " " + courseRegistered + " " + status + " " + statusDate;
    }
}
