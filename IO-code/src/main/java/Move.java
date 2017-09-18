import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;


/**
 * Class needed in Artificial Intelligence to somehow tell Board, which move does he want to make
 */
class Move {
     @Getter @Setter private Pair<Integer, Integer> from;
     @Getter @Setter private Pair<Integer, Integer> to;
    
     /**
      * Constructor
      */
     public Move(){}
     /**
      * Constructor
      * @param from move from field
      * @param to move to field
      */
     public Move(Pair<Integer, Integer> from, Pair<Integer, Integer> to) {
          this.from = from;
          this.to = to;
     }
}
