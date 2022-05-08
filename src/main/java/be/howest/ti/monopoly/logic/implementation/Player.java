package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.implementation.tile.*;
import be.howest.ti.monopoly.web.views.PropertyView;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    private void payDebt(int amount, Player creditor){
        boolean successfulPayment = payMoney(amount);
        if(!successfulPayment){
            debt += amount;
            this.creditor = creditor;
            throw new IllegalMonopolyActionException("You do not have enough money. You will have to sell properties " +
                    "so that you can pay off your debt. You have time until it is your turn again.");
        }
    }

    private void getMoney(int amount){
        money += amount;
    }

    public void MoveTo(Tile newTile) {
        currentTile = newTile;
    }

    public void turnOverAssetsTo(Player p){
        for(Property pr: properties){
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

    public void collectDebt(Property pr, Player pl, Game g){
        if(checkForOwnership(pr)){
            throw new IllegalMonopolyActionException("This property is not owned by you.");
        }
        else if(checkIfDebtorIsOnYourProperty(pr, pl)){
            throw new IllegalMonopolyActionException("The specified player is not on this property.");
        }
        else if(checkForNextRollDice(g)){
            throw new IllegalMonopolyActionException("You're too late. The next dice roll is already over.");
        }
        else{
            int rent = calculateRent(pr, pl, g);
            payDebt(rent, pl);
        }
    }

    private int calculateRent(Property pr, Player pl, Game g){
        if(pr.getType().equals("street")){
            Street s = (Street) pr;
            return s.calculateRent();
        }
        else if(pr.getType().equals("utility")){
            Utility u = (Utility) pr;
            return u.calculateRent(pl, g);
        }
        else{
            Railroad r = (Railroad) pr;
            return r.getRent();
        }
    }

    private boolean checkForOwnership(Property p){
        PropertyView pv = new PropertyView(p);
        return !properties.contains(pv);
    }

    private boolean checkIfDebtorIsOnYourProperty(Property pr, Player pl){
        return !pr.getName().equals(pl.currentTile.getName());
    }

    private boolean checkForNextRollDice(Game g){
        String descriptionLastRoll = g.getTurns().get(g.getTurns().size() - 1).getMoves().get(0).getDescription();
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
