package dungeonmania.main.entities.moving;

import dungeonmania.util.Position;

// interface for moving entities
public interface MovingEntities {
    public void move();
    public String getId();
    public Position getPosition();
}
