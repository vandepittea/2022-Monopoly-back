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
        Player p = new Player("Bob");

        game.joinGame("Bob");

        assertTrue(game.getPlayers().contains(p));
        assertFalse(game.isStarted());
    }

    @Test
    void joinGameAndStartGame(){
        Player p = new Player("Bob");
        Player p2 = new Player("Jan");

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
        Player p = new Player("Bob");
        g.joinGame("Bob");

        assertEquals(p, g.getPlayer("Bob"));
    }

    @Test
    void getPlayerUnexistedPlayer(){
        Game g = service.createGame(2, "group17");

        Assertions.assertThrows(MonopolyResourceNotFoundException.class, () -> g.getPlayer("Unexisted"));
    }
}