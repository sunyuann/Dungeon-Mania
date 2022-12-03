package dungeonmania.main.Goals;

import dungeonmania.main.Dungeon;
import dungeonmania.main.entities.statics.FloorSwitch;
import dungeonmania.main.entities.statics.StaticEntities;
import java.util.ArrayList;
import java.util.List;

public class BoulderGoal implements Goal {
    private Dungeon dungeon;

    public BoulderGoal(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    /**
     * Implement Goal interface method
     */
    @Override
    public boolean evaluate() {
        // if all floor switches are triggered, complete game.
        List<FloorSwitch> floor_switches = new ArrayList<FloorSwitch>();
        for (StaticEntities s : dungeon.getStaticEntities()) {
            if (s instanceof FloorSwitch) {
                floor_switches.add((FloorSwitch) s);
            }
        }
        Integer c, i;
        c = i = 0;
        for (i = 0; i < floor_switches.size(); i++) {
            if (floor_switches.get(i).isTriggered()) {
                c++;
            }
        }
        // if c = i then that means all floor switches are triggered
        if (c.equals(i)) {
            return true;
        }
        return false;
    }

    /**
     * Implement Goal interface method
     */
    @Override
    public void add(Goal goal) {
    }
}
