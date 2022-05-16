package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.implementation.enums.TileType;
import be.howest.ti.monopoly.logic.implementation.tile.*;
import be.howest.ti.monopoly.logic.implementation.turn.Move;
import be.howest.ti.monopoly.web.views.PropertyView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import be.howest.ti.monopoly.logic.implementation.tile.Property;
import be.howest.ti.monopoly.logic.implementation.tile.Street;

import java.util.*;

public class Player {
    public static final int MAX_HOUSE_COUNT = 4;
    public static final int MAX_HOTEL_COUNT = 1;
    private final String name;

    private Tile currentTile;
    private boolean jailed;
    private int money;
    private boolean bankrupt;
    private int getOutOfJailCards;
    private String taxSystem;

    private Map<Property, PropertyView> properties;
    private int debt;
    private Player creditor;

    public Player(String name, Tile startingTile) {
        this.name = name;
        this.currentTile = startingTile;
        this.jailed = false;
        this.money = 1500;
        this.bankrupt = false;
        this.getOutOfJailCards = 0;
        this.taxSystem = "ESTIMATE";
        properties = new HashMap<>();
        this.debt = 0;
        this.creditor = null;
    }

    public String getName() {
        return name;
    }

    public String getCurrentTile() {
        return currentTile.getName();
    }

    @JsonIgnore
    public Tile getCurrentTileObject() {
        return currentTile;
    }

    public boolean isJailed() {
        return jailed;
    }

