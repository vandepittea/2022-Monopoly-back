package be.howest.ti.monopoly.logic.implementation.tile;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Street extends Property {
    private Integer[] rentOfHouses;
    private final int housePrice;
    private final String streetColor;
    private final int rent;

    private int houseCount;
    private int hotelCount;

    public Street(int position, String name, int cost, int mortgage, int groupSize, String color,
                  Integer[] rentOfHouses, int housePrice, String streetColor, int rent) {
        super(position, name, cost, mortgage, groupSize, color, TileType.street);
        this.housePrice = housePrice;
        this.streetColor = streetColor;
        this.rentOfHouses = rentOfHouses;
        this.rent = rent;
        this.houseCount = 0;
        this.hotelCount = 0;
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
    @JsonIgnore
    public int getHouseCount() {
        return houseCount;
    }
    @JsonIgnore
    public int getHotelCount() {
        return hotelCount;
    }

    public int calculateRent(){

    }
}
