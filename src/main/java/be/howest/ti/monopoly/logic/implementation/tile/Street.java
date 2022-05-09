package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.MonopolyService;
import be.howest.ti.monopoly.logic.implementation.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;

import be.howest.ti.monopoly.logic.implementation.Player;

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

    public void buyHouse(){
        houseCount++;
    }
    public void buyHotel() {
        houseCount = 0;
        hotelCount = 1;
    }

    public boolean checkStreetHouseDifference(MonopolyService service){
        for(Tile t: service.getTiles()){
            Street s = (Street) t;
            if(s.getStreetColor().equals(this.getStreetColor())){
                int difference = Math.abs(s.getHouseCount() - this.houseCount);
                if(difference > 1){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int calculateRent(Player p, Game g){

        if(houseCount > 0){
            return rentOfHouses[houseCount - 1];
        }
        else if(hotelCount > 0){
            return rentOfHouses[rentOfHouses.length - 1];
        }
        else{
            return rent;
        }
    }
}
