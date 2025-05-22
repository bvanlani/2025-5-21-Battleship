/**
 * Represents the possible directions a battleship can be oriented on a grid, with support for clockwise rotation.
 */
public enum Direction {
    /** Upward direction, moving one row up. */
    UP(-1, 0),
    
    /** Downward direction, moving one row down. */
    DOWN(1, 0),
    
    /** Leftward direction, moving one column left. */
    LEFT(0, -1),
    
    /** Rightward direction, moving one column right. */
    RIGHT(0, 1);

    /** The change in row index for this direction. */
    public final int dRow;
    
    /** The change in column index for this direction. */
    public final int dCol;

    /**
     * Constructs a Direction with the specified row and column changes.
     *
     * @param dRow the change in row index
     * @param dCol the change in column index
     */
    Direction(int dRow, int dCol) {
        this.dRow = dRow;
        this.dCol = dCol;
    }

    /**
     * Returns the next direction in a clockwise rotation sequence (UP -> RIGHT -> DOWN -> LEFT -> UP).
     *
     * @return the next direction in the clockwise rotation
     */
    public Direction next() {
        return switch (this) {
            case UP -> RIGHT;
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
        };
    }
}