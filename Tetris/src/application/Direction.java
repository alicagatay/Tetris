/*
 *Course: AP CS A, Year: 2017-2018, Instructor: Sedat Yalçın, Student: Ali Çağatay, Lecture: Tetris 
 */
package application;
/**
 * initiliazing the directions by using the enum class.
 */
public enum Direction {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    int x, y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * initiliazing the preview direction of the shape
     */
    public Direction prev() {
        int nextIndex = ordinal() - 1;

        if (nextIndex == -1) {
            nextIndex = Direction.values().length - 1;
        }

        return Direction.values()[nextIndex];
    }
    /**
     * this code piece is used for changing the direction of the shape based on the previous direction of the shape.
     */
    public Direction next() {
        int nextIndex = ordinal() + 1;

        if (nextIndex == Direction.values().length) {
            nextIndex = 0;
        }

        return Direction.values()[nextIndex];
    }
}