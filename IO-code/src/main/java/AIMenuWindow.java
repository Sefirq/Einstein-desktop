import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;


class AIMenuWindow extends MenuWindow {

    private static final RoundRectangle2D hardMode = new RoundRectangle2D.Double();
    private static final RoundRectangle2D mediumMode = new RoundRectangle2D.Double();
    private static final RoundRectangle2D easyMode = new RoundRectangle2D.Double();
    private static final String easyModeString = "EASY";
    private static final String mediumModeString = "MEDIUM";
    private static final String hardModeString = "HARD";
    private boolean executeColorPicker;

    private static final String title = "Choose difficulty level";

    AIMenuWindow(boolean executeColorPicker) {
//        this.setVisible(true);
        this.executeColorPicker = executeColorPicker;
        initAll("Choose Difficulty Level");
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println(e.getX() + " " + e.getY());
                if (easyMode.contains(x, y)) {
                    setVisible(false);
                    JFrame frame = null;
                    try {
                        frame = new EinsteinWindow(executeColorPicker, false, "RANDOM");
                    } catch (InvalidCoordinateException | InvalidValueException e1) {
                        e1.printStackTrace();
                    }
                    initNewFrame(frame);
                } else if (mediumMode.contains(x, y)) {
                    setVisible(false);
                    JFrame frame = null;
                    try {
                        frame = new EinsteinWindow(executeColorPicker, false, "NAIVE");
                    } catch (InvalidCoordinateException | InvalidValueException e1) {
                        e1.printStackTrace();
                    }
                    initNewFrame(frame);
                } else if (hardMode.contains(x, y)) {
                    setVisible(false);
                    JFrame frame = null;
                    try {
                        frame = new EinsteinWindow(executeColorPicker, false, "EXPECTI");
                    } catch (InvalidCoordinateException | InvalidValueException e1) {
                        e1.printStackTrace();
                    }
                    initNewFrame(frame);
                }
            }
        });
    }

    /**
     * Paints the AI menu window. Not to be called directly
     *
     * @param g the Graphics context in which to paint
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(new Color(255,255,255,0));
        g2d.clearRect(0, 0, getWidth(), getHeight());
        drawCenteredString(g2d, title);
        g2d.setColor(Color.GRAY);
        drawCenteredMenuButton(g2d, easyModeString, easyMode, 0);
        drawCenteredMenuButton(g2d, mediumModeString, mediumMode, 100);
        drawCenteredMenuButton(g2d, hardModeString, hardMode, 200);
    }
}
