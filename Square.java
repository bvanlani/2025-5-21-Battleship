import javax.swing.JButton;
import java.awt.Color;

/**
 * Represents a square on a game board, which is a specialized JButton.
 * Each square has a position, a type (e.g., ship, water), and a hit status.
 */
public class Square extends JButton {

    /** The X position of the square on the board (column index). */
    private int positionX;

    /** The Y position of the square on the board (row index). */
    private int positionY;

    /** Boolean flag to determine if the square has been hit. */
    private boolean isHit;

    /** The type of square (e.g., "ship", "water", "miss", "sunk", "flag"). */
    private String squareType;

    /**
     * Constructs a Square object with the specified properties.
     *
     * @param positionX  The X position (column) of the square.
     * @param positionY  The Y position (row) of the square.
     * @param isHit      Whether the square has been hit.
     * @param squareType The type of square (e.g., "ship", "water").
     */
    public Square(int positionX, int positionY, boolean isHit, String squareType) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.isHit = isHit;
        this.squareType = squareType;
    }

    /**
     * Gets the X position (column index) of the square.
     *
     * @return The X position.
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Gets the Y position (row index) of the square.
     *
     * @return The Y position.
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Checks if the square has been hit.
     *
     * @return True if the square has been hit, otherwise false.
     */
    public boolean getIsHit() {
        return isHit;
    }

    /**
     * Gets the type of the square.
     *
     * @return The type of the square (e.g., "ship", "water").
     */
    public String getType() {
        return squareType;
    }

    /**
     * Sets the type of the square and updates its background color accordingly.
     * Also marks the square as hit and disables interaction if the type is "flag".
     *
     * @param type The new type of the square.
     */
    public void setType(String type) {
        this.squareType = type;
        switch (type) {
            case "ship":
                setBackground(ColorPalette.getShipColor());
                break;
            case "water":
            case "enemy_ship":
                setBackground(ColorPalette.getWaterColor());
                break;
            case "miss":
                setBackground(ColorPalette.getWaterHit());
                break;
            case "sunk":
                setBackground(ColorPalette.getSunkShip());
                break;
            case "flag":
                setBackground(ColorPalette.getFlagColor());
                isHit = true;
                removeActionListener(null); // Placeholder: should remove actual listeners
                break;
        }
    }

    /**
     * Marks the square as hit and updates its type and appearance accordingly.
     * If it's a ship or enemy ship, it's marked as "flag". If it's water, it's marked as "miss".
     */
    public void hitSquare() {
        switch (squareType) {
            case "ship":
            case "enemy_ship":
                setType("flag");
                break;
            case "water":
                setType("miss");
                break;
        }
    }
}
