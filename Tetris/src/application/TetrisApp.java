/*
 *Course: AP CS A, Year: 2017-2018, Instructor: Sedat Yalçın, Student: Ali Çağatay, Lecture: Tetris 
 */
package application;
/*
 * The declarations of the variables used in TetrisApp class.
 */
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

//Creating our TetrisApp class for the Tetris object.
public class TetrisApp extends Application {
	/**
	 *The declarations of the variables of the maze. 
	 */
    public static final int TILE_SIZE = 30;
    public static final int GRID_WIDTH = 25;
    public static final int GRID_HEIGHT = 25;
    private double time;
    private GraphicsContext g;
    private int[][] grid = new int[GRID_WIDTH][GRID_HEIGHT];
    /**
	 * The declarations of the variables used for our Tetromino class.
	 */
    private List<Tetromino> original = new ArrayList<>();
    private List<Tetromino> tetrominos = new ArrayList<>();
    private Tetromino selected;

    /**
     * This code pice creates our pane for the maze.
     */
    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);
        Canvas canvas = new Canvas(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);
        g = canvas.getGraphicsContext2D();
        root.getChildren().addAll(canvas);

        /**
         * this code piece creates one of our shapes.
         */
        original.add(new Tetromino(Color.rgb(0, 255, 255),
                new Piece(0, Direction.DOWN),
                new Piece(1, Direction.RIGHT),
                new Piece(2, Direction.RIGHT),
                new Piece(1, Direction.DOWN)));
        
        /**
         * this code piece creates one of our shapes.
         */
        original.add(new Tetromino(Color.rgb(255, 0, 255),
                new Piece(0, Direction.DOWN),
                new Piece(1, Direction.DOWN),
                new Piece(2, Direction.DOWN),
                new Piece(3, Direction.DOWN)));
        
        
        /**
         * this code piece creates one of our shapes.
         */
        original.add(new Tetromino(Color.rgb(255, 255, 0),
                new Piece(0, Direction.DOWN),
                new Piece(1, Direction.LEFT),
                new Piece(1, Direction.RIGHT),
                new Piece(1, Direction.DOWN)));
        


        /**
         * this code piece creates an another shape.
         */
        original.add(new Tetromino(Color.rgb(255, 0, 0),
                new Piece(0, Direction.DOWN),
                new Piece(1, Direction.RIGHT),
                new Piece(1, Direction.RIGHT, Direction.DOWN),
                new Piece(1, Direction.DOWN)));
        
        /**
         * this code piece creates one of our shapes.
         */
        original.add(new Tetromino(Color.rgb(20, 255, 100),
                new Piece(0, Direction.DOWN),
                new Piece(1, Direction.LEFT),
                new Piece(2, Direction.LEFT),
                new Piece(1, Direction.DOWN)));
        
        /**
         * this code piece creates one of our shapes.
         */
        original.add(new Tetromino(Color.rgb(255, 150, 0),
                new Piece(0, Direction.DOWN),
                new Piece(1, Direction.LEFT),
                new Piece(1, Direction.DOWN)));
        

        /**
         * the spawn method spawns our shapes into the program.
         */
        spawn();

        /**
         * this code piece creates a time counter for our program.
         */
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.017;
                if (time >= 0.5) {
                    update();
                    render();
                    time = 0;
                }
            }
        };
        timer.start();
        return root;
    }

    /**
     * this code piece updates the program to update the scene.
     */
    private void update() {
        makeMove(p -> p.move(Direction.DOWN), p -> p.move(Direction.UP), true);
    }
    /**
     * this code piece renders the shape.
     */
    private void render() {
        g.clearRect(0, 0, GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);
        tetrominos.forEach(p -> p.draw(g));
    }
    /**
     * this code piece is used for placing the shape into the program.
     */
    private void placePiece(Piece piece) {
        grid[piece.x][piece.y]++;
    }
    /**
     * this code piece is used for removing the shape from the program.
     */
    private void removePiece(Piece piece) {
        grid[piece.x][piece.y]--;
    }

    private boolean isOffscreen(Piece piece) {
        return piece.x < 0 || piece.x >= GRID_WIDTH
                || piece.y < 0 || piece.y >= GRID_HEIGHT;
    }

    private void makeMove(Consumer<Tetromino> onSuccess, Consumer<Tetromino> onFail, boolean endMove) {
        selected.pieces.forEach(this::removePiece);

        onSuccess.accept(selected);

        boolean offscreen = selected.pieces.stream().anyMatch(this::isOffscreen);

        if (!offscreen) {
            selected.pieces.forEach(this::placePiece);
        } else {
            onFail.accept(selected);

            selected.pieces.forEach(this::placePiece);

            if (endMove) {
                sweep();
            }

            return;
        }

        if (!isValidState()) {
            selected.pieces.forEach(this::removePiece);

            onFail.accept(selected);

            selected.pieces.forEach(this::placePiece);

            if (endMove) {
                sweep();
            }
        }
    }

    private boolean isValidState() {
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                if (grid[x][y] > 1) {
                    return false;
                }
            }
        }

        return true;
    }

    private void sweep() {
        List<Integer> rows = sweepRows();
        rows.forEach(row -> {
            for (int x = 0; x < GRID_WIDTH; x++) {
                for (Tetromino tetromino : tetrominos) {
                    tetromino.detach(x, row);
                }

                grid[x][row]--;
            }
        });

        rows.forEach(row -> {
            tetrominos.stream().forEach(tetromino -> {
                tetromino.pieces.stream()
                        .filter(piece -> piece.y < row)
                        .forEach(piece -> {
                            removePiece(piece);
                            piece.y++;
                            placePiece(piece);
                        });
            });
        });

        spawn();
    }

    private List<Integer> sweepRows() {
        List<Integer> rows = new ArrayList<>();

        outer:
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                if (grid[x][y] != 1) {
                    continue outer;
                }
            }

            rows.add(y);
        }

        return rows;
    }
    /**
     * This code piece help our shapes to spawn.
     */
    private void spawn() {
        Tetromino tetromino = original.get(new Random().nextInt(original.size())).copy();
        tetromino.move(GRID_WIDTH / 2, 0);

        selected = tetromino;

        tetrominos.add(tetromino);
        tetromino.pieces.forEach(this::placePiece);

        if (!isValidState()) {
            System.out.println("Game Over");
            System.exit(0);
        }
    }
    /**
     * This code piece helps our project to start.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());
        /**
         * This code piece will help our shapes to move, and to turn.
         */
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                makeMove(p -> p.rotate(), p -> p.rotateBack(), false);
            } else if (e.getCode() == KeyCode.LEFT) {
                makeMove(p -> p.move(Direction.LEFT), p -> p.move(Direction.RIGHT), false);
            } else if (e.getCode() == KeyCode.RIGHT) {
                makeMove(p -> p.move(Direction.RIGHT), p -> p.move(Direction.LEFT), false);
            } else if (e.getCode() == KeyCode.DOWN) {
                makeMove(p -> p.move(Direction.DOWN), p -> p.move(Direction.UP), true);
            }

            render();
        });

        stage.setScene(scene);
        stage.show();
    }
    /**
     * This code piece helps our program to run all of the code I wrote in this project.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
