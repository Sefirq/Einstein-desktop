import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EinsteinWindowTest {
    private EinsteinWindow einsteinWindow;
    @Before
    public void before() throws InvalidCoordinateException, InvalidValueException {
        einsteinWindow = new EinsteinWindow(false, true, "RANDOM");
    }

    @Test
    public void testCheckIfGameEndsPlayer1Conquer() {
        Board mockBoard = mock(Board.class);
        einsteinWindow.board = mockBoard;
        when(mockBoard.checkIfCornerFieldConquered(true)).thenReturn(true);
        String result = einsteinWindow.checkIfGameEnds();
        verify(mockBoard, times(1)).checkIfCornerFieldConquered(true);
        assertEquals("Player1 wins by conquer", result);
    }

    @Test
    public void testCheckIfGameEndsPlayer1Destroy() {
        Board mockBoard = mock(Board.class);
        einsteinWindow.board = mockBoard;
        when(mockBoard.checkIfAllEnemysStonesAreDestroyed(true)).thenReturn(true);
        String result = einsteinWindow.checkIfGameEnds();
        verify(mockBoard, times(1)).checkIfCornerFieldConquered(true);
        assertEquals("Player1 wins by destroying stones", result);
    }

    @Test
    public void testCheckIfGameEndsPlayer2Destroy() {
        Board mockBoard = mock(Board.class);
        einsteinWindow.board = mockBoard;
        einsteinWindow.whoseTurn = "PLAYER_2";
        when(mockBoard.checkIfAllEnemysStonesAreDestroyed(false)).thenReturn(true);
        String result = einsteinWindow.checkIfGameEnds();
        verify(mockBoard, times(1)).checkIfAllEnemysStonesAreDestroyed(false);
        assertEquals("Player2 wins by destroying stones", result);
    }

    @Test
    public void testCheckIfGameEndsPlayer2Conquer() {
        Board mockBoard = mock(Board.class);
        einsteinWindow.board = mockBoard;
        einsteinWindow.whoseTurn = "PLAYER_2";
        when(mockBoard.checkIfCornerFieldConquered(false)).thenReturn(true);
        String result = einsteinWindow.checkIfGameEnds();
        verify(mockBoard, times(1)).checkIfCornerFieldConquered(false);
        assertEquals("Player2 wins by conquer", result);
    }

    @Test
    public void testCheckIfGameEndsNoEndPlayer1() {
        Board mockBoard = mock(Board.class);
        einsteinWindow.board = mockBoard;
        when(mockBoard.checkIfAllEnemysStonesAreDestroyed(true)).thenReturn(false);
        String result = einsteinWindow.checkIfGameEnds();
        verify(mockBoard, times(1)).checkIfAllEnemysStonesAreDestroyed(true);
        assertEquals("", result);
    }

    @Test
    public void testCheckIfGameEndsNoEndPlayer2() {
        Board mockBoard = mock(Board.class);
        einsteinWindow.board = mockBoard;
        einsteinWindow.whoseTurn = "PLAYER_2";
        when(mockBoard.checkIfAllEnemysStonesAreDestroyed(false)).thenReturn(false);
        String result = einsteinWindow.checkIfGameEnds();
        verify(mockBoard, times(1)).checkIfAllEnemysStonesAreDestroyed(false);
        assertEquals("", result);
    }

    @Test
    public void testSelectedStoneMatchesWithDiceValueOneStoneOnFieldPlayer1() {
        Board mockBoard = mock(Board.class);
        einsteinWindow.board = mockBoard;
        when(mockBoard.onlyOneStoneRemaining(0)).thenReturn(true);
        boolean result = einsteinWindow.selectedStoneMatchesWithDiceValue(2, 5, 0);
        verify(mockBoard, times(1)).onlyOneStoneRemaining(0);
        assertEquals(true, result);
    }

    @Test
    public void testSelectedStoneMatchesWithDiceValueStoneIsOnTheBoardPlayer1() {
        Board mockBoard = mock(Board.class);
        einsteinWindow.board = mockBoard;
        when(mockBoard.onlyOneStoneRemaining(0)).thenReturn(false);
        when(mockBoard.stoneIsOnTheBoard(2)).thenReturn(true);
        boolean result = einsteinWindow.selectedStoneMatchesWithDiceValue(2, 2, 0);
        InOrder inOrder = inOrder(mockBoard);
        inOrder.verify(mockBoard).onlyOneStoneRemaining(0);
        inOrder.verify(mockBoard).stoneIsOnTheBoard(2);
        assertEquals(true, result);
    }

    @Test
    public void testSelectedStoneMatchesWithDiceValueStoneIsNotOnTheBoardPlayer1() {
        Board mockBoard = mock(Board.class);
        einsteinWindow.board = mockBoard;
        when(mockBoard.onlyOneStoneRemaining(0)).thenReturn(false);
        when(mockBoard.stoneIsOnTheBoard(2)).thenReturn(false);
        ArrayList<Integer> list = new ArrayList<>();
        list.add(3);
        when(mockBoard.findNearestMatchingStone(2)).thenReturn(list);
        boolean result = einsteinWindow.selectedStoneMatchesWithDiceValue(3, 2, 0);
        InOrder inOrder = inOrder(mockBoard);
        inOrder.verify(mockBoard).onlyOneStoneRemaining(0);
        inOrder.verify(mockBoard).stoneIsOnTheBoard(2);
        inOrder.verify(mockBoard).findNearestMatchingStone(2);
        assertEquals(true, result);
    }
}