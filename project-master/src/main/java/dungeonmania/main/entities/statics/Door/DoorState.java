package dungeonmania.main.entities.statics.Door;

public interface DoorState {
        
        // you can't close a door after its opened, so no close method
        public void openDoor();
}
