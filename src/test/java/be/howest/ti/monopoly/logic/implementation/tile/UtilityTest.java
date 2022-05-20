package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.MonopolyService;
import be.howest.ti.monopoly.logic.implementation.Player;
import be.howest.ti.monopoly.logic.implementation.turn.DiceRoll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {
    MonopolyService service;
    Game game;
    Player player;
    Utility utility1;
    Utility utility2;

    @BeforeEach
    void init() {
        service = new MonopolyService();
        game = service.createGame(2, "group17");
        player = new Player("Bob", null);
        utility1 = (Utility) service.getTile(12);
        utility2 = (Utility) service.getTile(28);
    }

    @Test
    void calculateRentOneUtility(){
        game.joinGame("Bob");
        game.joinGame("Jan");
        player.getProperties().add(new OwnedProperty(utility1));
        game.rollDice("Bob");

        DiceRoll roll = game.getLastDiceRoll();
        int totalDiceRoll = roll.getValue();

        assertEquals(totalDiceRoll * 4, utility1.calculateRent(player, game));
    }

    @Test
    void calculateRentTwoUtilities(){
        game.joinGame("Bob");
        game.joinGame("Jan");
        player.buyProperty(utility1);
        player.buyProperty(utility2);
        game.rollDice("Bob");

        DiceRoll roll = game.getLastDiceRoll();
        int totalDiceRoll = roll.getValue();

        assertEquals(totalDiceRoll * 10, utility1.calculateRent(player, game));
    }
}