    public int getMoney() {
        return money;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    public int getGetOutOfJailCards() {
        return getOutOfJailCards;
    }

    public String getTaxSystem() {
        return taxSystem;
    }

    public Set<PropertyView> getProperties() {
        return new HashSet<>(properties.values());
    }

    public PropertyView getPropertyView(Property property) {
        return properties.get(property);
    }

    public int getDebt() {
        return debt;
    }

    @JsonIgnore
    public Player getCreditor() {
        return creditor;
    }

    public void becomeBankrupt() {
        this.bankrupt = true;
    }

    public void goToJail(Tile jailTile) {
        this.jailed = true;
        moveTo(jailTile);
    }

    public void getOutOfJail() {
        this.jailed = false;
    }

    public void receiveGetOutOfJailCard() {
        getOutOfJailCards++;
    }

    public void buyProperty(Property pr) {
        boolean successfulPayment = payMoney(pr.getCost());

        if (successfulPayment) {
            addProperty(pr);
        } else {
            throw new IllegalMonopolyActionException("You don't have enough money to buy this property");
        }
    }

    private void addProperty(Property property) {
        properties.put(property, new PropertyView(property));
    }

    private boolean payMoney(int amount) {
        if (money > amount) {
            money -= amount;
            return true;
        } else {
            return false;
        }
    }

    public void payDebt(int amount, Player debtor){
        boolean successfulPayment = payMoney(amount);
        if (!successfulPayment) {
            debt += amount;
            creditor = debtor;
            throw new IllegalMonopolyActionException("You do not have enough money. You will have to sell properties " +
                    "so that you can pay off your debt. You have time until it is your turn again.");
        }
    }

    public void receiveMoney(int amount) {
        money += amount;
    }

    public void moveTo(Tile newTile) {
        currentTile = newTile;
    }

    public void turnOverAssetsTo(Player p) {
        for (Property property : properties.keySet()) {
            p.addProperty(property);
        }
        p.receiveMoney(money);

        money = 0;
        debt = 0;
        properties.clear();
    }

    public void turnOverAssetsToBank() {
        //TODO: start auction

        money = 0;
        debt = 0;
        properties.clear();
    }

    public void collectDebt(Property pr, Player pl, Game g) {
        if (!checkForOwnership(pr)) {
            throw new IllegalMonopolyActionException("This property is not owned by you.");
        }
        if (!checkIfDebtorIsOnYourProperty(pr, pl)) {
            throw new IllegalMonopolyActionException("The specified player is not on this property.");
        }
        if (checkForNextRollDice(g, pr)) {
            throw new IllegalMonopolyActionException("You're too late. The next dice roll is already over.");
        }

        int rent = calculateRent(pr, pl, g);
        pl.payDebt(rent, this);
        receiveMoney(rent);
    }

    private int calculateRent(Property pr, Player pl, Game g) {
        return pr.calculateRent(this, g);
    }

    private boolean checkForOwnership(Property p) {
        return properties.containsKey(p);
    }

    private boolean checkIfDebtorIsOnYourProperty(Property pr, Player pl) {
        return pr.getName().equals(pl.currentTile.getName());
    }

    private boolean checkForNextRollDice(Game g, Property p){
        Move move = g.getTurns().get(g.getTurns().size() - 1).getMoves().get(0);
        String descriptionLastRoll = move.getDescription();
        String propertyTitle = move.getTitle();
        boolean checkDescription = !descriptionLastRoll.equals("should pay rent");
        boolean checkTitle = !propertyTitle.equals(p.getName());
        return checkDescription && checkTitle;
    }

    public int buyHouseOrHotel(MonopolyService service, Game game, Street street) {
        if (!checkOwnershipWholeStreet(street, service)) {
            throw new IllegalMonopolyActionException("You can only build on a property when you own the whole group.");
        }
        if (!street.checkStreetHouseDifference(service, this, true)) {
            throw new IllegalMonopolyActionException("The difference between the houses in a street should " +
                    "not be higher than one.");
        } else {
            if (properties.get(street).getHouseCount() < MAX_HOUSE_COUNT) {
                return buyHouse(game, street);
            } else {
                return buyHotel(game, street);
            }
        }
    }

    public int sellHouseOrHotel(MonopolyService service, Game game, Street street) {
        if (!street.checkStreetHouseDifference(service, this, false)) {
            throw new IllegalMonopolyActionException("The difference between the houses in a street should " +
                    "not be higher than one.");
        }

        if (properties.get(street).getHotelCount() != MAX_HOTEL_COUNT) {
            return sellHouse(game, street);
        } else {
            return sellHotel(game, street);
        }
    }

    private int buyHouse(Game game, Street street) {
        if (game.getAvailableHouses() < 1) {
            throw new IllegalMonopolyActionException("The limit for the maximum number of houses has been reached. " +
                    "No more houses can be built.");
        }
        if (properties.get(street).getHotelCount() == MAX_HOTEL_COUNT) {
            throw new IllegalMonopolyActionException("You can only have 1 hotel on a street!");
        }

        boolean successfulPayment = payMoney(street.getHousePrice());
        if (!successfulPayment) {
            throw new IllegalMonopolyActionException("You don't have enough money to buy a house for this property.");
        }

        properties.get(street).buyHouse();
        game.setAvailableHouses(game.getAvailableHouses() - 1);

        return properties.get(street).getHouseCount();
    }

    private int sellHouse(Game game, Street street) {
        if (properties.get(street).getHouseCount() < 1) {
            throw new IllegalMonopolyActionException("There are no houses on this property.");
        }

        properties.get(street).sellHouse();
        game.setAvailableHouses(game.getAvailableHouses() + 1);
        receiveMoney(street.getHousePrice() / 2);

        return properties.get(street).getHouseCount();
    }

    private int buyHotel(Game game, Street street) {
        if (game.getAvailableHotels() < 1) {
            throw new IllegalMonopolyActionException("The limit for the maximum number of hotels has been reached. " +
                    "No more hotels can be built.");
        }

        boolean successfulPayment = payMoney(street.getHousePrice());
        if (!successfulPayment) {
            throw new IllegalMonopolyActionException("You don't have enough money to buy a hotel for this property.");
        }

        properties.get(street).buyHotel();
        game.setAvailableHouses(game.getAvailableHouses() + Street.NUMBER_OF_HOUSES_IN_ONE_HOTEL);
        game.setAvailableHotels(game.getAvailableHotels() - 1);

        return properties.get(street).getHotelCount();
    }

    private int sellHotel(Game game, Street street) {
        if (game.getAvailableHouses() < Street.NUMBER_OF_HOUSES_IN_ONE_HOTEL) {
            throw new IllegalMonopolyActionException("You can't sell your hotel since there are no houses left in the bank.");
        }

        properties.get(street).sellHotel();
        game.setAvailableHouses(game.getAvailableHouses() - Street.NUMBER_OF_HOUSES_IN_ONE_HOTEL);
        game.setAvailableHotels(game.getAvailableHotels() + 1);
        receiveMoney(street.getHousePrice() / 2);

        return properties.get(street).getHotelCount();
    }

    private boolean checkOwnershipWholeStreet(Street streetToBuildHouseOn, MonopolyService service){
        for(Tile t: service.getTiles()){
            if(t.getType() == TileType.STREET){
                Street streetOfGroup = (Street) t;
                if (streetOfGroup.getStreetColor().equals(streetToBuildHouseOn.getStreetColor())) {
                    if (!checkForOwnership(streetOfGroup)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void getOutOfJailFree() {
        if(getOutOfJailCards < 1){
            throw  new IllegalMonopolyActionException("You don't have get out of jail cards. You're still in jail.");
        }
        if(!jailed){
            throw new IllegalMonopolyActionException("You're not in jail. You can't use this endpoint.");
        }
        getOutOfJailCards--;
        jailed = false;
    }

    public void getOutOfJailFine() {
        if(!jailed){
            throw new IllegalMonopolyActionException("You're not in jail. You can't use this endpoint.");
        }
        boolean successfulPayment = payMoney(50);
        if (!successfulPayment){
            throw new IllegalMonopolyActionException("You don't have enough money to go out of jail.");
        }
        else {
            jailed = false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
