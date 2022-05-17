package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import be.howest.ti.monopoly.logic.implementation.enums.StreetColor;
import be.howest.ti.monopoly.logic.implementation.tile.Street;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonopolyServiceTest {
    MonopolyService service;
    Game game;
    Street street;

    @BeforeEach
    void init() {
        service = new MonopolyService();
        game = service.createGame(2, "group17");
        street = new Street(1, "Peach's Garden", 60, 30, 2, StreetColor.PURPLE,
                new Integer[]{10, 30, 90, 160, 250}, 50, 2);
    }

    @Test
    void getTileOnPositionSuccesful(){
        assertEquals(street, service.getTile(1));
    }

    @Test
    void getTileOnPositionIndexOutOfBound(){
        Assertions.assertThrows(MonopolyResourceNotFoundException.class, () -> service.getTile(40));
    }

    @Test
    void getTileOnNameSuccesful(){
        assertEquals(street, service.getTile("Peach's_Garden"));
    }

    @Test
    void getTileOnNameUnexistedTile(){
        Assertions.assertThrows(MonopolyResourceNotFoundException.class, () -> service.getTile("Unexisted_Tile"));
    }

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
        assertEquals(0, service.getGames(false, 2, null).size());
    }

    @Test
    void testGetGamesWithoutNumber() {
        assertEquals(1, service.getGames(false, null, "group17").size());
    }

    @Test
    void testGetGamesWithoutStarted() {
        assertEquals(1, service.getGames(null, 2, "group17").size());
    }

    @Test
    void testGetGamesWithoutMultipleParamater() {
        assertEquals(0, service.getGames(false, null, null).size());
        assertEquals(0, service.getGames(null, 2, null).size());
        assertEquals(1, service.getGames(null, null, "group17").size());
    }

    @Test
    void buyProperty(){
        game.joinGame("Jan");
        game.setCurrentPlayer("Jan");
        game.setDirectSale("DK Summit");

        assertEquals("DK Summit", service.buyProperty(game.getId(),
                "Jan", "DK_Summit"));
        assertTrue(game.isCanRoll());
        assertNull(game.getDirectSale());
    }

    @Test
    void buyPropertyIncorrectUser(){
        game.joinGame("Bob");
        game.joinGame("Jan");
        game.setCurrentPlayer("Jan");
        game.setDirectSale("DK Summit");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> service.buyProperty(game.getId(),
                "Bob", "DK_Summit"));
    }

    @Test
    void buyPropertyIncorrectProperty(){
        game.joinGame("Bob");
        game.joinGame("Jan");
        game.setCurrentPlayer("Jan");
        game.setDirectSale("Steam Gardens");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> service.buyProperty(game.getId(),
                "Jan", "DK_Summit"));
    }

    @Test
    void dontBuyProperty(){
        game.joinGame("Jan");
        game.setCurrentPlayer("Jan");
        game.setDirectSale("DK Summit");

        assertEquals("DK Summit", service.dontBuyProperty(game.getId(),
                "Jan", "DK_Summit"));
        assertTrue(game.isCanRoll());
        assertNull(game.getDirectSale());
    }

    @Test
    void dontBuyPropertyIncorrectUser(){
        game.joinGame("Bob");
        game.joinGame("Jan");
        game.setCurrentPlayer("Jan");
        game.setDirectSale("DK Summit");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> service.dontBuyProperty(game.getId(),
                "Bob", "DK_Summit"));
    }

    @Test
    void dontBuyPropertyIncorrectProperty(){
        game.joinGame("Bob");
        game.joinGame("Jan");
        game.setCurrentPlayer("Jan");
        game.setDirectSale("Steam Gardens");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> service.dontBuyProperty(game.getId(),
                "Jan", "DK_Summit"));
    }
}