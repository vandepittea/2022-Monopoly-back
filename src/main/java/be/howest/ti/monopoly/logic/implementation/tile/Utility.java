package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.Player;
import be.howest.ti.monopoly.logic.implementation.enums.StreetColor;
import be.howest.ti.monopoly.logic.implementation.enums.TileType;
import be.howest.ti.monopoly.logic.implementation.turn.DiceRoll;
import be.howest.ti.monopoly.web.views.PropertyView;

import java.util.Objects;

public class Utility extends Property {
    private final String rent;

    public Utility(int position, String name, int cost, int mortgage, int groupSize, StreetColor color, String rent) {
        super(position, name, cost, mortgage, groupSize, color, TileType.UTILITY);
        this.rent = rent;
    }
    public String getRent() {
        return rent;
    }

    @Override
    public int calculateRent(Player player, Game game){
        int totalDiceRoll = calculateDiceRoll(game);

        return totalDiceRoll * decideFourOrTenTimesDiceRoll(player);
    }

    private int calculateDiceRoll(Game g){
        DiceRoll roll = g.getLastDiceRoll();
        return roll.getValue();
    }

    private int decideFourOrTenTimesDiceRoll(Player p){
        if(amountOfUtilitiesInOwnership(p) >= 2){
            return 10;
        }
        else{
            return 4;
        }
    }

    private int amountOfUtilitiesInOwnership(Player player){
        int amountOfUtilities = 0;

        for(PropertyView propertyView: player.getProperties()){
            if(propertyView.getProperty().getType().equals(TileType.UTILITY)){
                amountOfUtilities++;
            }
        }

        return amountOfUtilities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Utility utility = (Utility) o;
        return rent.equals(utility.rent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rent);
    }
}
