package be.howest.ti.monopoly.logic.implementation.tile;

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
}
