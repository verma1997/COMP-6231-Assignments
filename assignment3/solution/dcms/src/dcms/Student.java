package dcms;

import java.util.Arrays;

/**
 * Student Model
 */
public class Student extends MemberRecord {
    public String courseRegistered;
    public String status;
    public String statusDate;
    private static String[] validFields = {"COURSEREGISTERED",
            "STATUS",
            "STATUSDATE"};
    private static String[] validCourses = {"MATHS", "FRENCH", "SCIENCE"};
    private static String[] validStatus = {"ACTIVE", "INACTIVE"};

    // Initialize a new student record
    public int init(String id, String firstName, String lastName, String courseRegistered, String status, String statusDate) {
        String[] courses = courseRegistered.toUpperCase().split(" ");
        if ((!(Arrays.asList(validCourses).containsAll(Arrays.asList(courses)))) || (!isBelongList(status.toUpperCase(), validStatus))) {
            return -1;						// Invalid courseRegistered or status
        }
        if (id.length()!=5) {
            return -2;						// Invalid ID
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.courseRegistered = courseRegistered.toLowerCase();
        this.status = status.toLowerCase();
        this.statusDate = statusDate;
        this.recordID = "SR" + id;
        return 0;
    }

    // Referenced by CenterServer.editRecord
    public int editRecord (String fieldName, String newValue) {
        if(!isBelongList(fieldName.toUpperCase(), validFields)) {
            return -1;		// Field invalid or can't be edited
        }

        if(fieldName.equals("courseRegistered")) {
            if (!isBelongList(newValue.toUpperCase(), validCourses)) {
                return -2;		// CourseRegistered value is invalid
            } else {
                newValue = newValue.toLowerCase();
            }
        } else if(fieldName.equals("status")) {
            if (!isBelongList(newValue.toUpperCase(),validStatus)) {
                return -2;      // Status value is invalid
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
