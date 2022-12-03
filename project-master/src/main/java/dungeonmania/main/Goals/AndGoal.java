package dungeonmania.main.Goals;

import java.util.ArrayList;

public class AndGoal implements Goal {

    int tcount;
    boolean other_done = false;
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
        tcount = 0;
        for (Goal b : children) {
            if (b instanceof ExitGoal && b.evaluate()) {
                // if other goal isnt done, exit goal shouldnt work (return false)
                if (!other_done) {
                    return false;
                }
                // if other is done then add to count
                tcount++;
            }
            // if the non exit goal is done
            if (!(b instanceof ExitGoal) && b.evaluate()) {
                tcount++;
                other_done = true;
            }
        }
        if (tcount == 2) {
            return true;
        } else {
            return false;
        }
    }
}
