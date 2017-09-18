import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Board class. Represents the main player board.
 */
public class Board extends JPanel implements java.io.Serializable {
    /** Player 1 id */
    private static final String PLAYER_1 = "PLAYER_1";
    /** Larege number */
    private static final int INFINITY = 50;
    /**
     * Board size
     */
    private final Dimension BOARD_SIZE;
    /**
     * Number of fields in each row == number of rows in the board
     */
    final int FIELDS_COUNT = 5;
    /**
     * Field size
     */
    private final Dimension FIELD_SIZE;
    /**
     * First color of fields
     */
    private Color firstBoardColor;
    /**
     * Second color of fields
     */
    private Color secondBoardColor;
    /**
     * First player color
     */
    private Color firstPlayerColor;
    /**
     * Second player color
     */
    private Color secondPlayerColor;
    /**
     * Array representing fields on the board
     */
    Field[][] fields;
    /**
     * Currently selected stone number
     */
    private int currentStone;
    /**
     * Currently selected row of a stone
     */
    private int selectedRow = -1;
    /**
     * Currently selected column of a stone
     */
    private int selectedColumn = -1;
    /**
     * Registered stone select listeners
     */
    private HashSet<OnStoneSelectListener> onStoneSelectListeners;

    /**
     * Returns current first board color
     *
     * @return current first board color
     */
    public Color getFirstBoardColor() {
        return firstBoardColor;
    }

    /**
     * Sets current first board color
     *
     * @param firstBoardColor First board color
     */
    public void setFirstBoardColor(Color firstBoardColor) {
        this.firstBoardColor = firstBoardColor;
    }

    /**
     * Returns current second board color
     *
     * @return current second board color
     */
    public Color getSecondBoardColor() {
        return secondBoardColor;
    }

    /**
     * Sets current second board color
     *
     * @param secondBoardColor Second board color
     */
    public void setSecondBoardColor(Color secondBoardColor) {
        this.secondBoardColor = secondBoardColor;
    }

    /**
     * Returns current first player color
     *
     * @return current first player color
     */
    public Color getFirstPlayerColor() {
        return firstPlayerColor;
    }

    /**
     * Sets current first player color
     *
     * @param firstPlayerColor First player color
     */
    public void setFirstPlayerColor(Color firstPlayerColor) {
        this.firstPlayerColor = firstPlayerColor;
    }

    /**
     * Returns current second player color
     *
     * @return current second player color
     */
    public Color getSecondPlayerColor() {
        return secondPlayerColor;
    }

    /**
     * Sets the current second player color
     *
     * @param secondPlayerColor Second player color
     */
    public void setSecondPlayerColor(Color secondPlayerColor) {
        this.secondPlayerColor = secondPlayerColor;
    }

    /**
     * Returns selected row
     *
     * @return current selected row
     */
    public int getSelectedRow() {
        return selectedRow;
    }

    /**
     * Sets current selected row
     *
     * @param selectedRow Selected row
     */
    void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    /**
     * Returns selected column
     *
     * @return current selected column
     */
    public int getSelectedColumn() {
        return selectedColumn;
    }

    /**
     * Sets current selected column
     *
     * @param selectedColumn Selected column
     */
    void setSelectedColumn(int selectedColumn) {
        this.selectedColumn = selectedColumn;
    }

