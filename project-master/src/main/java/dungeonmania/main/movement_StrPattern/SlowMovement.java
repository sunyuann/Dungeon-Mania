package dungeonmania.main.movement_StrPattern;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.entities.moving.MovingEntities;

public class SlowMovement implements MoveBehaviour {

    private int delay;
    private MoveBehaviour movement;
    private int tick;

    public SlowMovement(int tick, MoveBehaviour movement) {
        this.delay = tick;
        this.movement = movement;
        this.tick = 0;
    }
    
    public void move(Dungeon dungeon, Entity entity) {
        tick++;
        dungeon.updateEntities();
        if (tick >= delay) {
            entity.setMBehaviour(movement);
            if (entity instanceof MovingEntities) {
                MovingEntities mEntity = (MovingEntities) entity;
                mEntity.move();
            }
        }
    }
}
