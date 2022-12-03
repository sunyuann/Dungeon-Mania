package dungeonmania.main.movement_StrPattern;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;

// This interface instigates the strategy pattern for moveBehaviour of entities
public interface MoveBehaviour {

    public void move(Dungeon dungeon, Entity entity);

}
