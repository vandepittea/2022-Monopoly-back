package be.howest.ti.monopoly.logic.implementation.tile;

public class Railroad extends Property {
    private final int rent;

    public Railroad(int position, String name, int cost, int mortgage, int groupSize, String color, int rent) {
        super(position, name, cost, mortgage, groupSize, color, TileType.railroad);
        this.rent = rent;
    }

    public int getRent() {
        return rent;
    }
}
