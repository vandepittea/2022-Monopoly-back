package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.enums.TileType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public abstract class Tile {
    protected final TileType type;
    private final int position;
    private final String name;
    private final String nameAsPathParameter;

    public Tile(int position, String name, TileType type) {
        this.type = type;
        this.position = position;
        this.name = name;
        this.nameAsPathParameter = Tile.decideNameAsPathParameter(name);
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
    public TileType getType() {
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
