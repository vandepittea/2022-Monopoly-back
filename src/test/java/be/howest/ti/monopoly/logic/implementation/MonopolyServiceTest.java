package be.howest.ti.monopoly.logic.implementation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonopolyServiceTest {

    @Test
    void testGetGames() {
        MonopolyService service = new MonopolyService();
        assertEquals(0, service.getGames(false, 2, "test").size());
        service.createGame(2, "test");
        assertEquals(1, service.getGames(false, 2, "test").size());
        assertEquals(0, service.getGames(true, 2, "test").size());
        assertEquals(0, service.getGames(false, 3, "test").size());
        assertEquals(0, service.getGames(false, 2, "mistake").size());
    }

    @Test
    void testGetGamesWithoutPrefix() {
        MonopolyService service = new MonopolyService();
        service.createGame(2, "test");
        assertEquals(0, service.getGames(false, 2, null).size());
    }

    @Test
    void testGetGamesWithoutNumber() {
        MonopolyService service = new MonopolyService();
        service.createGame(2, "test");
        assertEquals(1, service.getGames(false, null, "test").size());
    }

    @Test
    void testGetGamesWithoutStarted() {
        MonopolyService service = new MonopolyService();
        service.createGame(2, "test");
        assertEquals(1, service.getGames(null, 2, "test").size());
    }

    @Test
    void testGetGamesWithoutMultipleParamater() {
        MonopolyService service = new MonopolyService();
        service.createGame(2, "test");
        assertEquals(0, service.getGames(false, null, null).size());
        assertEquals(0, service.getGames(null, 2, null).size());
        assertEquals(1, service.getGames(null, null, "test").size());
    }
}