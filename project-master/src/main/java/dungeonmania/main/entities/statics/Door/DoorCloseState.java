package dungeonmania.main.entities.statics.Door;

import dungeonmania.main.Item;
import dungeonmania.main.entities.items.KeyItem;

public class DoorCloseState implements DoorState {
	
	Door door;

	/**
	 * Constructor of DoorCloseState
	 * @param door
	 */
	public DoorCloseState(Door door) {
		this.door = door;
		door.setPassable(false);
	}

	/**
	 * Implements DoorState interface method
	 * Attempt to unlock door based on player's inventory items
	 */
	@Override
	public void openDoor() {

		if (unlock()) {
			door.setState(door.getDoorOpenState());
			door.setType("door_unlocked");
			door.setPassable(true);
		}
		// If nothing happens, then do nothing
	}

	/**
	 * Helper function - checks if door can be unlocked 
	 * Removes key/ uses sun stone if can be used
	 * @return true if door can be unlocked 
	 */
	private boolean unlock() {

		// doors can be unlocked via keys or sunstones
		KeyItem key = door.getDungeon().getDungeonInventory().getKey();
		Item sunStone = door.getDungeon().getDungeonInventory().getItemOfType("sun_stone");

		boolean unlocked = false;

		if (key != null) {
			if (key.getType().equals(door.getRequiredKeyType())) {
				// keys are removed after use
				door.getDungeon().removeFromInventory(key);
				unlocked = true;
			}
		}  
		
		// sunstones are retained after use so simply set unlocked to true 
		if (sunStone != null) {
			unlocked = true;
		}

		return unlocked;
	}

}
