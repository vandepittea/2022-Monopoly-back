package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.MonopolyService;
import be.howest.ti.monopoly.logic.implementation.Player;
import be.howest.ti.monopoly.logic.implementation.enums.StreetColor;
import be.howest.ti.monopoly.web.views.PropertyView;
import jdk.jshell.execution.Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {
    @Test
    void calculateRentOneUtility(){
        MonopolyService s = new MonopolyService();
        Game g = s.createGame(2, "group17");
        Player p = new Player("Bob", null);
        Utility u = new Utility(12, "Electric Koopa Farm", 150, 75, 2,
                StreetColor.WHITE, "4 or 10 times the dice roll");

        g.joinGame("Bob");
        g.joinGame("Jan");
        p.getProperties().add(new PropertyView(u));
        g.rollDice("Bob");

        Integer[] roll = g.getLastDiceRoll();
        int totalDiceRoll = roll[0] + roll[1];

        assertEquals(totalDiceRoll * 4, u.calculateRent(p, g));
    }

    @Test
    void calculateRentTwoUtilities(){
        MonopolyService s = new MonopolyService();
        Game g = s.createGame(2, "group17");
        Player p = new Player("Bob", null);
        Utility u = (Utility) s.getTile(12);
        Utility u2 = (Utility) s.getTile(28);

        g.joinGame("Bob");
        g.joinGame("Jan");
        p.buyProperty(u);
        p.buyProperty(u2);
        g.rollDice("Bob");

        Integer[] roll = g.getLastDiceRoll();
        int totalDiceRoll = roll[0] + roll[1];

        assertEquals(totalDiceRoll * 10, u.calculateRent(p, g));
    }
}