import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

abstract class ArtificialIntelligence {

    protected Board board;

    final int maxDepth = 5;

    private static final int LOWER_BOUNDARY_OF_BOARD = 0;

    private static final int UPPER_BOUNDARY_OF_BOARD = 4;

    private static final int PLAYER1_MODIFIER = 1;

    private static final int PLAYER2_MODIFIER = -1;

    static final String PLAYER1 = "PLAYER_1";

    static final String PLAYER2 = "PLAYER_2";

    Board tempBoard;

    abstract void makeAMove(Board board, String player, int diceValue) throws InvalidCoordinateException, InvalidValueException;

    /**
     * @param possibleMoves - every possible move with current diceValue
     * @param playerModifier - 1 for PLAYER_1, -1 otherwise
     * @return best move to be performed
     */
    abstract Move getBestMove(List<Move> possibleMoves, int playerModifier);


    void moveStone(Board board, Move move) throws InvalidCoordinateException, InvalidValueException {
        int value = board.getFieldValue(move.getFrom());
        board.setFieldValue(move.getFrom(), 0);
        board.setFieldValue(move.getTo(), value);
    }


    List<Move> getPossibleMoves(Board board, String player, int diceValue) {
        List<Move> possibleMoves = new ArrayList<>();
        diceValue = player.equals(PLAYER1) ? diceValue : diceValue + 10;
        possibleMoves = getMoves(board, player, diceValue, possibleMoves);
        return possibleMoves;
    }

    private List<Move> findAllPossibleMoves(String player, Pair<Integer, Integer> from) {
        List<Move> possibleMoves = new ArrayList<>();
        int row = from.getKey();
        int column = from.getValue();
        switch (player) {
            case PLAYER1:
                possibleMoves = createListOfMoves(row, column, from, PLAYER1_MODIFIER, UPPER_BOUNDARY_OF_BOARD);
                break;
            case PLAYER2:
                possibleMoves = createListOfMoves(row, column, from,  PLAYER2_MODIFIER, LOWER_BOUNDARY_OF_BOARD);
                break;
        }
        return possibleMoves;
    }

    private List<Move> createListOfMoves(int row, int column, Pair<Integer, Integer> from, int playerModifier, int boundaryOfBoard) {
        List<Move> possibleMoves = new ArrayList<>();
        if (playerModifier * column < boundaryOfBoard && playerModifier * row < boundaryOfBoard) {
            possibleMoves.add(new Move(from, new Pair<>(row+playerModifier, column+playerModifier)));
            possibleMoves.add(new Move(from, new Pair<>(row, column + playerModifier)));
            possibleMoves.add(new Move(from, new Pair<>(row+playerModifier, column)));
        } else if(playerModifier * column < boundaryOfBoard) {
            possibleMoves.add(new Move(from, new Pair<>(row, column + playerModifier)));
        } else if (playerModifier * row < boundaryOfBoard) {
            possibleMoves.add(new Move(from, new Pair<>(row+ playerModifier, column)));
        } else {
            System.out.println("Weird situation");
        }
        return possibleMoves;
    }

    List<Move> getMoves(Board board, String player, int diceValue, List<Move> possibleMoves) {
        if (board.stoneIsOnTheBoard(diceValue)) {
            possibleMoves = findAllPossibleMoves(player, board.stonesRowAndColumn(diceValue));
        } else {
            List<Integer> allPossibleStones = board.findNearestMatchingStone(diceValue);
            for (Integer stone : allPossibleStones) {
                possibleMoves.addAll(findAllPossibleMoves(player, board.stonesRowAndColumn(stone)));
            }
        }
        return possibleMoves;
    }
}
