package be.howest.ti.monopoly.logic.implementation.tile;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Property extends Tile {
    private final int cost;
    private final int mortgage;
    private final int groupSize;
    private final String color;

    private boolean mortgaged;

    public Property(int position, String name, int cost, int mortgage, int groupSize, String color, TileType type) {
        super(position, name, type);
        this.cost = cost;
        this.mortgage = mortgage;
        this.groupSize = groupSize;
        this.color = color;
        this.mortgaged = false;
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

    public void takeMortgage(){
        mortgaged = true;
    }

    public void payMortgage(){
        mortgaged = false;
    }
}
