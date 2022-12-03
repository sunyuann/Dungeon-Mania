package dungeonmania.main.movement_StrPattern;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.entities.statics.SwampTile;
import dungeonmania.util.Position;

public class FollowMovement implements MoveBehaviour {

    private double cost(Dungeon dungeon, Position u, Position v) {
        Map<Position, List<Entity>> grid = dungeon.getEntitiesLocation();
        if (grid.get(v).isEmpty()) {
            return 1.0;
        } else {
            for (Entity e: grid.get(v)) {
                if (!e.isPassable()) {
                    return Double.POSITIVE_INFINITY;
                } else if (e instanceof SwampTile) {
                    SwampTile st = (SwampTile) e;
                    return 1.0 + st.getDelay();
                }
            }
        }
        return 0;
    }

    private Map<Position, Position> Dijkstras(Dungeon dungeon, Entity entity) {
        Map<Position, List<Entity>> grid = dungeon.getEntitiesLocation();
        Map<Position, Double> dist = new HashMap<Position, Double>();
        Map<Position, Position> prev = new HashMap<Position, Position>();

        for (Position position: grid.keySet()) {
            dist.put(position, Double.POSITIVE_INFINITY);
            prev.put(position, null);
        }
        dist.put(entity.getPosition(), 0.0);

        Queue<Position> q = new LinkedList<Position>();
        q.add(entity.getPosition());
        while (!q.isEmpty()) {
            Position u = q.poll();
            for (Position v: u.getCardinallyAdjacentPositions()) {
                if (dist.containsKey(v) && prev.containsKey(v) && prev.get(v) == null) {
                    if (dist.get(u) + cost(dungeon, u, v) < dist.get(v)) {
                        dist.put(v, dist.get(u) + cost(dungeon, u, v));
                        prev.put(v, u);
                        q.add(v);
                    }
                }
            }
        }
        return prev;
    }
    
    public void move(Dungeon dungeon, Entity entity) {
        Map<Position, Position> dijkstras = Dijkstras(dungeon, entity);
        Position curr = dungeon.getPlayer().getPosition();
        while (dijkstras.get(curr) != null && !dijkstras.get(curr).equals(entity.getPosition())) {
            curr = dijkstras.get(curr);
        }

        if (dijkstras.get(curr) != null) {
            entity.setPosition(curr);
        }
    }
}
