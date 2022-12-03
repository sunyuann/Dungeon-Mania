package dungeonmania.main.Goals;

// This interface instigates the Goal Composite Pattern
public interface Goal {
    
    public void add(Goal goal);

    public boolean evaluate();
}
