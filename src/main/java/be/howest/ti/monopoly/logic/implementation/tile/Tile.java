package be.howest.ti.monopoly.logic.implementation.tile;

import java.util.Objects;

public class Tile {
    private final String name;
    private final int position;
    private final String nameAsPathParameter;

    public Tile(int position, String name) {
        this.position = position;
        this.name = name;
        this.nameAsPathParameter = decideNameAsPathParameter();
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

    public String decideNameAsPathParameter(){
        return name.replaceAll(" ", "_");
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
