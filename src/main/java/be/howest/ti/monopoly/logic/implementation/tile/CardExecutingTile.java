package be.howest.ti.monopoly.logic.implementation.tile;

public class CardExecutingTile extends Tile {
    private String type;

    public CardExecutingTile(int position, String name, String type) {
        super(position, name);
        this.type = type;
    }
}
