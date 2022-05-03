package be.howest.ti.monopoly.logic.implementation.tile;

public class SimpleTile extends Tile {
    private final String type;

    public SimpleTile(int position, String name, String type) {
        super(position, name);
        this.type = type;
    }
}
