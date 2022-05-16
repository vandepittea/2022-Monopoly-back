package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.Player;

public class Railroad extends Property {
    private final int rent;

    public Railroad(int position, String name, int cost, int mortgage, int groupSize, String color, int rent) {
        super(position, name, cost, mortgage, groupSize, color, TileType.RAILROAD);
        this.rent = rent;
    }

    public int getRent() {
        return rent;
    }

    @Override
    public int calculateRent(Player p, Game g) {
        return rent;
    }
}
