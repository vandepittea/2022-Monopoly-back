package be.howest.ti.monopoly.logic.implementation.turn;

import be.howest.ti.monopoly.logic.implementation.tile.Tile;
import com.fasterxml.jackson.annotation.JsonIdentityReference;

public class Move {
    private final Tile tile;
    private final String description;

    public Move(Tile tile, String description) {
        this.tile = tile;
        this.description = description;
    }

    @JsonIdentityReference(alwaysAsId = true)
    public Tile getTile() {
        return tile;
    }

    public String getDescription() {
        return description;
    }
}
