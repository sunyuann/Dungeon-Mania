package dungeonmania.main.collect_StrPattern;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.Item;

// This interface instigates a strategy pattern for the collect property of entities
public interface CollectBehaviour {

    public void collect(Dungeon dungeon, Entity entity, Item item);
    
}
