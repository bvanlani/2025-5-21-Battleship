package dev.bvanlani.battleship; /**
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

    public static void main(String[] args) {
        try{
            GameManager gameManager = new GameManager();
            GameInterface stub = (GameInterface) UnicastRemoteObject.exportObject(gameManager, 0);
            Registry registry = LocateRegistry.createRegistry(1234);
            registry.rebind("GameManager", stub);
            System.out.println("Server has started. Waiting for players to connect...");


        }catch(Exception e){
            e.printStackTrace();
        }
    }




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
    public GameManager() throws RemoteException {

        currentPlayer = null;

    }

    public void runWhenReady() {
        synchronized (this) {
            while (p1==null||p2 == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Both players connected. Starting game...");
        run();
    }
    
    /**
     * Processes a hit on the specified square.
     * If the square has not been hit before, this method updates the square's appearance
     * based on its type (e.g., ship or water).
     *
     *
     */
    public void callHit(int row, int col) {

        Player defender = (currentPlayer == p1) ? p2 : p1;
        defender.receivePlayerHit(row, col);

        currentPlayer.receiveTargetHit(row, col);

        currentPlayer = (currentPlayer == p1) ? p2 : p1;

    }    

    /**
     * Starts the game by initializing the current player, setting up the fleets,
     * and attaching the necessary event listeners to the display grids.
     */
    public void run() {
        currentPlayer = p1;
        
        p1.initializeFleet();
        p2.initializeFleet();
                           
        addBoardEventListeners();
        addBoardEventListeners();

    }
    
    /**
     * Executes the game run when both players are ready.
     * It sets up the ship positions on the target grids based on the players' ship placements
     * and disables the start buttons to prevent reinitialization.
     */
    public void gameRun(Player p) {
        p.setIsReady(true);
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
     */
    public void addBoardEventListeners() {
        p1.addBoardEventListeners();
        p2.addBoardEventListeners();
    }
                 
    /**
     * Sets up ships on the given opponent grid based on the player's grid.
     * For each square in the player's grid that contains a ship, the method marks the corresponding
     * square on the opponent's target grid as containing a ship.
     *
     */
    public void setUpShips(Player player, Player other) {
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

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public void sinkBattleship(Player player, int length, Direction dir, int row, int col) {
        if (player.equals(p1)) {
            p2.sinkBattleship(length, dir, row, col);
        } else if (player.equals(p2)) {
            p1.sinkBattleship(length, dir, row, col);
        }
    }

    public void checkWin(){
        if(p1.fleetSize() == 0){
            System.out.print("Player 2 wins!");
        }
        if(p2.fleetSize() == 0){
            System.out.print("Player 1 wins!");
        }
    }

    public void addPlayer(Player player) {
        if (p1 == null) {
            p1 = player;
            System.out.println("Player 1: \""+player.getName()+"\" connected.");
        } else if (p2 == null) {
            p2 = player;
            System.out.println("Player 2: \""+player.getName()+"\" connected.");
            notifyAll();
        }
    }
}
