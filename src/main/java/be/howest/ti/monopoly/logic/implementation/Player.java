package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.implementation.tile.Property;
import be.howest.ti.monopoly.logic.implementation.tile.Tile;
import be.howest.ti.monopoly.web.views.PropertyView;

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

    public void setMoney(int money) {
        this.money = money;
    }

    public void goToJail(Tile jailTile) {
        this.jailed = true;
        moveTo(jailTile);
    }

    public void getOutOfJail() {
        this.jailed = false;
    }

    public void buyProperty(Property pr) {
        boolean succesfulPayment = payProperty(pr);

        if (succesfulPayment) {
            addProperty(new PropertyView(pr));
        } else {
            throw new IllegalMonopolyActionException("You don't have enough money to buy this property");
        }
    }

    private void addProperty(PropertyView p) {
        properties.add(p);
    }

    private boolean payProperty(Property pr) {
        if (money > pr.getCost()) {
            money -= pr.getCost();
            return true;
        } else {
            return false;
        }
    }

    public void moveTo(Tile newTile) {
        currentTile = newTile;
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
