package dungeonmania.main.entities.statics;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.collect_StrPattern.CollectBehaviour;
import dungeonmania.response.models.EntityResponse;

public abstract class StaticEntities extends Entity {

    final int layer = 1;

    /**
     * Constructor for Static Entities
     * @param dungeon
     * @param entity
     * @param cBehaviour
     */
    public StaticEntities(Dungeon dungeon, EntityResponse entity, CollectBehaviour cBehaviour) {
        super(dungeon, entity);
    }

    /**
     * Constructor for Static Entities
     */
    public StaticEntities(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
    }
}
