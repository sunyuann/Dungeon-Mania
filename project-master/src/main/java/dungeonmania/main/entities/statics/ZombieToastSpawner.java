package dungeonmania.main.entities.statics;

import dungeonmania.main.Dungeon;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

public class ZombieToastSpawner extends StaticEntities {

	Integer zombiec = 0;

	/**
	 * Constructor for Zombie Toast Spawner
	 * @param dungeon
	 * @param entity
	 */
	public ZombieToastSpawner(Dungeon dungeon, EntityResponse entity) {
		super(dungeon, new EntityResponse(entity.getId(), entity.getType(), entity.getPosition(), true));
		this.setPassable(false);
	}

	@Override
	public void update() {
	}

	/**
	 * Method that spawns zombie - goes through each cardinal direction and checks for the first vacancy
	 */
	public void spawnZombie() {
		// spawn a zombie in cardinal open square adjacent to the spawner every 20 ticks
		if (getAdjacentEntitiesDirection().get(Direction.UP).isEmpty()) {
			createZombie(1);
		} else if (getAdjacentEntitiesDirection().get(Direction.DOWN).isEmpty()) {
			createZombie(5);
		} else if (getAdjacentEntitiesDirection().get(Direction.LEFT).isEmpty()) {
			createZombie(7);
		} else if (getAdjacentEntitiesDirection().get(Direction.RIGHT).isEmpty()) {
			createZombie(3);
		}
	}

	/**
	 * Creates a zombie in the specified direction
	 */
	public void createZombie(Integer pos) {
		// dungeon, id, type, position, isinteractable
		zombiec += 1;
		String id = "zombie" + zombiec + "-(" + getPosition().getAdjacentPositions().get(pos).getX() + ", "
				+ getPosition().getAdjacentPositions().get(pos).getY() + ")";
		getDungeon()
				.createEntity(new EntityResponse(id, "zombie", getPosition().getAdjacentPositions().get(pos), false));
	}
}
