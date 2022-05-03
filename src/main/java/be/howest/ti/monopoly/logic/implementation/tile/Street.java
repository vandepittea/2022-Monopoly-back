package be.howest.ti.monopoly.logic.implementation.tile;

public class Street extends Property {
    private String type;
    private Integer[] rentOfHouses;
    private int housePrice;
    private String streetColor;
    private int rent;

    public Street(int position, String name, int cost, int mortgage, int groupSize, String color,
                  String type, int housePrice, String streetColor, Integer[] rentOfHouses) {
        super(position, name, cost, mortgage, groupSize, color);
        this.type = type;
        this.housePrice = housePrice;
        this.streetColor = streetColor;
        this.rentOfHouses = rentOfHouses;
        this.rent = rent;
    }
}
