package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.implementation.tile.Property;
import be.howest.ti.monopoly.logic.implementation.tile.Tile;
import be.howest.ti.monopoly.web.views.PropertyView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

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
    private Set<PropertyView> properties;
    private int debt;
    private Player debtor;

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
        this.debtor = null;
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

    public Set<PropertyView> getProperties() {
        return properties;
    }

    public int getDebt() {
        return debt;
    }

    @JsonIgnore
    public Player getDebtor() {
        return debtor;
    }

    public void becomeBankrupt(){
        this.bankrupt = true;
    }

    public void buyProperty(Property pr) {
        boolean succesfulPayment = payMoney(pr.getCost());

        if (succesfulPayment) {
            addProperty(new PropertyView(pr));
        } else {
            throw new IllegalMonopolyActionException("You don't have enough money to buy this property");
        }
    }

    private void addProperty(PropertyView p) {
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

    private void getMoney(int amount){
        money += amount;
    }

    public void MoveTo(Tile newTile) {
        currentTile = newTile;
    }

    public void turnOverAssetsTo(Player p){
        for(PropertyView pr: properties){
            p.addProperty(pr);
        }
        p.getMoney(money);

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

    public void collectDebt(Property pr, Player pl, String descriptionLastRoll){
        if(checkForOwnership(pr)){
            throw new IllegalMonopolyActionException("This property is not owned by you.");
        }
        else if(checkIfDebtorIsOnYourProperty(pr, pl)){
            throw new IllegalMonopolyActionException("The specified player is not on this property.");
        }
        else if(checkForNextRollDice(descriptionLastRoll)){
            throw new IllegalMonopolyActionException("You're too late. The next dice roll is already over.");
        }
        else{

        }
    }

    private boolean checkForOwnership(Property p){
        PropertyView pv = new PropertyView(p);
        return !properties.contains(pv);
    }

    private boolean checkIfDebtorIsOnYourProperty(Property pr, Player pl){
        return !pr.getName().equals(pl.currentTile.getName());
    }

    private boolean checkForNextRollDice(String descriptionLastRoll){
        return !descriptionLastRoll.equals("should pay rent");
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
