import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.rmi.*;

/**
 * Represents a player in the Battleship game.
 * Handles player setup, ship placement, interaction with the game manager, and game events.
 */
public class Player extends UnicastRemoteObject implements PlayerInterface {

    /** Scanner for user input. */
    static Scanner scanner = new Scanner(System.in);

    /**
     * Entry point for the player client.
     * Connects to the server, registers the player, and initializes the game interface.
     */
    public static void main(String[] args) {
        try {
            System.out.print("Enter Server IP: ");
            String IP = scanner.nextLine();

            Registry registry = LocateRegistry.getRegistry(IP, 1234);
            GameInterface gm = (GameInterface) registry.lookup("GameManager");

            System.out.println("Connected to the server.\n");
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            Player player = new Player(name, gm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** The player's fleet of battleships. */
    private ArrayList<Battleship> fleet = new ArrayList<>();

    /** The player's name. */
    private final String name;

    /** The player's own grid. */
    private final Grid playerGrid;

    /** The player's target grid (opponent's board). */
    private final Grid targetGrid;

    /** List of ships to be placed during setup. */
    private final List<Battleship> shipsToPlace = new ArrayList<>();

    /** The current direction for ship placement. */
    private Direction currentDirection = Direction.RIGHT;

    /** Flag indicating whether the player is ready. */
    private boolean isReady = false;

    /** The display interface for the player. */
    private final Display dis;

    /** Reference to the game manager. */
    private GameInterface gm;

    /**
     * Constructs a Player object and registers it with the game manager.
     *
     * @param name The player's name.
     * @param gm   The game manager interface.
     * @throws RemoteException if a remote communication error occurs.
     */
    public Player(String name, GameInterface gm) throws RemoteException {
        super();
        this.name = name;
        this.gm = gm;

        playerGrid = new Grid(10, 10);
        targetGrid = new Grid(10, 10);
        this.dis = new Display(name, this);

        gm.addPlayer(this);
        setupShipStorage();
    }

    /** Initializes the list of ships to be placed and enables placement mode. */
    private void setupShipStorage() {
        shipsToPlace.add(new Battleship("Carrier", 5));
        shipsToPlace.add(new Battleship("Battleship", 4));
        shipsToPlace.add(new Battleship("Cruiser", 3));
        shipsToPlace.add(new Battleship("Submarine", 3));
        shipsToPlace.add(new Battleship("Destroyer", 2));

        dis.setShipLengthLabel("" + shipsToPlace.get(0).getLength());
        enablePlacementMode();
    }

    /** Enables ship placement mode by attaching listeners to the player's grid. */
    private void enablePlacementMode() {
        JButton[][] board = playerGrid.getBoard();
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                int row = r;
                int col = c;
                board[r][c].addActionListener(e -> handleShipPlacement(row, col));
            }
        }
    }

    /**
     * Handles the placement of a ship at the specified location.
     *
     * @param startRow The starting row.
     * @param startCol The starting column.
     */
    private void handleShipPlacement(int startRow, int startCol) {
        if (shipsToPlace.isEmpty()) return;

        int length = shipsToPlace.get(0).getLength();

        if (!playerGrid.canPlaceShip(startRow, startCol, length, currentDirection)) {
            System.out.println("Invalid ship placement at: (" + startRow + "," + startCol + ")");
            return;
        }

        playerGrid.placeShipTiles(startRow, startCol, length, currentDirection, shipsToPlace.get(0));
        shipsToPlace.get(0).setDirection(currentDirection);
        shipsToPlace.remove(0);

        if (shipsToPlace.isEmpty()) {
            dis.readyState();
        } else {
            dis.setShipLengthLabel("" + shipsToPlace.get(0).getLength());
        }
    }

    // --- GameManager interaction methods ---

    public void setGM(GameManager gm) {
        this.gm = gm;
    }

    public void setTargetSquareType(int row, int col, String type) {
        targetGrid.setSquare(row, col, type);
    }

    public String getPlayerSquareType(int row, int col) {
        return playerGrid.getSquare(row, col).getType();
    }

    public String getName() {
        return name;
    }

    public void gameRun() {
        try {
            gm.gameRun(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getIsReady() {
        return isReady;
    }

    public void setIsReady(boolean isReady) {
        this.isReady = isReady;
    }

    public Grid getPlayerGrid() {
        return playerGrid;
    }

    public Grid getTargetGrid() {
        return targetGrid;
    }

    public Direction getDirection() {
        return currentDirection;
    }

    public void setDirection(Direction dir) {
        this.currentDirection = dir;
    }

    public Display getDisplay() {
        return dis;
    }

    // --- Fleet management methods ---

    /**
     * Returns the player's fleet.
     *
     * @return The list of battleships.
     */
    public ArrayList<Battleship> getFleet() {
        return fleet;
    }

    /**
     * Sets the player's fleet.
     *
     * @param in_fleet The fleet to assign.
     */
    public void setFleet(ArrayList<Battleship> in_fleet) {
        fleet = in_fleet;
    }

    /**
     * Removes a ship from the player's fleet.
     *
     * @param ship The ship to remove.
     */
    public void removeShip(Battleship ship) {
        fleet.remove(ship);
    }

    /** Initializes the fleet from the player's grid. */
    public void initializeFleet() {
        setFleet(playerGrid.getShips());
    }

    /**
     * Returns the number of ships remaining in the fleet.
     *
     * @return The fleet size.
     */
    public int fleetSize() {
        return fleet.size();
    }
    
    public String getCurrentPlayer() throws RemoteException{
        return gm.getCurrentPlayer();
    }
    
    public void callHit(int x, int y) throws RemoteException{
        gm.callHit(x, y);
    }

    /** Adds event listeners to the target grid for handling attacks. */
    public void addBoardEventListeners() {
        try {
            Square[][] opBoard = targetGrid.getBoard();

            for (int row = 0; row < opBoard.length; row++) {
                for (int col = 0; col < opBoard[0].length; col++) {
                    Square opSquare = opBoard[row][col];
                    
                    Player p = this;
                    int x = opSquare.getPositionX();
                    int y = opSquare.getPositionY();

                    opSquare.addActionListener(e -> {
                        try {
                            //System.out.println(x+"," +y);
                            if (p.getCurrentPlayer().equals(p.getName())) {
                                p.callHit(x, y);
                            }
                            if(p.getDisplay().getStartText().equals("Opponents Turn")){
                                   p.getDisplay().setStartText("Your turn");
                                }else{
                                   p.getDisplay().setStartText("Opponents Turn");
                                }
                        } catch (Exception t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Game event handling ---

    /**
     * Handles a hit received on the player's grid.
     *
     * @param row The row of the hit.
     * @param col The column of the hit.
     */
    public void receivePlayerHit(int row, int col) {
        playerGrid.getSquare(row, col).hitSquare();
        checkIfShipSunk();
    }

    /**
     * Handles a hit on the target grid (opponent's board).
     *
     * @param row The row of the hit.
     * @param col The column of the hit.
     */
    public void receiveTargetHit(int row, int col) {
        targetGrid.getSquare(row, col).hitSquare();
    }

    /** Checks if any ship in the fleet has been sunk and updates the game state. */
    public void checkIfShipSunk() {
        try {
            for (int i = fleet.size() - 1; i >= 0; i--) {
                if (fleet.get(i).checkIsSunk()) {
                    ArrayList<Square> shipSquares = fleet.get(i).getShipParts();

                    for (Square shipSquare : shipSquares) {
                        shipSquare.setType("sunk");
                    }

                    gm.sinkBattleship((PlayerInterface)this, fleet.get(i).getLength(), fleet.get(i).getDirection(),
                            fleet.get(i).getStartX(), fleet.get(i).getStartY());

                    removeShip(fleet.get(i));
                    gm.checkWin();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /**
     * Marks a battleship as sunk on the target grid.
     * Updates the visual representation of the ship on the target grid.
     *
     * @param length The length of the ship.
     * @param dir    The direction the ship is facing.
     * @param startX The starting X (column) coordinate of the ship.
     * @param startY The starting Y (row) coordinate of the ship.
     */
    public void sinkBattleship(int length, Direction dir, int startX, int startY) {
        int x = startX;
        int y = startY;

        for (int i = 0; i < length; i++) {
            targetGrid.getSquare(x, y).setType("sunk");
            x += dir.dRow;
            y += dir.dCol;
        }
    }
    
    @Override
   public boolean equals(Object obj) {
       if (this == obj) return true; // same reference
       if (obj == null || getClass() != obj.getClass()) return false; // null or different class
   
       Player other = (Player) obj;
   
       // Compare relevant fields (adjust based on your class)
       return Objects.equals(this.name, other.getName()); // assuming playerId uniquely identifies a player
   }

}
