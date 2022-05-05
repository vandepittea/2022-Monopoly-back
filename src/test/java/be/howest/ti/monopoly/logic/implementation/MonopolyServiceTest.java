package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonopolyServiceTest {
    @Test
    void joinGameSuccesful(){
        MonopolyService s = new MonopolyService();
        Game g = s.createGame(2, "group17");
        Player p = new Player("Bob");

        s.joinGame(g.getId(), "Bob");

        assertTrue(g.getPlayers().contains(p));
        assertFalse(g.isStarted());
    }
    @Test
    void joinGameAndStartGame(){
        MonopolyService s = new MonopolyService();
        Game g = s.createGame(2, "group17");
        Player p = new Player("Bob");
        Player p2 = new Player("Jan");

        s.joinGame(g.getId(), "Bob");
        s.joinGame(g.getId(), "Jan");

        assertTrue(g.getPlayers().contains(p));
        assertTrue(g.getPlayers().contains(p2));
        assertTrue(g.isStarted());
    }
    @Test
    void joinGameNonExistingGameId(){
        MonopolyService s = new MonopolyService();

        Assertions.assertThrows(MonopolyResourceNotFoundException.class, () -> s.joinGame("group17_0", "Bob"));
    }
    @Test
    void joinGameTwoUserWithTheSameName(){
        MonopolyService s = new MonopolyService();
        Game g = s.createGame(2, "group17");

        s.joinGame(g.getId(), "Bob");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> s.joinGame(g.getId(), "Bob"));
    }
    @Test
    void joinAlreadyStartedGame(){
        MonopolyService s = new MonopolyService();
        Game g = s.createGame(2, "group17");

        s.joinGame(g.getId(), "Bob");
        s.joinGame(g.getId(), "Jan");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> s.joinGame(g.getId(), "Jonas"));
    }
}