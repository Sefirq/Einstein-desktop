import java.util.ArrayList;
import java.util.List;

/**
 * This AI will go towards the opponent's corner
 */
public class NaiveArtificialIntelligence extends ArtificialIntelligence{

    /**
     * Makes a move
     * @param board board to play on
     * @param player player to play with
     * @param diceValue current value on dice
     */
    @Override
    public void makeAMove(Board board, String player, int diceValue) throws InvalidCoordinateException, InvalidValueException {
        List<Move> possibleMoves = getPossibleMoves(board, player, diceValue);
        Move move = getBestMove(possibleMoves, player.equals(PLAYER1) ? 1 : -1);
        moveStone(board, move);
    }

    /**
     * gets best move
     * @param possibleMoves possible moves
     * @param playerModifier player id
     */
    @Override
    Move getBestMove(List<Move> possibleMoves, int playerModifier) {
        if(possibleMoves.size() == 1){
            return possibleMoves.get(0);
        } else {
            for (Move move: possibleMoves) {
                if (move.getFrom().getKey() + playerModifier == move.getTo().getKey() && move.getFrom().getValue() + playerModifier == move.getTo().getValue()) {
                    return move;
                }
            }
        }
        return new Move();
    }
}
