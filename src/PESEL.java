import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PESEL {

    private static final int[] utils = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};

    private static final String DIG = "[0-9]";
    private static final String TWO_DIG = DIG + "{2}";

    //private static final String pesel_pattern = "^[0-9]{11}$";
    private static final String pesel_pattern = "^" + "(" + TWO_DIG + ")" + "(" + TWO_DIG + ")"
            + "(" + TWO_DIG + ")" + DIG + "{3}" + "(" + DIG + ")" + "(" + DIG + ")$" ;

    private final String _val;
    private final Date _bd;
    private final Sex _sex;
    private static final char ZERO = '0';
    private static final Pattern pesel_regex = Pattern.compile(pesel_pattern);


    public PESEL(String value) {

            validate(value, true);
                _val = value;
                _bd = extractDateFromPes(value, true);
                _sex = extractSexFromPes(value);




    }



    private static boolean validate(String temp, boolean raiseException) {
        Matcher matcher = pesel_regex.matcher(temp);

        if(!matcher.matches()) {
            if (raiseException) {
                throw new UserException("Wrong input!");
            }
            else {
                return false;
            }
        }




        int lastD = temp.charAt(10) - ZERO; // char to int cast
        int checkSum = 0;

        for (int i = 0; i < utils.length; i++) {
            checkSum += utils[i] * (temp.charAt(i) - ZERO);
        }

        int module = checkSum % 10;
        return lastD == 10 - module;
    }



    private static Sex extractSexFromPes(String temp) {
        int i = temp.charAt(9) - ZERO;
        return i % 2 == 0? Sex.FEMALE:Sex.MALE;
    }


    private static Date extractDateFromPes(String temp, boolean raiseException) {
       
            Matcher m = pesel_regex.matcher(temp);

            if (m.matches()) {
                int rawYear = Integer.parseInt(m.group(1));
                int rawMonth = Integer.parseInt(m.group(2));
                int day = Integer.parseInt(m.group(3));

                int year;
                int month = rawMonth % 20;

                int century = rawMonth / 20;

                year = switch (century) {
                    case 0 -> 1900 + rawYear;
                    case 1 -> 2000 + rawYear;
                    case 3 -> 2200 + rawYear;
                    case 4 -> 1800 + rawYear;
                    default -> throw new IllegalStateException("Unexpected value: " + century);
                };

                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                return calendar.getTime();
            } else {
                if (raiseException) {
                    throw new UserException("Incorrect output!");
                } else {
                    return null;
                }
            }
        

    }




    enum Sex {
        MALE("MALE"),
        FEMALE("FEMALE");


        private final String _name;

        Sex(String name) {
            _name = name;
        }

        @Override
        public String toString() {
            return _name;
        }
    }





    @Override
    public String toString() {
        return "PESEL:  " + _val + ", date of birthday:  " + _bd + ", sex:  " + _sex;
    }
}
