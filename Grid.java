import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * The Grid class represents a game board for a battleship-style game.
 * It extends JPanel and contains a 2D array of Square objects.
 */
public class Grid extends JPanel {
    /** The 2D array representing the board grid. */
    private final Square[][] board;

    /** Number of rows in the grid. */
    private final int rows;

    /** Number of columns in the grid. */
    private final int cols;

    /** Size of each square tile in pixels. */
    private final int size = 20;

    /** List of battleships placed on the grid. */
    private ArrayList<Battleship> ships;

    /**
     * Constructs a Grid object with the specified number of rows and columns.
     * Initializes the board and sets up the layout.
     *
     * @param rows The number of rows in the grid.
     * @param cols The number of columns in the grid.
     */
    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        setLayout(new GridLayout(rows, cols, 2, 2));
        setBackground(ColorPalette.getBackgroundColor());
        board = new Square[rows][cols];
        ships = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = new Square(r, c, false, "water");
                board[r][c].setPreferredSize(new Dimension(size, size));
                board[r][c].setOpaque(true);
                board[r][c].setBackground(ColorPalette.getWaterColor());
                board[r][c].setBorderPainted(false);
                this.add(board[r][c]);
            }
        }
    }

    /**
     * Returns the Square object at the specified row and column.
     *
     * @param row The row index of the square.
     * @param col The column index of the square.
     * @return The Square at the specified position.
     */
    public Square getSquare(int row, int col) {
        return board[row][col];
    }

    /**
     * Returns the entire board as a 2D array of Square objects.
     *
     * @return The board grid.
     */
    public Square[][] getBoard() {
        return board;
    }

    /**
     * Returns the number of rows in the grid.
     *
     * @return The number of rows.
     */
    public int getRow() {
        return rows;
    }

    /**
     * Returns the number of columns in the grid.
     *
     * @return The number of columns.
     */
    public int getCol() {
        return cols;
    }

    /**
     * Sets the type of a square at the specified position.
     *
     * @param row The row index.
     * @param col The column index.
     * @param type The type to set (e.g., "ship", "water").
     */
    public void setSquare(int row, int col, String type) {
        board[row][col].setType(type);
    }

    /**
     * Resets the grid by clearing the background color and enabling all squares.
     */
    public void resetGrid() {
        for (JButton[] row : board) {
            for (JButton square : row) {
                square.setBackground(null);
                square.setEnabled(true);
            }
        }
    }

    /**
     * Places ship tiles at the specified location and direction.
     * Updates the board and the Battleship object with the ship parts.
     *
     * @param startRow The starting row index.
     * @param startCol The starting column index.
     * @param length The length of the ship.
     * @param dir The direction in which the ship extends.
     * @param bs The Battleship object to track ship parts.
     */
    public void placeShipTiles(int startRow, int startCol, int length, Direction dir, Battleship bs) {
        ships.add(bs);
        for (int i = 0; i < length; i++) {
            int r = startRow + dir.dRow * i;
            int c = startCol + dir.dCol * i;
            if (r >= 0 && r < rows && c >= 0 && c < cols) {
                board[r][c].setType("ship");
                bs.addShipPart(board[r][c]);
            }
        }
    }

    /**
     * Checks whether a ship can be placed at the specified location and direction.
     *
     * @param startRow The starting row index.
     * @param startCol The starting column index.
     * @param length The length of the ship.
     * @param dir The direction in which the ship extends.
     * @return true if the ship can be placed, false otherwise.
     */
    public boolean canPlaceShip(int startRow, int startCol, int length, Direction dir) {
        for (int i = 0; i < length; i++) {
            int r = startRow + dir.dRow * i;
            int c = startCol + dir.dCol * i;

            if (r < 0 || r >= rows || c < 0 || c >= cols) return false;
            if ("ship".equals(board[r][c].getType())) return false;
        }
        return true;
    }

    /**
     * Returns the list of Battleship objects placed on the grid.
     *
     * @return The list of ships.
     */
    public ArrayList<Battleship> getShips() {
        return ships;
    }
}
