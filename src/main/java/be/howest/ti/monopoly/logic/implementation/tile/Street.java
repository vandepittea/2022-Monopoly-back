package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.Player;

public class Street extends Property {
    private final String type;
    private Integer[] rentOfHouses;
    private final int housePrice;
    private final String streetColor;
    private final int rent;
    private int amountOfHouses;

    public Street(int position, String name, int cost, int mortgage, int groupSize, String color,
                  Integer[] rentOfHouses, int housePrice, String streetColor, int rent) {
        super(position, name, cost, mortgage, groupSize, color);
        this.type = "street";
        this.housePrice = housePrice;
        this.streetColor = streetColor;
        this.rentOfHouses = rentOfHouses;
        this.rent = rent;
    }

    public String getType() {
        return type;
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

    @Override
    public int getHouseCount() {
        return amountOfHouses;
    }

    public void addHouse(){
        amountOfHouses++;
    }

    public int calculateRent(){
        return rentOfHouses[amountOfHouses - 1];
    }

    public void buyHouse(Player player) {
        if (player.getMoney() >= getHousePrice()) {
            player.setMoney(player.getMoney() - getHousePrice());
            addHouse();
        } //else exception "not enough money"
    }
}
