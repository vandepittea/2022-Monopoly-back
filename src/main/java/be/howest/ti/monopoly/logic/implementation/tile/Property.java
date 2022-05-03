package be.howest.ti.monopoly.logic.implementation.tile;

public class Property extends Tile {
    private final int cost;
    private final int mortgage;
    private final int groupSize;
    private final String color;

    private boolean mortgaged;

    public Property(int position, String name, int cost, int mortgage, int groupSize, String color) {
        super(position, name);
        this.cost = cost;
        this.mortgage = mortgage;
        this.groupSize = groupSize;
        this.color = color;
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

    public void takeMortgage(){
        mortgaged = true;
    }

    public void payMortgage(){
        mortgaged = false;
    }
}
