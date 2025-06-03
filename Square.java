package dev.bvanlani.battleship;

import javax.swing.JButton;
import java.awt.Color;

/**
 * Represents a square on a game board, which is a specialized JButton.
 */
public class Square extends JButton {

    /** The X position of the square on the board. */
    private int positionX;
    
    /** The Y position of the square on the board. */
    private int positionY;
    
    /** Boolean flag to determine if the square has been hit. */
    private boolean isHit;
    
    /** The type of square (e.g., "ship" or "water"). */
    private String squareType;

    /**
     * Constructs a Square object with the specified properties.
     *
     * @param positionX  The X position of the square.
     * @param positionY  The Y position of the square.
     * @param isHit      Whether the square has been hit.
     * @param squareType The type of square.
     */
    public Square(int positionX, int positionY, boolean isHit, String squareType) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.isHit = isHit;
        this.squareType = squareType;
    }

    /**
     * Gets the X position of the square.
     *
     * @return The X position.
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Gets the Y position of the square.
     *
     * @return The Y position.
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Checks if the square has been hit.
     *
     * @return True if hit, otherwise false.
     */
    public boolean getIsHit() {
        return isHit;
    }

    /**
     * Gets the type of the square.
     *
     * @return The type of the square.
     */
    public String getType() {
        return squareType;
    }

    /**
     * Sets the type of the square and updates its color.
     *
     * @param type The new type of the square.
     */
    public void setType(String type) {
       this.squareType = type;
       switch(type){
           case "ship":
               setBackground(ColorPalette.getShipColor());
               break;
           case "water", "enemy_ship":
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
                removeActionListener(null);
                break;
       }
       //updateColor();
   }


    /**
     * Updates the square's background color based on its type.
     */
    public void hitSquare() {
        switch(squareType) {
            case "ship", "enemy_ship":
                setType("flag");
                break;
            case "water":
                setType("miss");
                break;
        }
    }
}