/**
 * GameManager.java
 *
 * Manages the game state for the Battleship game.
 *
 * Date: May 28, 2025
 * Author: Ethan Benzaquen and Ethan Drane
 */

import java.awt.event.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * The GameManager class is responsible for managing players, game state,
 * event listeners, and the interactions between the game components.
 * It initializes the players, sets up the display boards, handles player
 * actions, and monitors the game progress (e.g., sending hits and checking for sunk ships).
 */
public class GameManager implements GameInterface {

    /**
     * Entry point for the server. Initializes the GameManager and binds it to the RMI registry.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            GameManager gameManager = new GameManager();
            GameInterface stub = (GameInterface) UnicastRemoteObject.exportObject(gameManager, 0);
            Registry registry = LocateRegistry.createRegistry(1234);
            registry.rebind("GameManager", stub);
            System.out.println("Server has started. Waiting for players to connect...");
            gameManager.runWhenReady();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** The current player whose turn is active. */
    private PlayerInterface currentPlayer;

    /** Display for player 1. */
    private PlayerInterface p1;

    /** Display for player 2. */
    private PlayerInterface p2;

    /**
     * Constructs a new GameManager instance.
     * Initializes player objects and display boards for both players.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    public GameManager() throws RemoteException {
        currentPlayer = null;
    }

    /**
     * Waits until both players are connected, then starts the game.
     */
    public void runWhenReady() {
        synchronized (this) {
            while (p1 == null || p2 == null) {
                try {
                    wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Both players connected. Starting game...");
    }

    /**
     * Processes a hit on the specified square.
     * Sends the hit to the opponent and updates the current player's target grid.
     * Switches the turn to the other player.
     *
     * @param row The row index of the hit.
     * @param col The column index of the hit.
     */
    public void callHit(int row, int col) {
        try {
            currentPlayer.receiveTargetHit(row, col);
        
            PlayerInterface defender = (currentPlayer == p1) ? p2 : p1;
            defender.receivePlayerHit(row, col);

            currentPlayer = (currentPlayer == p1) ? p2 : p1;
            //System.out.println("Sent hit to "+ currentPlayer + " at ("+row+","+col+")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the game by initializing the current player, setting up the fleets,
     * and attaching the necessary event listeners to the display grids.
     */
    public void run() {
        try {
            currentPlayer = p1;
            p1.initializeFleet();
            p2.initializeFleet();
            addBoardEventListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes the game run when both players are ready.
     * Sets up ship positions on the target grids and disables start buttons.
     *
     * @param p The player who is ready.
     */
    public void gameRun(PlayerInterface p) {
        try {
            p.setIsReady(true);
            if(p2 != null){
               if (p1.getIsReady() && p2.getIsReady()) {
                   setUpShips(p1, p2);
                   setUpShips(p2, p1);
                   run();
               }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Attaches board event listeners for both players.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    public void addBoardEventListeners() throws RemoteException {
        System.out.println("Adding board event listeners...");
        p1.addBoardEventListeners();
        p2.addBoardEventListeners();
    }

    /**
     * Sets up ships on the given opponent grid based on the player's grid.
     * Marks corresponding squares on the target grid as containing enemy ships.
     *
     * @param player The player whose target grid is being updated.
     * @param other The opponent whose ship positions are being read.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void setUpShips(PlayerInterface player, PlayerInterface other) throws RemoteException {
        int boardSize = 10;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                String type = other.getPlayerSquareType(row, col);
                if ("ship".equals(type)) {
                    player.setTargetSquareType(row, col, "enemy_ship");
                }
            }
        }
    }

    /**
     * Returns the current player whose turn it is.
     *
     * @return The current player.
     */
    public String getCurrentPlayer() throws RemoteException{
        return currentPlayer.getName();
    }

    /**
     * Notifies the opponent to sink a battleship at the specified location.
     *
     * @param player The player who initiated the sink.
     * @param length The length of the ship.
     * @param dir The direction of the ship.
     * @param row The starting row of the ship.
     * @param col The starting column of the ship.
     */
    public void sinkBattleship(PlayerInterface player, int length, Direction dir, int row, int col) {
        try {
            if (player.equals(p1)) {
                p2.sinkBattleship(length, dir, row, col);
            } else if (player.equals(p2)) {
                p1.sinkBattleship(length, dir, row, col);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if either player has won the game by sinking all opponent ships.
     */
    public void checkWin() {
        try {
            if (p1.fleetSize() == 0) {
               p2.win();
               p1.lose();
            }
            if (p2.fleetSize() == 0) {
                p1.win();
                p2.lose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a player to the game. If both players are connected, notifies the waiting thread.
     *
     * @param player The player to add.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void addPlayer(PlayerInterface player) throws RemoteException {
        if (p1 == null) {
            p1 = player;
            System.out.println("Player 1: \"" + player.getName() + "\" connected.");
        } else if (p2 == null) {
            p2 = player;
            System.out.println("Player 2: \"" + player.getName() + "\" connected.");
            synchronized(this){
               notifyAll();
            }
        }
    }
}
