import java.util.List;
import org.apache.commons.lang3.SerializationUtils;
class ExpectiMiniMaxArtificialIntelligence extends ArtificialIntelligence {

    ExpectiMiniMaxArtificialIntelligence(Board board) {
        this.board = SerializationUtils.clone(board);
        this.tempBoard = SerializationUtils.clone(this.board);
    }

    @Override
    void makeAMove(Board board, String player, int diceValue) throws InvalidCoordinateException, InvalidValueException {
        this.board.cloneFields(board);
        MoveWithValue moveWithValue = expectiMax(player.equals(PLAYER1) ? 1 : -1, player.equals(PLAYER1) ? 1 : -1, 0, diceValue);
        System.out.println("" + moveWithValue.move.getFrom().getKey() + moveWithValue.move.getFrom().getValue());
        moveStone(board, moveWithValue.move);
    }


    @Override
    Move getBestMove(List<Move> possibleMoves, int playerModifier) {
//        int whoseTurn = playerModifier; // we know who initiated getting move -> it's his turn
//        MoveWithValue move = expectiMiniMax(possibleMoves, playerModifier, whoseTurn, 0);
        return null;
    }

    private MoveWithValue expectiMax(int playerModifier, int whoseTurn, int depth, int diceValue) throws InvalidCoordinateException, InvalidValueException {
        System.out.println("ExpectiMax " + depth + " " + diceValue);
        final MoveWithValue bestMove = new MoveWithValue();
        MoveWithValue reply;
//        System.out.println("DICE VALUE: " + diceValue);
        bestMove.value = -1000001;
        if(this.board.checkIfGameEnds() || depth == maxDepth) {
//            System.out.println("Game ends or max depth: " + depth + " " + maxDepth);
            System.out.println("Value to be returned from Max: " + this.board.evaluate(whoseTurn == 1 ? PLAYER1 : PLAYER2) + " on lvl " + depth);
            return new MoveWithValue(new Move(), this.board.evaluate(whoseTurn == 1 ? PLAYER1 : PLAYER2));
        } else {
            List<Move> moveList = getPossibleMoves(this.board, playerModifier == 1 ? PLAYER1 : PLAYER2, diceValue);
            for (Move move: moveList) {
                tempBoard.cloneFields(this.board);
                moveStone(this.board, move);
                reply = expectiRandom(playerModifier, whoseTurn, depth);
                if (reply.value > bestMove.value) {
                    bestMove.value = reply.value;
                    bestMove.move = move;
                }
                this.board.cloneFields(tempBoard);
//            System.out.println(bestMove.value + " " + reply.value);
            }
        }
        System.out.println("Value to be returned from Max: " + bestMove.value + " on lvl " + depth);
        return bestMove;
    }

    private MoveWithValue expectiRandom(int playerModifier, int whoseTurn, int depth) throws InvalidCoordinateException, InvalidValueException {
        System.out.println("ExpectiRandom " + depth);
        int value = 0;
        for (int i = 1; i < 7; i++) {
            if(playerModifier == whoseTurn) {
                value += expectiMin(-playerModifier, whoseTurn, depth+1, i).value;
            } else {
                value += expectiMax(-playerModifier, whoseTurn, depth+1, i).value;
            }
        }
        System.out.println("Value to be returned from Random: " + value/6 + " on lvl " + depth);
        return new MoveWithValue(new Move(), (float) value / 6);
    }

    private MoveWithValue expectiMin(int playerModifier, int whoseTurn, int depth, int diceValue) throws InvalidCoordinateException, InvalidValueException {
        System.out.println("ExpectiMin " + depth + " " + diceValue);
        final MoveWithValue bestMove = new MoveWithValue();
        MoveWithValue reply;
        bestMove.value = 1000001;
        if(this.board.checkIfGameEnds() || depth == maxDepth) {
            System.out.println("Value to be returned from Min: " + this.board.evaluate(whoseTurn == 1 ? PLAYER1 : PLAYER2) + " on lvl " + depth);
            return new MoveWithValue(new Move(), this.board.evaluate(whoseTurn == 1 ? PLAYER1 : PLAYER2));
        } else {
            List<Move> moveList = getPossibleMoves(this.board, playerModifier == 1 ? PLAYER1 : PLAYER2, diceValue);
            for (Move move: moveList) {
                tempBoard.cloneFields(this.board);
                moveStone(this.board, move);
                reply = expectiRandom(playerModifier, whoseTurn, depth);
                if (reply.value < bestMove.value) {
                    bestMove.value = reply.value;
                    bestMove.move = move;
                }
                this.board.cloneFields(tempBoard);
            }
        }
        System.out.println("Value to be returned from Min: " + bestMove.value + " on lvl " + depth);
        return bestMove;
    }
}
