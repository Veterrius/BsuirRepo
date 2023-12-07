package by.dlstudio.hospital.domain.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactInfoVerifier {

    public static boolean verifyAddress(String address) {
        return address.matches("^(\\d+) [a-zA-Z0-9\\s]+(,)? [a-zA-Z]+(,)? [a-zA-Z]+$");
    }

    public static boolean verifyPhoneNumber(String phoneNumber) {
        String patterns
                = "^(\\+\\d{1,3}( )?)?((\\(\\d{2,3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{2,3} ?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{2,3} ?)(\\d{2} ?){2}\\d{2}$";
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
