package dungeonmania.main.movement_StrPattern;

import java.util.Arrays;
import java.util.List;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.util.Direction;

public class CircularMovement implements MoveBehaviour {

    List<Direction> movement = Arrays.asList(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.LEFT, Direction.LEFT, Direction.UP, Direction.UP, Direction.RIGHT);
    Integer movement_index = 0;
    
    public void move(Dungeon dungeon, Entity entity) {
        entity.setPosition(movement.get(movement_index));
        movement_index++;
        if (movement_index > 8) {
            movement_index = 1;
        }
    }
}
