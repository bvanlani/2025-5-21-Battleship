/**
 * GameManager.java
 *
 * Manages the game state for the Battleship game.
 *
 * Date: May 28, 2025
 * Author: Ethan Benzaquen and Ethan Drane
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * The GameManager class is responsible for managing players, game state,
 * event listeners, and the interactions between the game components.
 * It initializes the players, sets up the display boards, handles player
 * actions, and monitors the game progress (e.g., sending hits and checking for sunk ships).
 */
public class GameManager {

    /**
     * The current player whose turn is active.
     */
    private Player currentPlayer;

   
    /**
     * Display for player 1.
     */
    private Player p1;

    /**
     * Display for player 2.
     */
    private Player p2;

    /**
     * Constructs a new GameManager instance.
     * Initializes player objects and display boards for both players.
     */
    public GameManager() {
        currentPlayer = null;
        
        p1 = new Player("player1");
        p2 = new Player("player2");
    }
    
    /**
     * Processes a hit on the specified square.
     * If the square has not been hit before, this method updates the square's appearance
     * based on its type (e.g., ship or water).
     *
     * @param square The square targeted for the hit.
     */
    public void sendHit(Square square) {
        if (!square.getIsHit()) {
            switch (square.getType()) {
                case "ship":
                    square.hitSquare(ColorPalette.getFlagColor());
                    break;
                case "water":
                    square.hitSquare(ColorPalette.getWaterHit());
                    break;
                default:
                    System.out.println("Unknown square type: " + square.getType());
                    break;
            }
        }
    }    
    
    /**
     * Checks if any ship in the current player's fleet has been sunk.
     * If a ship is sunk, its corresponding squares are updated to reflect the sunk state,
     * and the ship is removed from the player's fleet.
     * If the player's fleet becomes empty, the opponent is declared the winner.
     */
    public void checkIfShipSunk() {
        ArrayList<Battleship> fleet = currentPlayer.getFleet();
        
        for (int f = 0; f < fleet.size(); f++) {    
                
            Battleship ship = fleet.get(f);
            if (ship.checkIsSunk()) {
                ArrayList<Square> shipSquares = ship.getShipParts();
                
                Grid opposingGrid = p1.getTargetGrid();
                
                if (currentPlayer.equals(p1)) {
                     opposingGrid = p2.getTargetGrid();
                 }
                
                for (int s = 0; s < shipSquares.size(); s++) {
                    Square shipSquare = shipSquares.get(s);
                    
                    shipSquare.setBackground(ColorPalette.getSunkShip());
                    
                    opposingGrid.getSquare(shipSquare.getPositionX(), shipSquare.getPositionY()).setBackground(ColorPalette.getSunkShip());
                }
                
                currentPlayer.removeShip(ship);
                
                if (fleet.isEmpty()) {
                    if (currentPlayer.equals(p1)) {
                        System.out.println("Player 2 won!");
                        p1.dispatchEvent(new WindowEvent(p1, WindowEvent.WINDOW_CLOSING));
                        p2.dispatchEvent(new WindowEvent(p2, WindowEvent.WINDOW_CLOSING));
                    } else if (currentPlayer.equals(p2)) {
                        System.out.println("Player 1 won!");
                        p1.dispatchEvent(new WindowEvent(p1, WindowEvent.WINDOW_CLOSING));
                        p2.dispatchEvent(new WindowEvent(p2, WindowEvent.WINDOW_CLOSING));
                    } else {
                        System.out.println("fleet empty Doesn't work");
                    }
                }
            }
        }
    }
    
    /**
     * Starts the game by initializing the current player, setting up the fleets,
     * and attaching the necessary event listeners to the display grids.
     */
    public void run() {
        currentPlayer = p1;
        
        p1.setFleet(p1.getPlayerGrid().getShips());
        p2.setFleet(p2.getPlayerGrid().getShips());
                           
        addBoardEventListeners(p1, p1.getTargetGrid(), p2.getPlayerGrid());
        addBoardEventListeners(p2, p2.getTargetGrid(), p1.getPlayerGrid());
        
        p1.setOnStartButtonCreated(button -> button.addActionListener(e -> gameRun(p1)));
        p2.setOnStartButtonCreated(button -> button.addActionListener(e -> gameRun(p2)));

    }
    
    /**
     * Executes the game run when both players are ready.
     * It sets up the ship positions on the target grids based on the players' ship placements
     * and disables the start buttons to prevent reinitialization.
     */
    public void gameRun(Player p) {
        p.setIsReady(true);
        p.getStart().setEnabled(false);
        if (p1.getIsReady() && p2.getIsReady()) {
            setUpShips(p1, p2);
            setUpShips(p2, p1);
        }
    }
    
    /**
     * Attaches board event listeners for the specified player.
     * For each corresponding square in the opponent's grid and the player's own grid,
     * an action listener is added. When a square is clicked, if it is the player's turn
     * and both displays are ready, the method processes a hit on both squares, switches
     * the current player, and checks if any ship has sunk.
     *
     * @param p      The player associated with these event listeners.
     * @param opGrid The opponent's grid where hits are processed.
     * @param pGrid  The player's grid that provides additional feedback for the hit.
     */
    public void addBoardEventListeners(Player p, Grid opGrid, Grid pGrid) {        
        Square[][] opBoard = opGrid.getBoard();
        Square[][] pBoard = pGrid.getBoard();
             
        for (int row = 0; row < opBoard.length; row++) {
            for (int col = 0; col < opBoard[0].length; col++) {
                Square opSquare = opBoard[row][col];
                Square pSquare = pBoard[row][col];
   
                opSquare.addActionListener(new ActionListener() { 
                    public void actionPerformed(ActionEvent e) {
                        if (p == currentPlayer && p1.getIsReady() && p2.getIsReady()) {
                            sendHit(opSquare);
                            sendHit(pSquare);
                                                
                            if (currentPlayer == p1) {
                                currentPlayer = p2;
                            } else {
                                currentPlayer = p1;
                            }
                            
                            checkIfShipSunk();
                        }                   
                    } 
                });
            }
        }      
    }
                 
    /**
     * Sets up ships on the given opponent grid based on the player's grid.
     * For each square in the player's grid that contains a ship, the method marks the corresponding
     * square on the opponent's target grid as containing a ship.
     *
     * @param pGrid  The grid with the player's ship placements.
     * @param opGrid The opponent's grid that will reflect the ship positions.
     */
    public void setUpShips(Player p, Player op) {
        for (int row = 0; row < p.getGridRow(); row++) {
            for (int col = 0; col < p.getGridCol(); col++) {
                Square square = p.getGridSquare(row, col);
                  
                if ("ship".equals(square.getType())) {
                    op.getGridSquare(row, col).setType("ship");
                }
            }
        }
    }
}
