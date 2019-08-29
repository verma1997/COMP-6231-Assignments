/**
 * Member Model
 */
public class Member {
    protected String recordID;
    protected String firstName;
    protected String lastName;

    public static boolean isBelongList(String str, String[] validStrings) {
        for (String tempString : validStrings) {
            if (tempString.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public String getMemberID(){
        return this.recordID;
    }
}
