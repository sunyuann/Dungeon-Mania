package dungeonmania.main.entities.statics;

import dungeonmania.main.Dungeon;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.main.collect_StrPattern.*;

public class Exit extends StaticEntities {

	/**
	 * Constructor for Exit
	 * @param dungeon
	 * @param entity
	 */
	public Exit(Dungeon dungeon, EntityResponse entity) {
		super(dungeon, entity, new NoCollect());
		this.setPassable(true);
	}

	@Override
	public void update() {
	}

}
