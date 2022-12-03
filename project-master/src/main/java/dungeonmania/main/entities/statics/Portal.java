package dungeonmania.main.entities.statics;

import java.util.List;
import java.util.Map;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.main.entities.Player;

public class Portal extends StaticEntities {

	/**
	 * Constructor for Portal
	 * @param dungeon
	 * @param entity
	 */
	public Portal(Dungeon dungeon, EntityResponse entity) {
		super(dungeon, entity, new NoCollect());
		this.setPassable(true);
	}

	@Override
	public void update() {
		Player player = getDungeon().getPlayer();
		if (getEntitiesOnPosition().contains(player)) {
			Position newPosition = teleportTo(player.getCurDirection());
			player.setPosition(newPosition);
		}
	}

	// eg. like the newPosition contains entities which are not passable --> in this
	// case we should just not allow portal movement to happen(you move back to
	// where you were before)
	// Find the position of the other portal, and set players position there.
	public Position teleportTo(Direction direction) {
		
		for (Entity e : this.getDungeon().getEntities()) {
			// If entity is a portal and same colour, BUT different position, then we have
			// found the other portal.
			if (e.getType().equals(this.getType()) && !e.getPosition().equals(this.getPosition())) {
				// check all spaces adjacent to portal
				Map<Direction, List<Entity>> adjacent = e.getAdjacentEntitiesDirection();
				// if the spot in player's current direction is empty, then teleport there
				if (adjacent.get(direction).isEmpty()) {
					return e.getPosition().translateBy(direction);
				}
				// else loop through all adjacent directions
				for (Direction d : adjacent.keySet()) {
					if (adjacent.get(d).isEmpty()) {
						return e.getPosition().translateBy(d);
					}
				}
			}
		}
		// if can't find other portal or portal is blocked, then just return current
		// portal position
		return this.getPosition();
	}
}
