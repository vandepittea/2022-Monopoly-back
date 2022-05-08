package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Property extends Tile {
    private final int cost;
    private final int mortgage;
    private final int groupSize;
    private final String color;

    private boolean mortgaged;
    private int houseCount;
    private int hotelCount;

    public Property(int position, String name, int cost, int mortgage, int groupSize, String color, TileType type) {
        super(position, name, type);
        this.cost = cost;
        this.mortgage = mortgage;
        this.groupSize = groupSize;
        this.color = color;
        this.mortgaged = false;
        this.houseCount = 0;
        this.hotelCount = 0;
    }

    public int getCost() {
        return cost;
    }
    public int getMortgage() {
        return mortgage;
    }
    public int getGroupSize() {
        return groupSize;
    }
    public String getColor() {
        return color;
    }
    @JsonIgnore
    public boolean isMortgaged() {
        return mortgaged;
    }
    @JsonIgnore
    public int getHouseCount() {
        return houseCount;
    }
    @JsonIgnore
    public int getHotelCount() {
        return hotelCount;
    }

    public void takeMortgage(){
        mortgaged = true;
    }

    public void payMortgage(){
        mortgaged = false;
    }
}
