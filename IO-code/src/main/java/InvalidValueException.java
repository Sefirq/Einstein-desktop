/**
 * InvalidValueException class. Thrown when trying to set invalid value of field.
 */
public class InvalidValueException extends Exception {
    /**
     * InvalidValueException constructor
     *
     * @param s Exception message
     */
    public InvalidValueException(String s) {
        super(s);
    }
};
