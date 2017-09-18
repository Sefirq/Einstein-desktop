import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomArtificialIntelligence extends ArtificialIntelligence {


    @Override
    public void makeAMove(Board board, String player, int diceValue) throws InvalidCoordinateException, InvalidValueException {
        List<Move> possibleMoves = getPossibleMoves(board, player, diceValue);
        Move move = getBestMove(possibleMoves, player.equals(PLAYER1) ? 1 : -1);
        possibleMoves.clear();
        moveStone(board, move);
    }

    Move getBestMove(List<Move> possibleMoves, int PlayerModifier) {
        Random random = new Random();
        int moveIndex = random.nextInt(possibleMoves.size());
        return possibleMoves.get(moveIndex);
    }


}
