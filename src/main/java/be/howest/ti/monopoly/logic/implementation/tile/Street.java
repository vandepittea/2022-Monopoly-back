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
        super(position, name, cost, mortgage, groupSize, color, TileType.STREET);
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

    public void sellHotel(Game game) {
        game.sellHotel(this);
    }

    public boolean checkStreetHouseDifference(MonopolyService service, Game g, boolean buy){
        int decideBuyOrSell = 1;
        if(!buy){
            decideBuyOrSell *= -1;
        }

        for(Tile t: service.getTiles()){
            if(t.getType() == TileType.STREET){
                Street s = (Street) t;
                if(s.getStreetColor().equals(this.getStreetColor())){
                    int housesStreet1 = g.receiveHouseCount(s) + (g.receiveHotelCount(s)*4);
                    int housesStreet2 = g.receiveHouseCount(this) + (g.receiveHotelCount(this)*4);
                    int difference = Math.abs(housesStreet1 - (housesStreet2 + decideBuyOrSell));
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
