package dungeonmania.main.entities.statics;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.main.collect_StrPattern.*;

public class FloorSwitch extends StaticEntities {

	/**
	 * Constructor for FloorSwitch
	 * @param dungeon
	 * @param entity
	 */
	public FloorSwitch(Dungeon dungeon, EntityResponse entity) {
		super(dungeon, entity, new NoCollect());
		this.setPassable(true);
	}

	@Override
	public void update() {
	}

	public boolean isTriggered() {

		for (Entity entity : getDungeon().getEntities()) {
			if (entity.getPosition().equals(this.getPosition())) {
				if (entity instanceof Boulder) {
					return true;
				}
			}
		}
		return false;

	}
}
