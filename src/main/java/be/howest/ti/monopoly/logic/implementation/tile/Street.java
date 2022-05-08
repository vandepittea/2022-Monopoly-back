package be.howest.ti.monopoly.logic.implementation.tile;

public class Street extends Property {
    private Integer[] rentOfHouses;
    private final int housePrice;
    private final String streetColor;
    private final int rent;

    private int amountOfHouses;

    public Street(int position, String name, int cost, int mortgage, int groupSize, String color,
                  Integer[] rentOfHouses, int housePrice, String streetColor, int rent) {
        super(position, name, cost, mortgage, groupSize, color, TileType.street);
        this.housePrice = housePrice;
        this.streetColor = streetColor;
        this.rentOfHouses = rentOfHouses;
        this.rent = rent;
    }
    public Integer[] getRentOfHouses() {
        return rentOfHouses;
    }
    public int getHousePrice() {
        return housePrice;
    }
    public String getStreetColor() {
        return streetColor;
    }
    public int getRent() {
        return rent;
    }

    public void addHouse(){
        amountOfHouses++;
    }

    public int calculateRent(){
        return rentOfHouses[amountOfHouses - 1];
    }
}
