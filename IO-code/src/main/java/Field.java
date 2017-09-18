import java.io.Serializable;

/**
 * Field class. Represents the field.
 */
public class Field implements java.io.Serializable{
    /**
     * Value of the field
     * 1-6 first player has a stone on this field
     * 11-16 second player has a stone on this field
     * 0 field is empty
     */
    private int value;

    /**
     * Field constructor
     */
    public Field() {
        this.value = 0;
    }

    /**
     * Field constructor
     *
     * @param value Field value
     */
    public Field(int value) throws InvalidValueException {
        this.setValue(value);
    }

    /**
     * Sets field value
     *
     * @param value Field value
     */
    public void setValue(int value) throws InvalidValueException {
        if (value < 0 || (value > 6 && value < 11) || value > 16)
            throw new InvalidValueException(value + " is not in [0, 6] sum [11, 16]");
        this.value = value;
    }

    /**
     * Returns the current value of the field
     *
     * @return current value of the field
     */
    public int getValue() {
        return this.value;
    }
}
