package be.howest.ti.monopoly.logic.implementation.tile;

public class Railroad extends Property {
    private String type;
    private int rent;

    public Railroad(int position, String name, int cost, int mortgage, int groupSize, String color,
                String type, int rent) {
        super(position, name, cost, mortgage, groupSize, color);
        this.type = type;
        this.rent = rent;
    }
}
