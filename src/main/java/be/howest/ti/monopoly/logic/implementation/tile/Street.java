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

    public void buyHouse(Game game){
        game.buyHouse(this);
    }

    public void sellHouse(Game game){
        game.sellHouse(this);
    }
    public void buyHotel(Game game) {
        game.buyHotel(this);
    }

    public boolean checkStreetHouseDifference(MonopolyService service, Game g){
        for(Tile t: service.getTiles()){
            if(t.getType() == TileType.street){
                Street s = (Street) t;
                if(s.getStreetColor().equals(this.getStreetColor())){
                    int difference = Math.abs(g.receiveHouseCount(s) - (g.receiveHouseCount(this) + 1));
                    if(difference > 1){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public int calculateRent(Player p, Game g){

        if(g.receiveHouseCount(this) > 0){
            return rentOfHouses[g.receiveHouseCount(this) - 1];
        }
        else if(g.receiveHotelCount(this) > 0){
            return rentOfHouses[rentOfHouses.length - 1];
        }
        else{
            return rent;
        }
    }
}
