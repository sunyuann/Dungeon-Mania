package dungeonmania.main.entities.statics;

import java.util.Random;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.main.entities.Player;
import dungeonmania.main.entities.moving.MovingEntities;
import dungeonmania.main.movement_StrPattern.SlowMovement;

public class SwampTile extends StaticEntities {

    int delay;

	public SwampTile(Dungeon dungeon, EntityResponse entity) {
		super(dungeon, entity, new NoCollect());
		this.setPassable(true);
        this.setPosition(getPosition().asLayer(-1));

        // randomize 3 or 2 ticks delay
        int random = new Random().nextInt(10);
        if (random < 3) {
            delay = 3;
        } else {
            delay = 2;
        }
	}

    public int getDelay() {
        return this.delay;
    }

	@Override
    public void update() {
        setPosition(getPosition().asLayer(0));
        for (Entity entityOn: getEntitiesOnPosition()) {
            if (entityOn instanceof Player || entityOn instanceof MovingEntities) {
                if (!(entityOn.getMBehaviour() instanceof SlowMovement)) {
                    entityOn.setMBehaviour(new SlowMovement(delay, entityOn.getMBehaviour()));
                }
            }
        }
        setPosition(getPosition().asLayer(-1));
    }
}
