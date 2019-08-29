package Core;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement StudentRecord Class and inherit base class memberRecord
 */
public class StudentRecord extends memberRecord {

    List<String> where = new ArrayList<String>();
    public String [] courselist;
    public String coursesRegistered;
    public String status;
    public String statusDate;
    private static String [] validFields = {"coursesRegistered", "status", "statusDate"};
    private static String [] validCourse = {"MATHS", "FRENCH", "SCIENCE"};
    private static String [] validStatus = {"ACTIVE", "INACTIVE"};

    //Init new student record
    public int init(String first, String last, String[] course, String st, String stDate, String ID){
        for (String eachCourse: course) {
            if((!belong(eachCourse.toUpperCase(), validCourse))||(!belong(st.toUpperCase(),validStatus))){
                return -1;
            }

        }

        if (ID.length()!=5){
            return -2;
        }
        this.firstName = first;
        this.lastName = last;

        for (String eachCourse: course) {
            where.add(eachCourse.toLowerCase());
        }
        this.courselist = new String[ where.size() ];
        where.toArray(courselist);

        this.coursesRegistered = String.join(" ", courselist);
        //
        this.status = st.toLowerCase();
        this.statusDate = stDate;
        this.recordID = "SR" + ID;

        return 0;
    }

    //CenterServer.editRecord
    public int editRecord(String fileName, String newValue) {

        String [] newCourse = newValue.split(" ");
        if (!belong(fileName, validFields)) {
            return -1;
        }


        if (fileName.equals("coursesRegistered")){
            for (String newC: newCourse){
                if (!belong(newC.toUpperCase(), validCourse))   return -2;
                else newValue = newValue.toLowerCase();
            }

        }
        else if (fileName.equals("status")){
            if (!belong(newValue.toUpperCase(),validStatus))    return -2;
            else newValue = newValue.toLowerCase();
        }
        try{
            this.getClass().getDeclaredField(fileName).set(this, newValue);
        }catch (Exception e){
            return -3;
        }
        return 0;
    }

    public String getRecord(){
        return recordID + " " + firstName + " " + lastName + " " + coursesRegistered + " " + status + " " + statusDate;
    }

}
