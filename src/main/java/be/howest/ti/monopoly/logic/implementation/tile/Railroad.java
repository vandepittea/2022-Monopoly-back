package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.Player;
import be.howest.ti.monopoly.logic.implementation.enums.TileType;

import java.util.Objects;

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
    public int calculateRent(Player player, Game game) {
        return rent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Railroad railroad = (Railroad) o;
        return rent == railroad.rent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rent);
    }
}
