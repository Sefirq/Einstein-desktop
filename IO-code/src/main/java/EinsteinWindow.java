import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * EinsteinWindow class. Main frame in the window
 */
public class EinsteinWindow extends JFrame {

    private static final String PLAYER_1 = "PLAYER_1";
    private static final String PLAYER_2 = "PLAYER_2";
    /**
     * Window size
     */
    private final Dimension WINDOW_SIZE = new Dimension(600, 600);
    /**
     * Board size
     */
    private final Dimension BOARD_SIZE = new Dimension(
            (int) (WINDOW_SIZE.width * 0.75), (int) (0.75 * WINDOW_SIZE.width));
    /**
     * Dice size
     */
    private final Dimension DICE_SIZE = new Dimension(
            (int) (WINDOW_SIZE.width * 0.25), (int) (0.25 * WINDOW_SIZE.width));
    private ArtificialIntelligence artificialIntelligence;
    /**
     * Container holding the layout
     */
    private Container pane;
    /**
     * Board object
     * */
    Board board;
    /**
     *  Dice object
     *  */
    private Dice dice;
    /**
     * Current number visible on dice
     */
    private int diceValue;
    /**
     * Currently selected stone row on the field
     */
    private int selectedStoneRow = -1;
    /**
     * Currently selected stone column on the field
     */
    private int selectedStoneColumn = -1;
    /**
     * Currently selected field row
     */
    private int selectedFieldRow;
    /**
     * Currently selected field column
     */
    private int selectedFieldColumn;
    /**
     * Current state of the move
     */
    private String stateOfGame = "FIRST_STONE";
    /**
     * Constant used when checking if we should choose second field to move a stone
     */
    private static final String CHOOSING_SECOND_FIELD_TO_MOVE = "CHOOSE";
    /**
     * Constant used when checking if we should choose second field to move a stone
     */
    private static final String CHOOSING_STONE_TO_MOVE = "FIRST_STONE";
    /**
     * Constant String indicating that we will play against a random AI
     */
    private static final String RANDOM_AI = "RANDOM";
    /**
     * Constant String indicating that we will play against a naive AI
     */
    private static final String NAIVE_AI = "NAIVE";
    /**
     * Constant String indicating that we will play against ExpectiMiniMax AI
     */
    private static final String EXPECTI = "EXPECTI";
    /**
     * Currently playing player (PLAYER_1 or PLAYER_2)
     */
    String whoseTurn = PLAYER_1;
    /**
     * If true then it's a two player game, else single player
     */
    private boolean twoPlayersGame;
    /**
     * This String will be equal to "PLAYER_1" or "PLAYER_2", randomly.
     */
    private String botSide;
    private String playerSide;

    /**
     * EinsteinWindow constructor
     */
    EinsteinWindow(boolean executeColorPicker, boolean twoPlayersGame, String artificialIntelligenceType) throws InvalidCoordinateException, InvalidValueException {
        this.twoPlayersGame = twoPlayersGame;
        this.initAll();
        this.initBoard(executeColorPicker);
        this.initDice();
        this.pack();
        switch (artificialIntelligenceType) {
            case RANDOM_AI:
                this.artificialIntelligence = new RandomArtificialIntelligence();
                break;
            case NAIVE_AI:
                this.artificialIntelligence = new NaiveArtificialIntelligence();
                break;
            case EXPECTI:
                this.artificialIntelligence = new ExpectiMiniMaxArtificialIntelligence(this.board);
                break;
        }
        dice.addOnRollListener((value) -> diceValue = value);
        if (this.twoPlayersGame) {
            board.addOnStoneSelectListener(this::playersMove);
        } else {
            this.botSide = this.initSides();
            this.playerSide = botSide.equals(PLAYER_1) ? PLAYER_2 : PLAYER_1;
            if (this.botSide.equals(whoseTurn)) {
                botsMove();
            }
            board.addOnStoneSelectListener((fieldX, fieldY) -> {
                if(this.playerSide.equals(whoseTurn)) {
                    playersMove(fieldX, fieldY);
                    System.out.println(playerSide + " " + whoseTurn);
                    if (this.botSide.equals(whoseTurn)) {
                        System.out.println("BOTS TURN SECOND OR MORE");
                        botsMove();
                    }
                }
            });
        }
    }

