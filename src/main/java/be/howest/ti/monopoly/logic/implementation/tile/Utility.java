package be.howest.ti.monopoly.logic.implementation.tile;

public class Utility extends Property {
    private final String type;
    private final String rent;

    public Utility(int position, String name, int cost, int mortgage, int groupSize, String color,
                   String type, String rent) {
        super(position, name, cost, mortgage, groupSize, color);
        this.type = type;
        this.rent = rent;
    }
}
