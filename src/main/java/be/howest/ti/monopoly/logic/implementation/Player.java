package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.implementation.tile.*;
import be.howest.ti.monopoly.logic.implementation.turn.Move;
import com.fasterxml.jackson.annotation.JsonIgnore;
import be.howest.ti.monopoly.logic.implementation.tile.Property;
import be.howest.ti.monopoly.logic.implementation.tile.Street;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Player {
    private final String name;

    private Tile currentTile;
    private boolean jailed;
    private int money;
    private boolean bankrupt;
    private int getOutOfJailCards;
    private String taxSystem;
    private Set<Property> properties;
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
        properties = new HashSet<>();
        this.debt = 0;
        this.creditor = null;
    }

    public String getName() {
        return name;
    }

    public String getCurrentTile() {
        return currentTile.getName();
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

    public Set<Property> getProperties() {
        return properties;
    }

    public int getDebt() {
        return debt;
    }

    @JsonIgnore
    public Player getCreditor() {
        return creditor;
    }

    public void becomeBankrupt(){
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

    private void addProperty(Property p) {
        properties.add(p);
    }

    private boolean payMoney(int amount) {
        if (money > amount) {
            money -= amount;
            return true;
        } else {
            return false;
        }
    }

    private void payDebt(int amount, Player debtor){
        boolean successfulPayment = payMoney(amount);
        if(!successfulPayment){
            debt += amount;
            creditor = debtor;
            throw new IllegalMonopolyActionException("You do not have enough money. You will have to sell properties " +
                    "so that you can pay off your debt. You have time until it is your turn again.");
        }
    }

    private void receiveMoney(int amount){
        money += amount;
    }

    public void moveTo(Tile newTile) {
        currentTile = newTile;
    }

    public void turnOverAssetsTo(Player p){
        for(Property pr: properties){
            p.addProperty(pr);
        }
        p.receiveMoney(money);

        money = 0;
        debt = 0;
        properties.clear();
    }

    public void turnOverAssetsToBank(){
        //TODO: start auction

        money = 0;
        debt = 0;
        properties.clear();
    }

    public void collectDebt(Property pr, Player pl, Game g){
        if(!checkForOwnership(pr)){
            throw new IllegalMonopolyActionException("This property is not owned by you.");
        }
        else if(!checkIfDebtorIsOnYourProperty(pr, pl)){
            throw new IllegalMonopolyActionException("The specified player is not on this property.");
        }
        else if(checkForNextRollDice(g, pr)){
            throw new IllegalMonopolyActionException("You're too late. The next dice roll is already over.");
        }
        else{
            int rent = calculateRent(pr, pl, g);
            pl.payDebt(rent, this);
            receiveMoney(rent);
        }
    }

    private int calculateRent(Property pr, Player pl, Game g){
        return pr.calculateRent(pl, g);
    }

    private boolean checkForOwnership(Property p){
        return properties.contains(p);
    }

    private boolean checkIfDebtorIsOnYourProperty(Property pr, Player pl){
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

    public int buyHouseOrHotel(MonopolyService service, Game g, Street s){
        if(!checkOwnershipWholeStreet(s, service)){
            throw new IllegalMonopolyActionException("You can only build on a property when you own the whole group.");
        }
        if(!s.checkStreetHouseDifference(service, g)){
            throw new IllegalMonopolyActionException("The difference between the houses in a street should " +
                    "not be higher than one.");
        }
        else{
            if(g.receiveHouseCount(s) < 4){
                return buyHouse(g, s);
            }
            else{
                return buyHotel(g, s);
            }
        }
    }

    private int buyHouse(Game g, Street s){
        if(g.getAvailableHouses() < 1){
            throw new IllegalMonopolyActionException("The limit for the maximum number of houses has been reached. " +
                    "No more houses can be built.");
        }

        boolean successfulPayment = payMoney(s.getHousePrice());

        if (successfulPayment) {
            s.buyHouse(g);
        } else {
            throw new IllegalMonopolyActionException("You don't have enough money to buy a house for this property.");
        }

        return g.receiveHouseCount(s);
    }

    private int buyHotel(Game g, Street s){
        if(g.getAvailableHotels() < 1){
            throw new IllegalMonopolyActionException("The limit for the maximum number of hotels has been reached. " +
                    "No more hotels can be built.");
        }

        boolean successfulPayment = payMoney(s.getHousePrice());

        if (successfulPayment) {
            s.buyHotel(g);
        } else {
            throw new IllegalMonopolyActionException("You don't have enough money to buy a hotel for this property.");
        }

        return g.receiveHotelCount(s);
    }

    private boolean checkOwnershipWholeStreet(Street streetToBuildHouseOn, MonopolyService service){
        for(Tile t: service.getTiles()){
            if(t.getType() == TileType.street){
                Street streetOfGroup = (Street) t;
                if(streetOfGroup.getStreetColor().equals(streetToBuildHouseOn.getStreetColor())){
                    if(!checkForOwnership(streetOfGroup)){
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
