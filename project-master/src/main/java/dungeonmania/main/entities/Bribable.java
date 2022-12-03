package dungeonmania.main.entities;

public interface Bribable {

    public boolean inBribingRadius();

    public boolean canBribe();

    public void bribe();

    public boolean canMindControl();

    public void mindControl();
}
