import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.HashSet;

/**
 * MenuWindow class. Represents the main menu window.
 */
public class MenuWindow extends JFrame {

    private static final String NONE = "NONE";
    /**
     * Window size
     */
    private final Dimension WINDOW_SIZE = new Dimension(600, 600);
    /**
     * Registered roll listeners
     */
    private HashSet<OnClickListener> onClickListeners;
    private String title = "EinStein, wurfelt nicht";
    private RoundRectangle2D localGame2P = new RoundRectangle2D.Double();
    private String localGame2PString = "Local game 2P";
    private RoundRectangle2D localGame1P = new RoundRectangle2D.Double();
    private String localGame1PString = "Local game 1P";
    private RoundRectangle2D networkGame = new RoundRectangle2D.Double();
    private String networkGameString = "Network game";
    private RoundRectangle2D options = new RoundRectangle2D.Double();
    private String optionsString = "Options";
    private RoundRectangle2D exit = new RoundRectangle2D.Double();
    private String exitString = "Exit the game";

    /**
     * MenuWindow constructor
     */
    MenuWindow() {
        this.initAll("Menu");
        onClickListeners = new HashSet<>();
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println(e.getX() + " " + e.getY());
                if(localGame1P.contains(x, y)) {
                    System.out.println("One player");
                    setVisible(false);
                    JFrame frame = null;
                    frame = new AIMenuWindow(true);
                    frame.repaint();
                    initNewFrame(frame);
                } else if(localGame2P.contains(x, y)) {
                    System.out.println("Two players");
                    setVisible(false);
                    JFrame frame = null;
                    try {
                        frame = new EinsteinWindow(true, true, NONE);
                    } catch (InvalidCoordinateException | InvalidValueException e1) {
                        e1.printStackTrace();
                    }
                    initNewFrame(frame);
                } else if(exit.contains(x, y)) {
                    System.out.println("Exit");
                    System.exit(0);
                }
            }
        });
    }

    /** Initialises new frame
     * @param fram frame to initialise
     */
    void initNewFrame(JFrame frame) {
        if (frame != null) {
            frame.setDefaultCloseOperation (WindowConstants.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        }
    }

    /**
     * Initialises window and layout
     */
    void initAll(String windowTitle) {
        this.initWindow(windowTitle);
        this.initLayout();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Initialises window in wm
     */
    private void initWindow(String windowTitle) {
        this.setTitle(windowTitle);
        this.setSize(WINDOW_SIZE);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Initialises layout
     */
    private void initLayout() {
        /* Container holding the layout */
        Container pane = getContentPane();
        pane.setLayout(new GridBagLayout());
    }

    /**
     * Paints the menu window. Not to be called directly
     *
     * @param g the Graphics context in which to paint
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(new Color(255,255,255,0));
        g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
        drawCenteredString(g2d, title);
        g2d.setColor(Color.GRAY);
        drawCenteredMenuButton(g2d, localGame1PString, localGame1P, 0);
        drawCenteredMenuButton(g2d, localGame2PString, localGame2P, 100);
        drawCenteredMenuButton(g2d, networkGameString, networkGame, 200);
        drawCenteredMenuButton(g2d, optionsString, options, 300);
        drawCenteredMenuButton(g2d, exitString, exit, 400);
    }

    /**
     * Draws menu button
     * @param g2d Graphics2D instance
     * @param text text on button
     * @param roundRect rectangle in which to draw
     * @param height_offset height offset
     */
    void drawCenteredMenuButton(Graphics2D g2d, String text, RoundRectangle2D roundRect, int height_offset) {
        FontRenderContext context = g2d.getFontRenderContext();
        Font font = new Font("Courier New", Font.BOLD, 15);
        g2d.setFont(font);
        TextLayout txt = new TextLayout(text, font, context);
        Rectangle2D bounds = roundRect.getBounds();
        int x = (getWidth() - (int) bounds.getWidth()) / 2;
        int y = 200 + height_offset;
        roundRect.setRoundRect(x, y, 150, 40, 20, 20);
        g2d.draw(roundRect);
        int textX = x + (int)roundRect.getBounds().getWidth()/2 - (int)txt.getBounds().getWidth() / 2;
        int textY = y + (int) roundRect.getBounds().getHeight() / 2 - (int) - txt.getBounds().getHeight() / 2 ;
        g2d.drawString(text, textX, textY);
    }
    
    /**
     * Draws text
     * @param g2d Graphics2D instance
     * @param text text to draw
     */
    void drawCenteredString(Graphics2D g2d, String text) {
        FontRenderContext context = g2d.getFontRenderContext();
        Font font = new Font("Courier New", Font.BOLD, 35);
        TextLayout txt = new TextLayout(text, font, context);
        Rectangle2D bounds = txt.getBounds();
        int x = ((getWidth() - (int) bounds.getWidth()) / 2);
        int y = 100;
        g2d.setFont(font);
        g2d.drawString(text, x, y);
    }

    /**
     * Registers new onRollListener
     *
     * @param listener listener to register
     */
    public void addOnClickListener(OnClickListener listener) {
        onClickListeners.add(listener);
    }

    /**
     * Listener that listens for roll event
     */
    public interface OnClickListener {
        /**
         * Click event callback
         */
        void onClick();
    }

}
