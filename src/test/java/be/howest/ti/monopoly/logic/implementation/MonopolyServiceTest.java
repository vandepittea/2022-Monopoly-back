package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import be.howest.ti.monopoly.logic.implementation.tile.Property;
import be.howest.ti.monopoly.logic.implementation.tile.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonopolyServiceTest {
    MonopolyService service = new MonopolyService();

    @Test
    void testGetGames() {
        assertEquals(0, service.getGames(false, 2, "test").size());
        service.createGame(2, "test");
        assertEquals(1, service.getGames(false, 2, "test").size());
        assertEquals(0, service.getGames(true, 2, "test").size());
        assertEquals(0, service.getGames(false, 3, "test").size());
        assertEquals(0, service.getGames(false, 2, "mistake").size());
    }

    @Test
    void testGetGamesWithoutPrefix() {
        service.createGame(2, "test");
        assertEquals(0, service.getGames(false, 2, null).size());
    }

    @Test
    void testGetGamesWithoutNumber() {
        service.createGame(2, "test");
        assertEquals(1, service.getGames(false, null, "test").size());
    }

    @Test
    void testGetGamesWithoutStarted() {
        service.createGame(2, "test");
        assertEquals(1, service.getGames(null, 2, "test").size());
    }

    @Test
    void testGetGamesWithoutMultipleParamater() {
        service.createGame(2, "test");
        assertEquals(0, service.getGames(false, null, null).size());
        assertEquals(0, service.getGames(null, 2, null).size());
        assertEquals(1, service.getGames(null, null, "test").size());
    }

    @Test
    void buyProperty(){
        Game g = service.createGame(2, "group17");

        g.joinGame("Jan");
        g.setCurrentPlayer("Jan");
        g.setDirectSale("DK Summit");

        assertEquals("DK Summit", service.buyProperty(g.getId(),
                "Jan", "DK_Summit"));
    }

    @Test
    void buyPropertyIncorrectUser(){
        Game g = service.createGame(2, "group17");

        g.joinGame("Bob");
        g.joinGame("Jan");
        g.setCurrentPlayer("Jan");
        g.setDirectSale("DK Summit");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> service.buyProperty(g.getId(),
                "Bob", "DK_Summit"));
    }

    @Test
    void buyPropertyIncorrectProperty(){
        Game g = service.createGame(2, "group17");

        g.joinGame("Bob");
        g.joinGame("Jan");
        g.setCurrentPlayer("Jan");
        g.setDirectSale("Steam Gardens");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> service.buyProperty(g.getId(),
                "Jan", "DK_Summit"));
    }
}