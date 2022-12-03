package dungeonmania.main.entities.statics.Door;

public class DoorOpenState implements DoorState {
    
    Door door;

    /**
     * Constructor  for Door
     * @param door
     */
    public DoorOpenState(Door door) {
        this.door = door;
        door.setPassable(true);
    }

    @Override
    public void openDoor() {
            // Do nothing..
    }
}
