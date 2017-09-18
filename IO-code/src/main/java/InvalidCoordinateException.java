/**
 * InvalidCoordinateException class. Thrown when coordinate is invalid (field is outside the board).
 */
public class InvalidCoordinateException extends Exception {
    /**
     * InvalidCoordinateException constructor
     *
     * @param s Exception message
     */
    public InvalidCoordinateException(String s) {
        super(s);
    }
};
