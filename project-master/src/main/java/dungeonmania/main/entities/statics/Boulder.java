package dungeonmania.main.entities.statics;

import dungeonmania.main.Dungeon;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.main.collect_StrPattern.*;

public class Boulder extends StaticEntities {

	/**
	 * Constructor for Boulder
	 * @param dungeon
	 * @param entity
	 */
	public Boulder(Dungeon dungeon, EntityResponse entity) {
		super(dungeon, entity, new NoCollect());
		this.setPassable(false);
	}

	@Override
    public void update() {}

	public boolean pushable(Direction direction) {
		// check if boulder is pushable in given direction
		return isEntityInDirectionPassable(direction);
	}
}