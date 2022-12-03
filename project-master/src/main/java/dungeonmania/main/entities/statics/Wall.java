package dungeonmania.main.entities.statics;

import dungeonmania.main.Dungeon;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.main.collect_StrPattern.*;

public class Wall extends StaticEntities {

    /**
     * Constructor for Wall
     * @param dungeon
     * @param entity
     */
    public Wall(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity, new NoCollect());
        this.setPassable(false);
    }

    @Override
    public void update() {
    }
}
