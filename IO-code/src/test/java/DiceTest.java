import static org.junit.Assert.*;
import org.junit.*;
import javax.swing.*;
import java.awt.*;


public class DiceTest {
    private Dice dice;

    @Before
    public void before() {
        Dimension DICE_SIZE = new Dimension(1, 1);
        dice = new Dice(DICE_SIZE);
    }
    
    @Test
    public void rollTest() {
        dice.roll();
        assertTrue(dice.getVisibleSide() > 0);
        assertTrue(dice.getVisibleSide() < 7);
    }
}
