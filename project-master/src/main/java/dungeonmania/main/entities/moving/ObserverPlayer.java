package dungeonmania.main.entities.moving;

import dungeonmania.util.Position;

// interface for player's allies
public interface ObserverPlayer {
	
	public void positionUpdate(Position position);

	public void attachAllyUpdate();

	public void detachAllyUpdate();

	public Position getPosition();

	public void tickUpdates();
	
    
}
