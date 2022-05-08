package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    MonopolyService service = new MonopolyService();
    Game game = service.createGame(2, "group17");

    @Test
    void joinGameSuccesful(){
        Player p = new Player("Bob", null);

        game.joinGame("Bob");

        assertTrue(game.getPlayers().contains(p));
        assertFalse(game.isStarted());
    }

    @Test
    void joinGameAndStartGame(){
        Player p = new Player("Bob", null);
        Player p2 = new Player("Jan", null);

        game.joinGame("Bob");
        game.joinGame("Jan");

        assertTrue(game.getPlayers().contains(p));
        assertTrue(game.getPlayers().contains(p2));
        assertTrue(game.isStarted());
    }

    @Test
    void joinGameTwoUserWithTheSameName(){
        game.joinGame("Bob");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.joinGame( "Bob"));
    }

    @Test
    void joinAlreadyStartedGame() {
        Game g = service.createGame(2, "group17");

        g.joinGame("Bob");
        g.joinGame("Jan");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> g.joinGame( "Jonas"));
    }

    @Test
    void getPlayerSuccesful(){
        Game g = service.createGame(2, "group17");
        Player p = new Player("Bob", null);
        g.joinGame("Bob");

        assertEquals(p, g.getPlayer("Bob"));
    }

    @Test
    void getPlayerUnexistedPlayer(){
        Game g = service.createGame(2, "group17");

        Assertions.assertThrows(MonopolyResourceNotFoundException.class, () -> g.getPlayer("Unexisted"));
    }

    @Test
    void rollDiceGameNotStarted() {
        Game game = service.createGame(2, "group17");
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.rollDice("Jonas"));
    }

    //TODO: make rollDice test for an ended game
    //TODO: make rollDice test for a player in debt, when debt is properly implemented

    @Test
    void rollDiceWrongPlayer() {
        Game game = service.createGame(2, "group17");
        game.joinGame("Jonas");
        game.joinGame("Thomas");
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.rollDice("Thomas"));
    }

    @Test
    void rollDiceWhenDirectSale() {
        Game game = service.createGame(2, "group17");
        game.joinGame("Jonas");
        game.joinGame("Thomas");
        while (game.getDirectSale() == null) {
            assertEquals(game, service.rollDice(game.getId(), game.getCurrentPlayer()));
        }
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.rollDice("Jonas"));
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.rollDice("Thomas"));
    }

    //TODO: make rollDice test for getting jailed when not buying a property is implemented

    @Test
    void rollDice() {
        Game game = service.createGame(2, "group17");

        game.joinGame("Jonas");
        game.joinGame("Thomas");

        assertEquals("Peach Castle", game.getPlayer("Jonas").getCurrentTile());
        assertEquals(game, service.rollDice(game.getId(), "Jonas"));
        assertNotEquals("Peach Castle", game.getPlayer("Jonas").getCurrentTile());

        Integer[] lastDiceRoll = game.getLastDiceRoll();
        if ((game.getDirectSale() == null) && (!lastDiceRoll[0].equals(lastDiceRoll[1]))) {
            assertEquals("Thomas", game.getCurrentPlayer());
        } else {
            assertEquals("Jonas", game.getCurrentPlayer());
        }

        assertEquals(1, game.getTurns().size());
        assertEquals(game.getLastDiceRoll(), game.getTurns().get(game.getTurns().size() - 1).getRoll());
    }
}