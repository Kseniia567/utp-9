import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

public class PESELTest {

    String correct = "02281911585"; //correct
    String incorrect = "12345678901"; //incorrect

    PESEL correct_pesel = new PESEL(correct);
    static Method validate_test = null;
    static Method extractBD_test = null;
    static Method extractS_test = null;


    static {

        try {
            validate_test = PESEL.class.getDeclaredMethod("validate", String.class, Boolean.TYPE);
            validate_test.setAccessible(true);

            extractBD_test = PESEL.class.getDeclaredMethod("extractDateFromPes", String.class, Boolean.TYPE);
            extractBD_test.setAccessible(true);

            extractS_test = PESEL.class.getDeclaredMethod("extractSexFromPes", String.class);
            extractS_test.setAccessible(true);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }





    //with strings
    @Test
    public void test_validate() throws InvocationTargetException, IllegalAccessException {
        Assert.assertTrue((boolean)validate_test.invoke(null, correct, false));
        Assert.assertFalse((boolean)validate_test.invoke(null, incorrect, false));
    }

    @Test
    public void test_extractBD() throws InvocationTargetException, IllegalAccessException {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(2002, Calendar.AUGUST, 19);
        Date d = calendar.getTime();
        Assert.assertEquals(d, extractBD_test.invoke(null, correct, false));
    }


    @Test
    public void test_extractS() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {

        Field field = PESEL.class.getDeclaredField("_sex");
        field.setAccessible(true);
        //just practice with reflection)
        Assert.assertEquals(field.get(correct_pesel), extractS_test.invoke(null, correct));
    }


}