import java.util.*;

/**
 * Represents a Battleship in a game, managing its properties and status.
 */
public class Battleship {
    /** The name of the battleship. */
    private final String name;

    /** The length of the battleship, indicating the number of squares it occupies. */
    private final int length;

    /** Indicates whether the battleship has been sunk. */
    private boolean isSunk;

    /** The list of squares occupied by the battleship. */
    private ArrayList<Square> shipParts;

    /** The direction in which the battleship is oriented. */
    private Direction dir;

    /**
     * Constructs a Battleship with the specified name and length.
     *
     * @param name   the name of the battleship
     * @param length the number of squares the battleship occupies
     */
    public Battleship(String name, int length) {
        this.name = name;
        this.length = length;
        this.shipParts = new ArrayList<>();
    }

    /**
     * Sets the direction of the battleship.
     *
     * @param dir the direction to set for the battleship
     */
    public void setDirection(Direction dir) {
        this.dir = dir;
    }

    /**
     * Retrieves the direction of the battleship.
     *
     * @return the direction of the battleship
     */
    public Direction getDirection() {
        return dir;
    }

    /**
     * Checks if the battleship is sunk. A ship is considered sunk if it has no parts or has been marked as sunk.
     *
     * @return true if the battleship is sunk, false otherwise
     */
    public boolean checkIsSunk() {
        if (isSunk) return isSunk;

        if (shipParts.size() == 0) {
            isSunk = true;
            return isSunk;
        }

        return false;
    }

    /**
     * Removes a specific ship part from the battleship.
     *
     * @param shipPart the square to remove from the battleship's parts
     */
    public void removeShipPart(Square shipPart) {
        shipParts.remove(shipParts.indexOf(shipPart));
    }

    /**
     * Adds a ship part to the battleship.
     *
     * @param shipPart the square to add to the battleship's parts
     */
    public void addShipPart(Square shipPart) {
        shipParts.add(shipPart);
    }

    /**
     * Retrieves the length of the battleship.
     *
     * @return the length of the battleship
     */
    public int getLength() {
        return length;
    }
}