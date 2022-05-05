package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.implementation.tile.PlayerProperty;
import be.howest.ti.monopoly.logic.implementation.tile.Tile;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Player {
    private final String name;

    private String currentTile;
    private boolean jailed;
    private int money;
    private boolean bankrupt;
    private int getOutOfJailCards;
    private String taxSystem;
    private Set<PlayerProperty> properties;
    private int debt;

    public Player(String name){
        this.name = name;
        this.currentTile = "Go";
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
    public Set<PlayerProperty> getProperties() {
        return properties;
    }
    public int getDebt() {
        return debt;
    }
    public void setMoney(int money) {
        this.money = money;
    }

    public void addProperty(PlayerProperty p){
        properties.add(p);
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
