import static org.junit.Assert.*;

import org.junit.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static org.mockito.Mockito.*;

public class BoardTest {

    private Board board;

    private void clearBoard() throws InvalidValueException {
        for (int i = 0; i < board.FIELDS_COUNT; ++i) {
            for (int j = 0; j < board.FIELDS_COUNT; ++j) {
                board.fields[i][j].setValue(0);
            }
        }
    }

    @Before
    public void before() throws InvalidValueException {
        Dimension DICE_SIZE = new Dimension(1, 1);
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        board = new Board(DICE_SIZE, colors);
    }

    @Test(expected = InvalidCoordinateException.class)
    public void getFieldMinusPlus() throws Exception {
        board.getFieldValue(-1, 0);
    }

    @Test(expected = InvalidCoordinateException.class)
    public void getFieldPlusMinus() throws Exception {
        board.getFieldValue(0, -1);
    }

    @Test(expected = InvalidCoordinateException.class)
    public void getFieldOverOK() throws Exception {
        board.getFieldValue(6, 0);
    }

    @Test(expected = InvalidCoordinateException.class)
    public void getFieldOKOver() throws Exception {
        board.getFieldValue(0, 6);
    }

    @Test
    public void testGetFieldValueFirstPlayer() throws InvalidValueException, InvalidCoordinateException {
        Field mockObject = mock(Field.class);
        board.fields[0][0] = mockObject;
        when(mockObject.getValue()).thenReturn(1);
        int result = board.getFieldValue(0, 0);
        verify(mockObject, times(1)).getValue();
        assertEquals(1, result);
    }

    @Test
    public void testGetFieldValueSecondPlayer() throws InvalidValueException, InvalidCoordinateException {
        Field mockObject = mock(Field.class);
        board.fields[0][0] = mockObject;
        when(mockObject.getValue()).thenReturn(15);
        int result = board.getFieldValue(0, 0);
        verify(mockObject, times(1)).getValue();
        assertEquals(15, result);
    }

    @Test
    public void testGetFieldValueEmpty() throws InvalidValueException, InvalidCoordinateException {
        Field mockObject = mock(Field.class);
        board.fields[0][0] = mockObject;
        when(mockObject.getValue()).thenReturn(0);
        int result = board.getFieldValue(0, 0);
        verify(mockObject, times(1)).getValue();
        assertEquals(0, result);
    }

    @Test
    public void testStoneIsOnTheBoardPositiveFirstPlayer() throws InvalidValueException {
        clearBoard();
        board.fields[3][3].setValue(3);
        assertTrue(board.stoneIsOnTheBoard(3));
    }

    @Test
    public void testStoneIsOnTheBoardPositiveSecondPlayer() throws InvalidValueException {
        clearBoard();
        board.fields[1][4].setValue(16);
        assertTrue(board.stoneIsOnTheBoard(16));
    }

    @Test
    public void testStoneIsOnTheBoardNegativeFirstPlayer() throws InvalidValueException {
        clearBoard();
        board.fields[0][4].setValue(5);
        assertFalse(board.stoneIsOnTheBoard(3));
    }

    @Test
    public void testStoneIsOnTheBoardNegativeSecondPlayer() throws InvalidValueException {
        clearBoard();
        board.fields[2][0].setValue(11);
        assertFalse(board.stoneIsOnTheBoard(12));
    }

    @Test
    public void testFindNearestMatchingStoneExactStoneFirstPlayer() throws InvalidValueException {
        clearBoard();
        board.fields[1][0].setValue(3);
        List<Integer> listOfPossibleStones = new ArrayList<>();
        listOfPossibleStones = board.findNearestMatchingStone(3);
        assertEquals(listOfPossibleStones.size(), 0);
    }

    @Test
    public void testFindNearestMatchingStoneOnlyHigherStoneFirstPlayer() throws InvalidValueException {
        clearBoard();
        board.fields[1][0].setValue(3);
        board.fields[1][1].setValue(4);
        List<Integer> listOfPossibleStones = new ArrayList<>();
        listOfPossibleStones = board.findNearestMatchingStone(2);
        assertEquals(listOfPossibleStones.size(), 1);
        assertTrue(listOfPossibleStones.contains(3));
    }

