package dungeonmania.main.Goals;

import java.util.ArrayList;

public class OrGoal implements Goal {
    ArrayList<Goal> children = new ArrayList<Goal>();

    /**
     * Implement Goal interface method
     */
    @Override
    public void add(Goal node) {
        children.add(node);
    }

    /**
     * Implement Goal interface method
     */
    @Override
    public boolean evaluate() {
        for (Goal b : children) {
            if (b.evaluate()) {
                return true;
            }
        }
        return false;
    }
}