    /**
     * Board constructor
     *
     * @param board_size Board size
     */
    Board(Dimension board_size, ArrayList<Color> colors) throws InvalidValueException {
        firstBoardColor = Color.GRAY;
        secondBoardColor = Color.LIGHT_GRAY;
        firstPlayerColor = colors.get(0);
        secondPlayerColor = colors.get(1);
        onStoneSelectListeners = new HashSet<>();
        initFields();
        BOARD_SIZE = board_size;
        FIELD_SIZE = new Dimension(BOARD_SIZE.width / FIELDS_COUNT, BOARD_SIZE.height / FIELDS_COUNT);
//        fields[1][1].setValue(0);
//        fields[2][2].setValue(5);
        evaluate("PLAYER_1");
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int fieldX = (int) (x / FIELD_SIZE.getWidth());
                int fieldY = (int) (y / FIELD_SIZE.getHeight());
                try {
                    currentStone = fields[fieldY][fieldX].getValue();
                    System.out.println("click @ " + x + "x" + y + "; field: " + fieldX + "x" + fieldY + "; stone: " + currentStone);
                    for (OnStoneSelectListener listener : onStoneSelectListeners)
                        listener.onStoneSelect(fieldX, fieldY);
                } catch (ArrayIndexOutOfBoundsException exception) {
                    System.out.println("clicked outside the board");
                } catch (InvalidValueException | InvalidCoordinateException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }


    /**
     * Initialises fields
     */
    void initFields() {
        try {
            fields = new Field[FIELDS_COUNT][FIELDS_COUNT];
            for (int i = 0; i < FIELDS_COUNT; ++i)
                for (int j = 0; j < FIELDS_COUNT; ++j) {
                    fields[i][j] = new Field(0);
                }
            this.fields[2][0].setValue(1);
            this.fields[1][1].setValue(2);
            this.fields[0][2].setValue(3);
            this.fields[0][0].setValue(4);
            this.fields[0][1].setValue(5);
            this.fields[1][0].setValue(6);

            this.fields[2][4].setValue(11);
            this.fields[3][3].setValue(12);
            this.fields[4][2].setValue(13);
            this.fields[4][4].setValue(14);
            this.fields[4][3].setValue(15);
            this.fields[3][4].setValue(16);
        } catch (InvalidValueException e) {
            System.out.print("This should not happen");
        }
    }

    /**
     * Paints the board. Not to be called directly
     *
     * @param g the Graphics context in which to paint
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int counter = 0;
        for (int i = 0; i < FIELDS_COUNT; ++i)
            for (int j = 0; j < FIELDS_COUNT; ++j) {
                if (counter % 2 == 0) {
                    g2d.setColor(firstBoardColor);
                } else {
                    g2d.setColor(secondBoardColor);
                }
                counter++;
                g2d.fillRect(i * FIELD_SIZE.width, j * FIELD_SIZE.height,
                        FIELD_SIZE.width, FIELD_SIZE.height);
                int value = fields[j][i].getValue();
                if (value > 0) {
                    if (value > 10) {
                        g2d.setColor(secondPlayerColor);
                    } else {
                        g2d.setColor(firstPlayerColor);
                    }
                    Ellipse2D.Double circle = new Ellipse2D.Double(i * FIELD_SIZE.width, j * FIELD_SIZE.height, 90, 90);
                    g2d.fill(circle);
                    g2d.setColor(Color.black);
                    Font font = new Font("Serif", Font.PLAIN, 25);
                    g2d.setFont(font);
                    g2d.drawString(Integer.toString(value % 10), (int) ((i + 0.45) * FIELD_SIZE.width), (int) ((j + 0.55) * FIELD_SIZE.height));
                }
            }
        if (selectedColumn != -1 && selectedRow != -1) {
            g2d.setColor(Color.red);
            g2d.drawRect(selectedColumn * FIELD_SIZE.width, selectedRow * FIELD_SIZE.height,
                    FIELD_SIZE.width, FIELD_SIZE.height);
            selectedRow = -1;
            selectedColumn = -1;
        }
    }

    /**
     * Field value getter
     *
     * @param row    row in fields array
     * @param column column in fields array
     */
    int getFieldValue(int row, int column) throws InvalidCoordinateException {
        if (row < 0 || row > 5 || column < 0 || column > 5)
            throw new InvalidCoordinateException(row + " x " + column + " is not in [0,0]x[5,5]");
        return fields[row][column].getValue();
    }
    
    /**
     * Field value getter
     *
     * @param field field to get the value of
     */
    int getFieldValue(Pair<Integer, Integer> field) throws InvalidCoordinateException {
        return getFieldValue(field.getKey(), field.getValue());
    }

    /**
     * Field value setter
     *
     * @param field field to set
     * @param value value to set in field
     */
    void setFieldValue(Pair<Integer, Integer> field, int value) throws InvalidValueException {
        fields[field.getKey()][field.getValue()].setValue(value);
    }

    /**
     * Method that checks if move is valid and can be performed
     *
     * @param valueOfStone        value of the stone to be moved
     * @param rowOfChosenStone    row of this stone's field on the board
     * @param columnOfChosenStone column of this stone's field on the board
     * @param rowOfField          row of the field where stone should be moved
     * @param columnOfField       column of the field where stone should be moved
     * @return true when the move is valid, false when the move is invalid
     */
    private boolean checkIfValidMove(int valueOfStone, int rowOfChosenStone, int columnOfChosenStone, int rowOfField, int columnOfField) {
        if (valueOfStone > 10) {
            if ((rowOfField == rowOfChosenStone - 1 && columnOfField == columnOfChosenStone)
                    || (columnOfField == columnOfChosenStone - 1 && rowOfField == rowOfChosenStone)
                    || (rowOfField == rowOfChosenStone - 1 && columnOfField == columnOfChosenStone - 1)) {
                return true;
            }
        } else if (valueOfStone > 0) {
            if ((rowOfField == rowOfChosenStone + 1 && columnOfField == columnOfChosenStone)
                    || (columnOfField == columnOfChosenStone + 1 && rowOfField == rowOfChosenStone)
                    || (rowOfField == rowOfChosenStone + 1 && columnOfField == columnOfChosenStone + 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param playerModifier value that indicates if it's first player's turn (true) or second player's turn (false)
     * @return true if the game should be ended due to conquer of corner field, false otherwise
     */
    boolean checkIfCornerFieldConquered(boolean playerModifier) {
        try {
            int valueOfEndingField;
            if (playerModifier)
                valueOfEndingField = getFieldValue(FIELDS_COUNT - 1, FIELDS_COUNT - 1);
            else
                valueOfEndingField = getFieldValue(0, 0);
            if (playerModifier && valueOfEndingField > 0 && valueOfEndingField < 10) {
//                JOptionPane.showMessageDialog(null, "Player 1 won!");
                System.out.println("Player1");
                return true;
            } else if (!playerModifier && valueOfEndingField > 10) {
//                JOptionPane.showMessageDialog(null, "Player 2 won!");
                System.out.println("Player2");
                return true;
            }
            return false;
        } catch (InvalidCoordinateException e) {
            System.out.print("This should't happen");
            return false;
        }
    }


    /**
     * @param playerModifier value that indicates if it's first player's turn (true) or second player's turn (false)
     * @return true if the game should be ended due to destroying all enemy's stones, false otherwise
     */
    boolean checkIfAllEnemysStonesAreDestroyed(boolean playerModifier) {
        try {
            if (playerModifier) {
                for (int i = 0; i < FIELDS_COUNT; ++i)
                    for (int j = 0; j < FIELDS_COUNT; ++j) {
                        if (getFieldValue(i, j) > 10) {
                            return false;
                        }
                    }
//                JOptionPane.showMessageDialog(null, "Player 1 won! All stones killed!");
                return true;
            } else {
                for (int i = 0; i < FIELDS_COUNT; ++i)
                    for (int j = 0; j < FIELDS_COUNT; ++j) {
                        if (getFieldValue(i, j) > 0 && getFieldValue(i, j) < 10) {
                            return false;
                        }
                    }
//                JOptionPane.showMessageDialog(null, "Player 2 won! All stones killed!");
                return true;
            }
        } catch (InvalidCoordinateException e) {
            System.out.print("Something wrong");
            return false;
        }
    }

    /**
     * Method which tries to make move, as given in parameters
     *
     * @param rowOfChosenStone    row in fields array of chosen stone
     * @param columnOfChosenStone column in fields array of chosen stone
     * @param rowOfField          row in fields array of a field to move stone to
     * @param columnOfField       column in fields array of a field to move stone to
     */
    int tryToMakeAMove(int rowOfChosenStone, int columnOfChosenStone, int rowOfField, int columnOfField) {
        int valueOfStone = fields[rowOfChosenStone][columnOfChosenStone].getValue();
        int valueOfField = fields[rowOfField][columnOfField].getValue();
        if (valueOfField == valueOfStone) {
            return 2; // it means that we want to change chosen stone
        }
        if (checkIfValidMove(valueOfStone, rowOfChosenStone, columnOfChosenStone, rowOfField, columnOfField)) {
            try {
                fields[rowOfField][columnOfField].setValue(valueOfStone);
                fields[rowOfChosenStone][columnOfChosenStone].setValue(0);
            } catch (InvalidValueException e) {
                return 0;
            }
            paintImmediately(0,0,1000,1000);
            return 1; // it means that the move was successful
        } else {
            return 0; // it means that there was an error while trying to move the stone
        }
    }

    /**
     * Registers new stone select listeners
     *
     * @param listener listener to register
     */
    void addOnStoneSelectListener(OnStoneSelectListener listener) {
        onStoneSelectListeners.add(listener);
    }

    /**
     * @param selectedStone value of the selected stone
     * @return true when the stone with provided value is present on the table, false otherwise
     */
    boolean stoneIsOnTheBoard(int selectedStone) {
        return stonesRowAndColumn(selectedStone).getKey() != -1;
    }

    /**
     * Returns stone row and column
     *
     * @param selectedStone stone number
     * @return Pair containing row and column
     */
    Pair<Integer, Integer> stonesRowAndColumn(int selectedStone) {
        for (int i = 0; i < FIELDS_COUNT; i++)
            for (int j = 0; j < FIELDS_COUNT; j++)
                if (fields[i][j].getValue() == selectedStone) return new Pair<>(i, j);
        return new Pair<>(-1, -1);
    }

    /**
     * This method tells us which stones can player choose, when the proper stone is not present on the board.
     *
     * @param value value of the selected stone
     * @return list of 1 or 2 integers
     */
    List<Integer> findNearestMatchingStone(int value) {
        List<Integer> listOfPossibleStones = new ArrayList<>();
        int smallerStone = value - 1;
        int biggerStone = value + 1;
        int modifier = value > 10 ? 10 : 0;
        while (smallerStone > modifier) {
            if (stoneIsOnTheBoard(smallerStone)) {
                listOfPossibleStones.add(smallerStone);
                System.out.println("??" + smallerStone);
                break;
            }
            smallerStone--;
        }
        while (biggerStone < modifier + 7) {
            if (stoneIsOnTheBoard(biggerStone)) {
                listOfPossibleStones.add(biggerStone);
                System.out.println("!!" + biggerStone);
                break;
            }
            biggerStone++;
        }
        return listOfPossibleStones;
    }

    /**
     * @param modifier 0 when player1's move, 10 when player2's move
     * @return true when there are
     */
    boolean onlyOneStoneRemaining(int modifier) {
        int howManyStones = 0;
        for (int i = 0; i < FIELDS_COUNT; ++i)
            for (int j = 0; j < FIELDS_COUNT; ++j) {
                if (fields[i][j].getValue() != 0 && fields[i][j].getValue() < 7 + modifier && fields[i][j].getValue() > modifier) {
                    howManyStones++;
                }
            }
        return howManyStones <= 1;
    }

    int evaluate(String player) {
        int score = 0;
        boolean player_modifier = player.equals(PLAYER_1);
        if(checkIfCornerFieldConquered(player_modifier)) {
            score += INFINITY;
            System.out.println("CONQUER: " + score);
            return score;
        } else if (checkIfCornerFieldConquered(!player_modifier)) {
            score -= INFINITY;
            System.out.println("ENEMY CONQUER: " + score);
            return score;
        }
        if(checkIfAllEnemysStonesAreDestroyed(player_modifier)) {
            score += INFINITY;
            System.out.println("Enemy destroyed: " + score);
            return score;
        } else if(checkIfAllEnemysStonesAreDestroyed(!player_modifier)) {
            score -= INFINITY;
            System.out.println("I was destroyed: " + score);
            return score;
        }
        score += checkIfAnyStoneIsBehindEnemyStones(player_modifier);
//        System.out.println("Score: " + score);
        score -= checkIfAnyStoneIsBehindEnemyStones(!player_modifier);
//        System.out.println("Score: " + score);
        score += 3*(FIELDS_COUNT - minimalDistanceFromCorner(player_modifier));
//        System.out.println("Score: " + score);
        score -= 3*(FIELDS_COUNT - minimalDistanceFromCorner(!player_modifier));
//        System.out.println("Score: " + score);
        // we like to lose middle stones
        score += numberOfLostMiddleStones(player_modifier);
//        System.out.println("Score: " + score);
        // it's not as harmful if enemy lost middle stones
        score -= numberOfLostMiddleStones(!player_modifier);
//        System.out.println("Score: " + score);
        score -= numberOfLostMinMaxStones(player_modifier);
//        System.out.println("Score: " + score);
        score += numberOfLostMinMaxStones(!player_modifier);
//        System.out.println("Score: " + score);
        return score;
    }

    private int numberOfLostMinMaxStones(boolean player_modifier) {
        int lowestStoneValue = findLowestStoneValue(player_modifier);
        int highestStoneValue = findHighestStoneValue(player_modifier);
        return lowestStoneValue + FIELDS_COUNT-highestStoneValue;
    }

    private int numberOfLostMiddleStones(boolean player_modifier) {
        int number = 0;
        int lowestStoneValue = findLowestStoneValue(player_modifier);
        int highestStoneValue = findHighestStoneValue(player_modifier);
        for (int i = 0; i < FIELDS_COUNT; ++i)
            for (int j = 0; j < FIELDS_COUNT; ++j) {
                if(fields[i][j].getValue() > lowestStoneValue && fields [i][j].getValue() < highestStoneValue) {
                    number++;
                }
            }
        if(highestStoneValue > 10)
            highestStoneValue -= 10;
        return highestStoneValue-2-number;
    }

    private int findHighestStoneValue(boolean player_modifier) {
        for(int i = 6 + (player_modifier ? 0 : 10); i > (player_modifier ? 0 : 10); i--) {
            if(stoneIsOnTheBoard(i)) {
                return i;
            }
        }
        return -1;
    }

    private int findLowestStoneValue(boolean player_modifier) {
        for(int i = (player_modifier ? 1 : 11); i < 7 + (player_modifier ? 0 : 10); i++) {
            if(stoneIsOnTheBoard(i)) {
                return i;
            }
        }
        return 0;
    }

    private int minimalDistanceFromCorner(boolean player_modifier) {
        int minDistance = 10;
        int currentDistance;
        if(player_modifier) {
            for (int i = 0; i < FIELDS_COUNT; ++i)
                for (int j = 0; j < FIELDS_COUNT; ++j) {
                    if(fields[i][j].getValue() > 0 && fields[i][j].getValue() < 10) {
                        currentDistance = distanceBetweenFieldAndEnd(i, j, 4);
                        minDistance = currentDistance < minDistance ? currentDistance : minDistance;
                    }
                }
        } else {
            for (int i = 0; i < FIELDS_COUNT; ++i)
                for (int j = 0; j < FIELDS_COUNT; ++j) {
                    if(fields[i][j].getValue() > 10) {
                        currentDistance = distanceBetweenFieldAndEnd(i, j, 0);
                        minDistance = currentDistance < minDistance ? currentDistance : minDistance;
                    }
                }
        }
        return minDistance;
    }

    private int distanceBetweenFieldAndEnd(int row, int column, int boundary) {
        int distance = 0;
        if(boundary > 0) {
            while (row < boundary && column < boundary) {
                row++;
                column++;
                distance++;
            }
            distance += row < boundary ? boundary - row : boundary - column;
        } else {
            while (row > boundary && column > boundary) {
                row--;
                column--;
                distance++;
            }
            distance += row > boundary ? row : column;
        }
        return distance;
    }

    private int checkIfAnyStoneIsBehindEnemyStones(boolean playerModifier) {
        for (int i = 0; i < FIELDS_COUNT; ++i)
            for (int j = 0; j < FIELDS_COUNT; ++j) {
                if(playerModifier) {
                    if (isNotDominated(i, j, true) && fields[i][j].getValue() > 0 && fields[i][j].getValue() < 10) {
                        return 5;
                    }
                } else {
                    if(isNotDominated(i, j, false) && fields[i][j].getValue() > 0 && fields[i][j].getValue() > 10) {
                        return 5;
                    }
                }
            }
        return 0;
    }

    private boolean isNotDominated(int row, int column, boolean player_modifier) {
        if(player_modifier) {
            for (int i = row; i < FIELDS_COUNT; i++) {
                for (int j = column; j < FIELDS_COUNT; j++) {
                    if(fields[i][j].getValue() > 10) {
                        return false;
                    }
                }
            }
        }
        else {
            for (int i = row; i > 0; i--) {
                for (int j = column; j > 0; j--) {
                    if(fields[i][j].getValue() > 0 && fields[i][j].getValue() < 10) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    boolean checkIfGameEnds() {
        return checkIfCornerFieldConquered(true) || checkIfCornerFieldConquered(false) || checkIfAllEnemysStonesAreDestroyed(true) || checkIfAllEnemysStonesAreDestroyed(false);
    }

    void cloneFields(Board board) throws InvalidValueException {
        for (int i = 0; i < FIELDS_COUNT; i++) {
            for (int j = 0; j < FIELDS_COUNT; j++) {
                fields[i][j].setValue(board.fields[i][j].getValue());
            }
        }
    }


    /**
     * Listener that listens for stone select event
     */
    public interface OnStoneSelectListener {
        /**
         * Stone select event callback
         *
         * @param selectedX selected field's column
         * @param selectedY selected field's row
         */
        void onStoneSelect(int selectedX, int selectedY) throws InvalidCoordinateException, InvalidValueException;
    }
}
