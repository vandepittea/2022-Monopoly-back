package be.howest.ti.monopoly.logic.implementation.tile;

public class Utility extends Property {
    private final String type;
    private final String rent;

    public Utility(int position, String name, int cost, int mortgage, int groupSize, String color, String rent) {
        super(position, name, cost, mortgage, groupSize, color);
        this.type = "utility";
        this.rent = rent;
    }

    public String getType() {
        return type;
    }
    public String getRent() {
        return rent;
    }
}
