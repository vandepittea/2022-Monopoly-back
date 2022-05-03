package be.howest.ti.monopoly.logic.implementation.tile;

public class Property extends Tile {
    private boolean mortgaged;

    private int cost;
    private int mortgage;
    private int groupSize;
    private String color;

    public Property(int position, String name, int cost, int mortgage, int groupSize, String color) {
        super(position, name);
        this.cost = cost;
        this.mortgage = mortgage;
        this.groupSize = groupSize;
        this.color = color;
    }

    public void takeMortgage(){
        mortgaged = true;
    }

    public void payMortgage(){
        mortgaged = false;
    }
}