    @Test
    public void testFindNearestMatchingStoneOnlyLowerStoneFirstPlayer() throws InvalidValueException {
        clearBoard();
        board.fields[1][0].setValue(3);
        board.fields[1][1].setValue(4);
        List<Integer> listOfPossibleStones = new ArrayList<>();
        listOfPossibleStones = board.findNearestMatchingStone(5);
        assertEquals(listOfPossibleStones.size(), 1);
        assertTrue(listOfPossibleStones.contains(4));
    }

    @Test
    public void testFindNearestMatchingStoneTwoStoneFirstPlayer() throws InvalidValueException {
        clearBoard();
        board.fields[1][0].setValue(1);
        board.fields[1][1].setValue(4);
        board.fields[1][4].setValue(5);
        List<Integer> listOfPossibleStones = new ArrayList<>();
        listOfPossibleStones = board.findNearestMatchingStone(3);
        assertEquals(listOfPossibleStones.size(), 2);
        assertTrue(listOfPossibleStones.contains(4));
        assertTrue(listOfPossibleStones.contains(1));
    }

    @Test
    public void testFindNearestMatchingStoneOpponentStoneFirstPlayer() throws InvalidValueException {
        clearBoard();
        board.fields[1][0].setValue(1);
        board.fields[1][1].setValue(14);
        board.fields[1][4].setValue(5);
        List<Integer> listOfPossibleStones = new ArrayList<>();
        listOfPossibleStones = board.findNearestMatchingStone(3);
        assertEquals(listOfPossibleStones.size(), 2);
        assertTrue(listOfPossibleStones.contains(5));
        assertTrue(listOfPossibleStones.contains(1));
    }

    @Test
    public void testFindNearestMatchingStoneExactStoneSecondPlayer() throws InvalidValueException {
        clearBoard();
        board.fields[1][0].setValue(14);
        List<Integer> listOfPossibleStones = new ArrayList<>();
        listOfPossibleStones = board.findNearestMatchingStone(14);
        assertEquals(listOfPossibleStones.size(), 0);
    }

    @Test
    public void testFindNearestMatchingStoneOnlyHigherStoneSecondPlayer() throws InvalidValueException {
        clearBoard();
        board.fields[1][0].setValue(13);
        board.fields[1][1].setValue(14);
        List<Integer> listOfPossibleStones = new ArrayList<>();
        listOfPossibleStones = board.findNearestMatchingStone(12);
        assertEquals(listOfPossibleStones.size(), 1);
        assertTrue(listOfPossibleStones.contains(13));
    }

    @Test
    public void testFindNearestMatchingStoneOnlyLowerStoneSecondPlayer() throws InvalidValueException {
        clearBoard();
        board.fields[1][0].setValue(13);
        board.fields[1][1].setValue(14);
        List<Integer> listOfPossibleStones = new ArrayList<>();
        listOfPossibleStones = board.findNearestMatchingStone(15);
        assertEquals(listOfPossibleStones.size(), 1);
        assertTrue(listOfPossibleStones.contains(14));
    }

    @Test
    public void testFindNearestMatchingStoneTwoStoneSecondPlayer() throws InvalidValueException {
        clearBoard();
        board.fields[1][0].setValue(11);
        board.fields[1][1].setValue(14);
        board.fields[1][4].setValue(15);
        List<Integer> listOfPossibleStones = new ArrayList<>();
        listOfPossibleStones = board.findNearestMatchingStone(13);
        assertEquals(listOfPossibleStones.size(), 2);
        assertTrue(listOfPossibleStones.contains(14));
        assertTrue(listOfPossibleStones.contains(11));
    }

    @Test
    public void testFindNearestMatchingStoneOpponentStoneSecondPlayer() throws InvalidValueException, InvalidValueException {
        clearBoard();
        board.fields[1][0].setValue(11);
        board.fields[1][1].setValue(4);
        board.fields[1][4].setValue(15);
        List<Integer> listOfPossibleStones = new ArrayList<>();
        listOfPossibleStones = board.findNearestMatchingStone(13);
        assertEquals(listOfPossibleStones.size(), 2);
        assertTrue(listOfPossibleStones.contains(15));
        assertTrue(listOfPossibleStones.contains(11));
    }

    @Test
    public void testEvaluate() throws InvalidValueException, InvalidValueException {
        clearBoard();
        board.initFields();
        board.fields[1][1].setValue(0);
        board.fields[2][2].setValue(5);
        System.out.println(board.evaluate("PLAYER_1"));
        //assertEquals(board.evaluate("PLAYER_1"), 0);
    }
}
