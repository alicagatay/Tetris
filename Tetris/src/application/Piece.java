package application;
/**
 * our imports
 */
import java.util.Arrays;
import java.util.List;

/**
 * our piece class
 */
public class Piece {
	/**
	 * needed variables for the piece class.
	 */
    public int distance;
    public List<Direction> directions;
    public Tetromino parent;
    public int x, y;
    
    /**
     * Piece method
     */
    public Piece(int distance, Direction... direction) {
        this.distance = distance;
        this.directions = Arrays.asList(direction);
    }

    /**
     * setParent method
     */
    public void setParent(Tetromino parent) {
        this.parent = parent;

        int dx = 0, dy = 0;

        for (Direction d : directions) {
            dx += distance * d.x;
            dy += distance * d.y;
        }

        x = parent.x + dx;
        y = parent.y + dy;
    }
    
    /**
     * setDirection method
     */
    public void setDirection(Direction... direction) {
        this.directions = Arrays.asList(direction);

        int dx = 0, dy = 0;

        for (Direction d : directions) {
            dx += distance * d.x;
            dy += distance * d.y;
        }

        x = parent.x + dx;
        y = parent.y + dy;
    }
    
    /**
     * copy method
     */
    public Piece copy() {
        return new Piece(distance, directions.toArray(new Direction[0]));
    }
}