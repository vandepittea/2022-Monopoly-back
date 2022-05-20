package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.implementation.enums.Taxsystem;
import be.howest.ti.monopoly.logic.implementation.enums.TileType;
import be.howest.ti.monopoly.logic.implementation.tile.*;
import be.howest.ti.monopoly.logic.implementation.turn.Move;
import be.howest.ti.monopoly.logic.implementation.tile.OwnedProperty;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import be.howest.ti.monopoly.logic.implementation.tile.Property;
import be.howest.ti.monopoly.logic.implementation.tile.Street;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Player {
    public static final int MAX_HOUSE_COUNT = 4;
    public static final int MAX_HOTEL_COUNT = 1;

    private final String name;

    private boolean jailed;
    private boolean bankrupt;
    private int debt;
    private int money;
    private int getOutOfJailFreeCards;

    private Taxsystem taxSystem;

    private Tile currentTile;
    private Player creditor;
    private final Map<Property, OwnedProperty> properties;

    public Player(String name, Tile startingTile) {
        this.jailed = false;
        this.bankrupt = false;

        this.debt = 0;
        this.money = 1500;
        this.getOutOfJailFreeCards = 0;

        this.taxSystem = Taxsystem.ESTIMATE;

        this.name = name;
        this.currentTile = startingTile;
        this.creditor = null;

        properties = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    @JsonIdentityReference(alwaysAsId = true)
    public Tile getCurrentTile() {
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

    public int getGetOutOfJailFreeCards() {
        return getOutOfJailFreeCards;
    }

    public Taxsystem getTaxSystem() {
        return taxSystem;
    }

    public Set<OwnedProperty> getProperties() {
        return new HashSet<>(properties.values());
    }

    public OwnedProperty getPropertyView(Property property) {
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
        if (creditor != null) {
            turnOverAssetsTo(creditor);
        } else {
            turnOverAssetsToBank();
        }

        this.bankrupt = true;
    }

    public void goToJail(Tile jailTile) {
        this.jailed = true;
        moveTo(jailTile);
    }

    public void getOutOfJail() {
        this.jailed = false;
    }

    public void receiveGetOutOfJailFreeCard() {
        getOutOfJailFreeCards++;
    }

    public void buyProperty(Property property) {
        boolean successfulPayment = payMoney(property.getCost());

        if (!successfulPayment) {
            throw new IllegalMonopolyActionException(name + " doesn't have enough money to buy this property");
        }

        addProperty(property);
    }

    private void addProperty(Property property) {
        properties.put(property, new OwnedProperty(property));
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
            throw new IllegalMonopolyActionException("You don't have enough money, earn money before your next turn" +
                    "or be bankrupt");
        }

        if (debtor != null) {
            debtor.receiveMoney(amount);
        }
    }

    public void receiveMoney(int amount) {
        money += amount;
    }

    public void moveTo(Tile newTile) {
        currentTile = newTile;
    }

    public void payTaxes() {
        if (taxSystem == Taxsystem.ESTIMATE) {
            payDebt(200, null);
        } else {
            int assetWorth = calculateAssetWorth();
            payDebt(assetWorth, null);
        }
    }

    private int calculateAssetWorth() {
        int assetWorth = money;

        for (Map.Entry<Property, OwnedProperty> propertyPair : properties.entrySet()) {
            Property property = propertyPair.getKey();
            OwnedProperty view = propertyPair.getValue();

            assetWorth += property.getCost();

            if (property.getType() == TileType.STREET) {
                Street street = (Street) property;
                assetWorth += view.getHouseCount() * street.getHousePrice();
                assetWorth += view.getHotelCount() * street.getHousePrice();
            }
        }

        assetWorth /= 10;
        return assetWorth;
    }

    public void turnOverAssetsTo(Player player) {
        for (Property property : properties.keySet()) {
            player.addProperty(property);
        }
        player.receiveMoney(money);

        clearAssets();
    }

    public void turnOverAssetsToBank() {
        //TODO: start auction

        clearAssets();
    }

    private void clearAssets() {
        money = 0;
        debt = 0;
        properties.clear();
    }

    public void collectDebt(Property property, Player player, Game game) {
        if (!checkForOwnership(property)) {
            throw new IllegalMonopolyActionException(property.getName() + " is not owned by you.");
        }
        if (!checkIfDebtorIsOnYourProperty(property, player)) {
            throw new IllegalMonopolyActionException(player.getName() + " is not on this property.");
        }
        if (hasPlayerAlreadyRolled(game, property)) {
            throw new IllegalMonopolyActionException("You're too late. The next dice roll is already happened.");
        }

        int rent = property.calculateRent(this, game);
        player.payDebt(rent, this);
    }

    private boolean checkForOwnership(Property property) {
        return properties.containsKey(property);
    }

    private boolean checkIfDebtorIsOnYourProperty(Property property, Player player) {
        return property.getName().equals(player.currentTile.getName());
    }

    private boolean hasPlayerAlreadyRolled(Game game, Property property){
        Move move = game.getTurns().get(game.getTurns().size() - 1).getMoves().get(0);
        String descriptionLastRoll = move.getDescription();
        Tile propertyTitle = move.getTile();

        boolean checkDescription = !descriptionLastRoll.equals("should pay rent");
        boolean checkTitle = !propertyTitle.equals(property);

        return checkDescription && checkTitle;
    }

    public int buyHouseOrHotel(MonopolyService service, Game game, Street street) {
        if (!checkOwnershipWholeStreet(street, service)) {
            throw new IllegalMonopolyActionException("You can only build on a property when you own the whole group.");
        }
        if (!street.checkStreetHouseDifference(service, this, true)) {
            throw new IllegalMonopolyActionException("You have to build equally among streets of the same color");
        }

        if (properties.get(street).getHouseCount() < MAX_HOUSE_COUNT) {
            return buyHouse(game, street);
        } else {
            return buyHotel(game, street);
        }
    }

    public int sellHouseOrHotel(MonopolyService service, Game game, Street street) {
        if (!street.checkStreetHouseDifference(service, this, false)) {
            throw new IllegalMonopolyActionException("You have to sell equally among streets of the same color");
        }

        if (properties.get(street).getHotelCount() != MAX_HOTEL_COUNT) {
            return sellHouse(game, street);
        } else {
            return sellHotel(game, street);
        }
    }

    private int buyHouse(Game game, Street street) {
        if (game.getAvailableHouses() < 1) {
            throw new IllegalMonopolyActionException("There are no more available houses, wait until some are free");
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
            throw new IllegalMonopolyActionException("There are no more available hotels, wait until there are more.");
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
                if (streetOfGroup.getStreetColor().equals(streetToBuildHouseOn.getStreetColor()) &&
                        !checkForOwnership(streetOfGroup)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void getOutOfJailFree() {
        if(getOutOfJailFreeCards < 1){
            throw  new IllegalMonopolyActionException("You don't have get out of jail cards. You're still in jail.");
        }
        if(!jailed){
            throw new IllegalMonopolyActionException("You're not in jail. You can't use this endpoint.");
        }

        getOutOfJailFreeCards--;
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

        jailed = false;
    }

    public void useComputeTax() {
        taxSystem = Taxsystem.COMPUTE;
    }

    public void useEstimateTax() {
        taxSystem = Taxsystem.ESTIMATE;
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
