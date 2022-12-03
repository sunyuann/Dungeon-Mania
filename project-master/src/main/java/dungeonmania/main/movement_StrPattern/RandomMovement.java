package dungeonmania.main.movement_StrPattern;

import java.util.Random;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.util.Direction;

public class RandomMovement implements MoveBehaviour {
    
    public void move(Dungeon dungeon, Entity entity) {
        Random random = new Random();
        int randInt = random.nextInt(4);
        switch (randInt) {
            case 0:
                if (entity.isEntityInDirectionPassable(Direction.UP)) {
                    entity.setPosition(Direction.UP);
                }
                break;
            case 1:
                if (entity.isEntityInDirectionPassable(Direction.DOWN)) {
                    entity.setPosition(Direction.DOWN);
                }
                break;
            case 2:
                if (entity.isEntityInDirectionPassable(Direction.LEFT)) {
                    entity.setPosition(Direction.LEFT);
                }
                break;
            case 3:
                if (entity.isEntityInDirectionPassable(Direction.RIGHT)) {
                    entity.setPosition(Direction.RIGHT);
                }
                break;
        }
    }
}
