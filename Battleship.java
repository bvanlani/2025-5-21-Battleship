import java.util.*;

/**
 * Represents a Battleship in a game, managing its properties and status.
 */
public class Battleship {
    /** The name of the battleship. */
    private final String name;

    /** The length of the battleship, indicating the number of squares it occupies. */
    private final int length;

    /** The list of squares occupied by the battleship. */
    private final ArrayList<Square> shipParts;

    /** The direction in which the battleship is oriented. */
    private Direction dir;

    /** The starting X (column) coordinate of the battleship. */
    private int startX;

    /** The starting Y (row) coordinate of the battleship. */
    private int startY;

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
     * Checks if the battleship is sunk.
     * A ship is considered sunk if all of its parts have been hit.
     *
     * @return true if the battleship is sunk, false otherwise
     */
    public boolean checkIsSunk() {
        for (Square shipPart : shipParts) {
            if (!shipPart.getIsHit()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a ship part (Square) to the battleship.
     * Also sets the starting coordinates if this is the first part added.
     *
     * @param shipPart the square to add to the battleship's parts
     */
    public void addShipPart(Square shipPart) {
        if (shipParts.isEmpty()) {
            startX = shipPart.getPositionX();
            startY = shipPart.getPositionY();
        }
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

    /**
     * Returns the list of squares that make up the battleship.
     *
     * @return the list of ship parts
     */
    public ArrayList<Square> getShipParts() {
        return shipParts;
    }

    /**
     * Returns the direction in which the battleship is oriented.
     *
     * @return the direction of the battleship
     */
    public Direction getDirection() {
        return dir;
    }

    /**
     * Returns the starting X (column) coordinate of the battleship.
     *
     * @return the starting X coordinate
     */
    public int getStartX() {
        return startX;
    }

    /**
     * Returns the starting Y (row) coordinate of the battleship.
     *
     * @return the starting Y coordinate
     */
    public int getStartY() {
        return startY;
    }

    /**
     * Sets the direction of the battleship.
     *
     * @param dir the direction to set
     */
    public void setDirection(Direction dir) {
        this.dir = dir;
    }
}
