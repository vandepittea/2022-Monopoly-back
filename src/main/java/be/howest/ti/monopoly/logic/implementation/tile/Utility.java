package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.Player;

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

    private int amountOfUtilitiesInOwnership(Player pl){
        int amountOfUtilities = 0;

        for(Property pr: pl.getProperties()){
            if(pr.getType().equals("utility")){
                amountOfUtilities++;
            }
        }

        return amountOfUtilities;
    }
}
