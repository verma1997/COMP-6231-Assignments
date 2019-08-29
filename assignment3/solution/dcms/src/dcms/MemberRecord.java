package dcms;

/**
 * Base class of TeacherRecord and StudentRecord
 */
public class MemberRecord {
    protected String firstName;
    protected String lastName;
    protected String recordID;

    protected static boolean isBelongList(String str, String[] validStrings){
        for (String temp : validStrings) {
            if (temp.equals(str)) {
                return (true);
            }
        }
        return (false);
    }

    public String getMemberID(){
        return this.recordID;
    }
}
