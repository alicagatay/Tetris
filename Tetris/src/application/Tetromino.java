/*
 *Course: AP CS A, Year: 2017-2018, Instructor: Sedat Yalçın, Student: Ali Çağatay, Lecture: Tetris 
 */
package application;
/**
 * the imports of the project
 */
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static application.TetrisApp.TILE_SIZE;

/**
 * our tetrominoo class that creates the shapes
 */
public class Tetromino {
	/**
	 * the declarations of our variables
	 */
    public int x, y;
    public Color color;
    public List<Piece> pieces;

    /**
     * the Tetromino class
     */
    public Tetromino(Color color, Piece... pieces) {
        this.color = color;
        this.pieces = new ArrayList<>(Arrays.asList(pieces));

        for (Piece piece : this.pieces)
            piece.setParent(this);
    }
    
    /**
     * this method is used for moving the shapes.
     */
    public void move(int dx, int dy) {
        x += dx;
        y += dy;

        pieces.forEach(p -> {
            p.x += dx;
            p.y += dy;
        });
    }
    /**
     * this method initiliazes the direction of the shapes while moving.
     */
    public void move(Direction direction) {
        move(direction.x, direction.y);
    }
    
    /**
     * this method colorizes the shapes created before.
     */
    public void draw(GraphicsContext g) {
        g.setFill(color);

        pieces.forEach(p -> g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
    }
    /**
     * this method rotates the shape
     */
    public void rotateBack() {
        pieces.forEach(p -> p.setDirection(p.directions.stream().map(Direction::prev).collect(Collectors.toList()).toArray(new Direction[0])));
    }
    /**
     * this method rotates the shape
     */
    public void rotate() {
        pieces.forEach(p -> p.setDirection(p.directions.stream().map(Direction::next).collect(Collectors.toList()).toArray(new Direction[0])));
    }

    public void detach(int x, int y) {
        pieces.removeIf(p -> p.x == x && p.y == y);
    }
    /**
     * this method copies the shape
     */
    public Tetromino copy() {
        return new Tetromino(color, pieces.stream()
                .map(Piece::copy)
                .collect(Collectors.toList())
                .toArray(new Piece[0]));
    }
}