    private void botsMove() throws InvalidCoordinateException, InvalidValueException {
        dice.roll();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        artificialIntelligence.makeAMove(board, botSide, diceValue);
        if(!checkIfGameEnds().equals("")) {
            JOptionPane.showMessageDialog(null, checkIfGameEnds());
            setVisible(false);
            JFrame frame;
            frame = new MenuWindow();
            frame.repaint();
            initNewFrame(frame);
        }        changeTheTurn();
    }

    private void playersMove(int fieldX, int fieldY) {
        if (stateOfGame.equals(CHOOSING_STONE_TO_MOVE)) {
            firstPartOfMove(fieldX, fieldY);
        } else if (stateOfGame.equals(CHOOSING_SECOND_FIELD_TO_MOVE)) {
            secondPartOfMove(fieldX, fieldY);
        }
    }

    private void firstPartOfMove(int fieldX, int fieldY) {
        int selectedStone;
        selectedStoneRow = fieldY;
        selectedStoneColumn = fieldX;
        try {
            selectedStone = board.getFieldValue(selectedStoneRow, selectedStoneColumn);
        } catch (InvalidCoordinateException e) {
            System.out.print("This shouldn't happen");
            return;
        }
        System.out.println("Board clicked; dice: " + diceValue + " select: " + selectedStone % 10 + " " + stateOfGame);
        if (stateOfGame.equals(CHOOSING_STONE_TO_MOVE) && whoseTurn.equals(PLAYER_1) && selectedStoneMatchesWithDiceValue(selectedStone, diceValue, 0) && dice.isIfRolled()) {
            stateOfGame = CHOOSING_SECOND_FIELD_TO_MOVE;
            board.setSelectedColumn(selectedStoneColumn);
            board.setSelectedRow(selectedStoneRow);
            repaint();
            System.out.println(selectedStone + " is a valid select " + stateOfGame);
        } else if (stateOfGame.equals(CHOOSING_STONE_TO_MOVE) && whoseTurn.equals(PLAYER_2) && selectedStoneMatchesWithDiceValue(selectedStone, diceValue, 10) && dice.isIfRolled()) {
            stateOfGame = CHOOSING_SECOND_FIELD_TO_MOVE;
            board.setSelectedColumn(selectedStoneColumn);
            board.setSelectedRow(selectedStoneRow);
            repaint();
            // color chosen field
            System.out.println(selectedStone + " is a valid select " + stateOfGame);
        }
    }

    private void secondPartOfMove(int fieldX, int fieldY) {
        System.out.println(stateOfGame);
        selectedFieldColumn = fieldX;
        selectedFieldRow = fieldY;
        System.out.println("Board clicked; coordinates on the field: row - " + selectedFieldRow + " column - " + selectedFieldColumn);
        int status = board.tryToMakeAMove(selectedStoneRow, selectedStoneColumn, selectedFieldRow, selectedFieldColumn);
        if (status == 1) {
            System.out.println("Valid move");
            selectedStoneRow = -1;
            selectedStoneColumn = -1;
            repaint();
            if(!checkIfGameEnds().equals("")) {
                JOptionPane.showMessageDialog(null, checkIfGameEnds());
                setVisible(false);
                JFrame frame = null;
                frame = new MenuWindow();
                frame.repaint();
                initNewFrame(frame);
            }
            changeTheTurn();
            dice.setIfReadyToBeRolled(true);
            stateOfGame = CHOOSING_STONE_TO_MOVE;
            dice.setIfRolled(false);
        } else if (status == 2) {
            selectedStoneRow = -1;
            selectedStoneColumn = -1;
            repaint();
            stateOfGame = CHOOSING_STONE_TO_MOVE;
        } else {
            System.out.println("Not a valid move");
        }
    }

    private void changeTheTurn() {
        System.out.println("CHANGING THE TURN");
        if(whoseTurn.equals(PLAYER_1)) {
            whoseTurn = PLAYER_2;
        } else {
            whoseTurn = PLAYER_1;
        }
    }

    private String initSides() {
        Random rand = new Random();
        if(rand.nextInt(2) == 0) {
            return PLAYER_1;
        }
        return PLAYER_2;

    }

