package be.howest.ti.monopoly.logic.implementation.tile;

public class Street extends Property {
    private int amountOfHouses;

    private final String type;
    private Integer[] rentOfHouses;
    private final int housePrice;
    private final String streetColor;
    private final int rent;

    public Street(int position, String name, int cost, int mortgage, int groupSize, String color,
                  String type, Integer[] rentOfHouses, int housePrice, String streetColor, int rent) {
        super(position, name, cost, mortgage, groupSize, color);
        this.type = type;
        this.housePrice = housePrice;
        this.streetColor = streetColor;
        this.rentOfHouses = rentOfHouses;
        this.rent = rent;
    }

    public void addHouse(){
        amountOfHouses++;
    }

    public int calculateRent(){
        return rentOfHouses[amountOfHouses - 1];
    }
}
