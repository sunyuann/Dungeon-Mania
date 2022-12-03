package dungeonmania.main.entities.statics.Door;

import dungeonmania.main.Dungeon;
import dungeonmania.main.entities.statics.StaticEntities;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.main.collect_StrPattern.*;

public class Door extends StaticEntities {

    /**
     * Attributes of DoorState
     */
    DoorState DoorCloseState;
    DoorState DoorOpenState;
    DoorState state;
    String required_key_type;

    /**
     * Constructor of Door
     * @param dungeon
     * @param entity
     */
    public Door(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity, new NoCollect());
        DoorCloseState = new DoorCloseState(this);
        DoorOpenState = new DoorOpenState(this);
        state = DoorCloseState;
        if (entity.getType().equals("door_locked_gold"))
                required_key_type = "key_gold";
        if (entity.getType().equals("door_locked_silver"))
                required_key_type = "key_silver";
        this.setPassable(false);
    }

    /**
     * Implements Abstract Method of Entity
     */
    @Override
    public void update() {
    }

    /**
     * attempt to open the door
     */
    public void open() {
        state.openDoor();
    }

    public void setState(DoorState state) {
        this.state = state;
    }

    public String getRequiredKeyType() {
        return required_key_type;
    }

    public DoorState getDoorOpenState() {
        return DoorOpenState;
    }

}