    /**
     * Method checks if the stone, which we selected as the one to be moved matches with value on the dice
     *
     * @param selectedStone value of selected stone
     * @param diceValue     value on the visible side of the dice
     * @param modifier      modifier 0 - player1, 10 - player 2
     * @return true when selected stone's value matches with the one on the dice (respectively for both players)
     */
    boolean selectedStoneMatchesWithDiceValue(int selectedStone, int diceValue, int modifier) {
        if(board.onlyOneStoneRemaining(modifier)) {
            return true;
        }
        if (board.stoneIsOnTheBoard(diceValue + modifier)) {
            if (selectedStone - modifier == diceValue) {
                return true;
            }
        } else {
            java.util.List<Integer> listOfPossibleStones = board.findNearestMatchingStone(diceValue + modifier);
            if (listOfPossibleStones.contains(selectedStone)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Method checking if it's the end of the game
     */
    String checkIfGameEnds() {
        if(whoseTurn.equals(PLAYER_1)) {
            if(board.checkIfCornerFieldConquered(true)){
                // end game screen? menu?
                return("Player1 wins by conquer");
            }
            if(board.checkIfAllEnemysStonesAreDestroyed(true)) {
                return("Player1 wins by destroying stones");
            }
        } else {
            if (board.checkIfCornerFieldConquered(false)) {
                // end game screen? menu?
                return("Player2 wins by conquer");            }
            if(board.checkIfAllEnemysStonesAreDestroyed(false)) {
                return("Player2 wins by destroying stones");
            }
        }
        return("");
    }

    /**
     * Initialises window and layout
     */
    private void initAll() {
        this.initWindow();
        this.initLayout();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Initializes new frame
     * @param frame to be initialized
     */
    private void initNewFrame(JFrame frame) {
        if (frame != null) {
            frame.setDefaultCloseOperation (WindowConstants.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        }
    }

    /**
     * Initialises window in wm
     */
    private void initWindow() {
        this.setTitle("Einstein");
        this.setSize(WINDOW_SIZE);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Initialises layout
     */
    private void initLayout() {
        pane = getContentPane();
        pane.setLayout(new GridBagLayout());
    }

    /**
     * Shows pop up that enables user to select color
     *
     * @param title title of pop up window
     * @return color selected by user
     */
    private Color showColorPickerThatReturnsColor(String title) {
        return JColorChooser.showDialog(this, title, Color.GRAY);
    }

    /**
     * Initialises colors
     *
     * @return Array of player colors
     */
    private ArrayList<Color> initColors() {
        Color firstPlayerColor;
        Color secondPlayerColor;
        do {
            firstPlayerColor = showColorPickerThatReturnsColor("Select first player colour");
            if (firstPlayerColor == null) {
                JOptionPane.showMessageDialog(null, "Please, choose a color.");
            }
        } while (firstPlayerColor == null);
        System.out.println(firstPlayerColor);
        do {
            secondPlayerColor = showColorPickerThatReturnsColor("Select second player colour");
            if (secondPlayerColor == null) {
                JOptionPane.showMessageDialog(null, "Please, choose a color.");
            } else if (secondPlayerColor.equals(firstPlayerColor)) {
                JOptionPane.showMessageDialog(null, "Please, choose different color than first player.");
            }
        } while (secondPlayerColor == null || firstPlayerColor.equals(secondPlayerColor));
        System.out.println(secondPlayerColor);
        ArrayList<Color> l = new ArrayList<>();
        l.add(firstPlayerColor);
        l.add(secondPlayerColor);
        return l;
    }

    /**
     * Initialises board
     */
    private void initBoard(boolean executeColorPicker) throws InvalidValueException {
        ArrayList<Color> colors;
        if(executeColorPicker){
            colors = initColors();
        } else {
            colors = new ArrayList<>();
            colors.add(Color.yellow);
            colors.add(Color.blue);
        }
        this.board = new Board(BOARD_SIZE, colors);
        this.board.setVisible(true);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.75;
        c.weighty = 0.75;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(board, c);
    }

    /**
     * Initialises dice
     */
    private void initDice() {
        this.dice = new Dice(DICE_SIZE);
        this.dice.setVisible(true);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.25;
        c.weighty = 0.25;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        pane.add(dice, c);
    }
}
