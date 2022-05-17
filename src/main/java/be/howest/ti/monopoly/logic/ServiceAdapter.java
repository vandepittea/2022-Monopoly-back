package be.howest.ti.monopoly.logic;

import be.howest.ti.monopoly.logic.implementation.tile.Tile;

import java.util.List;

import be.howest.ti.monopoly.logic.implementation.Game;

public class ServiceAdapter implements IService {

    @Override
    public String getVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Tile> getTiles() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Tile getTile(int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Tile getTile(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Game createGame(int numberOfPlayers, String prefix) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Game> getGames(Boolean started, Integer numberOfPlayers, String prefix) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Game getGame(String gameId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getChance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getCommunityChest() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void joinGame(String gameId, String playerName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String buyProperty(String gameId, String playerName, String propertyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Game rollDice(String gameId, String playerName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Game declareBankruptcy(String gameId, String playerName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String dontBuyProperty(String gameId, String playerName, String propertyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean collectDebt(String gameId, String playerName, String propertyName, String debtorName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int buyHotel(String gameId, String playerName, String propertyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int sellHotel(String gameId, String playerName, String propertyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getOutOfJailFree(String gameId, String playerName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getOutOfJailFine(String gameId, String playerName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void useComputeTax(String gameId, String playerName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void useEstimateTax(String gameId, String playerName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int buyHouse(String gameId, String playerName, String propertyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int sellHouse(String gameId, String playerName, String propertyName) {
        throw new UnsupportedOperationException();
    }
}
