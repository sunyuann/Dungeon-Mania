package dungeonmania.main.collect_StrPattern;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.Item;

public class NoCollect implements CollectBehaviour {

    // does nothing
    public void collect(Dungeon dungeon, Entity entity, Item item) {}
    
}
