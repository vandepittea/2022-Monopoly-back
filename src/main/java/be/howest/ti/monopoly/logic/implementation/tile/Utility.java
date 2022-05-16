package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.Player;
import be.howest.ti.monopoly.web.views.PropertyView;

public class Utility extends Property {
    private final String rent;

    public Utility(int position, String name, int cost, int mortgage, int groupSize, String color, String rent) {
        super(position, name, cost, mortgage, groupSize, color, TileType.UTILITY);
        this.rent = rent;
    }
    public String getRent() {
        return rent;
    }

    @Override
    public int calculateRent(Player p, Game g){
        int totalDiceRoll = calculateDiceRoll(g);

        return totalDiceRoll * decideFourOrTenTimesDiceRoll(p);
    }

    private int calculateDiceRoll(Game g){
        Integer[] roll = g.getLastDiceRoll();
        return roll[0] + roll[1];
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
            if(propertyView.getPropertyObject().getType().equals(TileType.UTILITY)){
                amountOfUtilities++;
            }
        }

        return amountOfUtilities;
    }
}
