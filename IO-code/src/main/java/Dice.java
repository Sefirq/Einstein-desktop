import javax.swing.*;
import java.awt.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.*;
import java.io.IOException;
import java.util.Random;

/**
 * Dice class. Provides random value for game by rolling.
 */
public class Dice extends JPanel {
    /**
     * Dice size
     */
    private final Dimension DICE_SIZE;
    /**
     * Dice sides images
     */
    private Image[] sides = new Image[7];
    /**
     * Currently visible side number
     */
    private int visibleSide = 3;
    /**
     * Registered roll listeners
     */
    private HashSet<OnRollListener> onRollListeners;
    /**
     * If the dice is ready to be rolled again
     */
    private boolean ifReadyToBeRolled = true;
    /**
     * If the dice was rolled in this turn
     */
    private boolean ifRolled = false;


    /**
     * Dice constructor
     *
     * @param dice_size Dice size
     */
    public Dice(Dimension dice_size) {
        onRollListeners = new HashSet<>();
        DICE_SIZE = dice_size;
        for (int i = 1; i < 7; ++i) {
            try {
                sides[i] = ImageIO.read(getClass().getResource("/drawable/dice" + i + ".png"));
            } catch (IOException e) {
            }
        }
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Dice is clicked");
                if(ifReadyToBeRolled) {
                    roll();
                    setIfReadyToBeRolled(false);
                    setIfRolled(true);
                    System.out.println("Rolled " + visibleSide);
                    repaint();
                }
            }
        });
    }

    /**
     * Rolls the dice 5 times
     */
    private void rollManyTimes() throws InterruptedException {
        Random rand = new Random();
        for(int i = 0; i < 5; i++) {
            visibleSide = rand.nextInt(6) + 1;
            for (OnRollListener l : onRollListeners)
                l.onRoll(visibleSide);
            paintImmediately(0,0,1000,1000); 
            Thread.sleep(100);
        }
    }

    /**
     * Rolls the dice and calls the listeners
     */
    void roll() {
        try {
            rollManyTimes();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        Random rand = new Random();
        visibleSide = rand.nextInt(6) + 1;
        paintImmediately(0,0,1000,1000);
        for (OnRollListener l : onRollListeners)
            l.onRoll(visibleSide);
    }

    /**
     * Paints the dice. Not to be called directly
     *
     * @param g the Graphics context in which to paint
     */
    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        System.out.println("Repainting");
        g.drawImage(sides[visibleSide], 0, 0, DICE_SIZE.width, DICE_SIZE.height, null);
    }

    /**
     * Returns the current side number
     *
     * @return current side number
     */
    public int getVisibleSide() {
        return visibleSide;
    }

    /**
     * Registers new onRollListener
     *
     * @param listener listener to register
     */
    public void addOnRollListener(OnRollListener listener) {
        onRollListeners.add(listener);
    }

    /**
     * Checks if dice is ready to be rolled
     */
    public boolean isIfReadyToBeRolled() {
        return ifReadyToBeRolled;
    }

    /**
     * Sets dice ready to be rolled
     *
     * @param ifReadyToBeRolled represents logical value of dice to be set
     */
    public void setIfReadyToBeRolled(boolean ifReadyToBeRolled) {
        this.ifReadyToBeRolled = ifReadyToBeRolled;
    }

    /**
     * Checks if dice is rolled
     */
    public boolean isIfRolled() {
        return ifRolled;
    }

    /**
     * Sets dice rolled
     *
     * @param ifRolled variable indicating if dice state should be ifRolled
     */
    public void setIfRolled(boolean ifRolled) {
        this.ifRolled = ifRolled;
    }

    /**
     * Listener that listens for roll event
     */
    public interface OnRollListener {
        /**
         * Roll event callback
         *
         * @param value rolled value
         */
        void onRoll(int value);
    }
}
