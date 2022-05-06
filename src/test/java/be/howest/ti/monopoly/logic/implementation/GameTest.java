package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    MonopolyService s = new MonopolyService();
    Game g = s.createGame(2, "group17");

    @Test
    void joinGameSuccesful(){
        Player p = new Player("Bob");

        g.joinGame("Bob");

        assertTrue(g.getPlayers().contains(p));
        assertFalse(g.isStarted());
    }

    @Test
    void joinGameAndStartGame(){
        Player p = new Player("Bob");
        Player p2 = new Player("Jan");

        g.joinGame("Bob");
        g.joinGame("Jan");

        assertTrue(g.getPlayers().contains(p));
        assertTrue(g.getPlayers().contains(p2));
        assertTrue(g.isStarted());
    }

    @Test
    void joinGameTwoUserWithTheSameName(){
        g.joinGame("Bob");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> g.joinGame( "Bob"));
    }

    @Test
    void joinAlreadyStartedGame() {
        Game g = s.createGame(2, "group17");

        g.joinGame("Bob");
        g.joinGame("Jan");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> g.joinGame( "Jonas"));
    }
}