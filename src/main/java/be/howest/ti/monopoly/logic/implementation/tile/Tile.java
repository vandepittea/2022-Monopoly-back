package be.howest.ti.monopoly.logic.implementation.tile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public abstract class Tile {
    protected final TileType type;
    private final String name;
    private final int position;
    private final String nameAsPathParameter;

    public Tile(int position, String name, TileType type) {
        this.position = position;
        this.name = name;
        this.nameAsPathParameter = Tile.decideNameAsPathParameter(name);
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public int getPosition() {
        return position;
    }
    public String getNameAsPathParameter() {
        return nameAsPathParameter;
    }
    public String getType() {
        return this.type.toString().replace("_", " ");
    }

    @JsonIgnore
    public TileType getActualType() {
        return type;
    }

    public static String decideNameAsPathParameter(String tileName){
        return tileName.replace(" ", "_");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return position == tile.position && name.equals(tile.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, position);
    }
}
