package dungeonmania.util;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    NONE(0, 0)
    ;

    private Position offset;

    private Direction(Position offset) {
        this.offset = offset;
    }

    private Direction(int x, int y) {
        this.offset = new Position(x, y);
    }

    public Position getOffset() {
        return this.offset;
    }

    /**
     * Helper function to reverse a direction
     * @param direction
     * @return
     */
    public static Direction reverse(Direction direction) {
        Direction reverse = Direction.NONE;
        switch (direction) {
            case UP:
                reverse = Direction.DOWN;
                break;
            case DOWN:
                reverse = Direction.UP;
                break;
            case LEFT:
                reverse = Direction.RIGHT;
                break;
            case RIGHT:
                reverse = Direction.LEFT;
                break;
            case NONE:
                break;
        }
        return reverse;
    }
}
