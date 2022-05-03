package be.howest.ti.monopoly.logic.implementation;

public class Tile {
    private final int position;
    private final String name;

    public Tile(int number, String name) {
        this.position = number;
        this.name = name;
    }

    public int getPosition() {
        return position;
    }
    public String getName() {
        return name;
    }
}
