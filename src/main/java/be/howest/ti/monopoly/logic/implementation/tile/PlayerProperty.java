package be.howest.ti.monopoly.logic.implementation.tile;

public class PlayerProperty {
    private String property;
    private boolean mortgage;
    private int houseCount;
    private int hotelCount;

    public PlayerProperty(String property){
        this.property = property;
        this.mortgage = false;
        this.houseCount = 0;
        this.hotelCount = 0;
    }

    public void takeMortgage(){
        mortgage= true;
    }

    public void payMortgage(){
        mortgage = false;
    }
}
