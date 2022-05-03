package be.howest.ti.monopoly.logic.implementation;

public class Tile {
    private final String name;
    private final int position;
    private final String nameAsPathParameter;

    public Tile(int position, String name) {
        this.position = position;
        this.name = name;
        this.nameAsPathParameter = decideNameAsPathParameter();
    }

    public String decideNameAsPathParameter(){
        return name.replaceAll(" ", "_");
    }
}
