package Core;

/**
 * Base class of StudentRecord and TeacherRecord
 */
public class memberRecord {
    protected String firstName;
    protected String lastName;
    protected String recordID;

    protected static boolean belong(String str, String [] validStrings){
        for (String temp: validStrings) {
            if (temp.equals((str))){
                return (true);
            }
        }
        return (false);
    }

    public String getID(){  return this.recordID; }

}
