import static org.junit.Assert.*;
import org.junit.*;
import javax.swing.*;
import java.awt.*;


public class FieldTest {
    private Field field;

    @Before
    public void before() {
        field = new Field();
    }
    @Test
    public void setValue() throws Exception {
        field.setValue(5);
        assertEquals(field.getValue(), 5);
    }

    @Test
    public void getValue() throws Exception {
        field.setValue(11);
        assertEquals(field.getValue(), 11);
    }

    @Test(expected=InvalidValueException.class)
    public void setValue7() throws Exception {
        field.setValue(7);
    }

    @Test(expected=InvalidValueException.class)
    public void setValue10() throws Exception {
        field.setValue(10);
    }

    @Test(expected=InvalidValueException.class)
    public void setValueMinus() throws Exception {
        field.setValue(-1);
    }

    @Test(expected=InvalidValueException.class)
    public void setValue17() throws Exception {
        field.setValue(17);
    }

}
