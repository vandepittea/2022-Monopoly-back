package be.howest.ti.monopoly.web;

import be.howest.ti.monopoly.logic.IService;
import be.howest.ti.monopoly.logic.ServiceAdapter;
import be.howest.ti.monopoly.logic.implementation.tile.Tile;

import java.util.List;
import be.howest.ti.monopoly.logic.implementation.Game;


public class TestService implements IService {

    IService delegate = new ServiceAdapter();

    void setDelegate(IService delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getVersion() {
        return delegate.getVersion();
    }

    @Override
    public List<Tile> getTiles() {
        return delegate.getTiles();
    }

    @Override
    public Tile getTile(int position) {
        return delegate.getTile(position);
    }

    @Override
    public Tile getTile(String name) {
        return delegate.getTile(name);
    }

    @Override
    public Game createGame(int numberOfPlayers, String prefix) {
        return delegate.createGame(numberOfPlayers, prefix);
    }

    @Override
    public List<Game> getGames(Boolean started, Integer numberOfPlayers, String prefix) {
        return delegate.getGames(started, numberOfPlayers, prefix);
    }

    @Override
    public Game getGame(String gameId) {
        return delegate.getGame(gameId);
    }

    @Override
    public String[] getChance() {
        return delegate.getChance();
    }

    @Override
    public String[] getCommunityChest() {
        return delegate.getCommunityChest();
    }

    @Override
    public void joinGame(String gameId, String playerName) {
        delegate.joinGame(gameId, playerName);
    }

    @Override
    public String buyProperty(String gameId, String playerName, String propertyName) {
        return delegate.buyProperty(gameId, playerName, propertyName);
    }

    @Override
    public Game rollDice(String gameId, String playerName) {
        return delegate.rollDice(gameId, playerName);
    }

    @Override
    public Game declareBankruptcy(String gameId, String playerName) {
        return delegate.declareBankruptcy(gameId, playerName);
    }

    @Override
    public String dontBuyProperty(String gameId, String playerName, String propertyName) {
        return delegate.dontBuyProperty(gameId, playerName, propertyName);
    }

    @Override
    public boolean collectDebt(String gameId, String playerName, String propertyName, String debtorName) {
        return delegate.collectDebt(gameId, playerName, propertyName, debtorName);
    }

    @Override
    public int buyHouse(String gameId, String playerName, String propertyName) {
        return delegate.buyHouse(gameId, playerName, propertyName);
    }

    @Override
    public int sellHouse(String gameId, String playerName, String propertyName) {
        return delegate.sellHouse(gameId, playerName, propertyName);
    }

    @Override
    public int buyHotel(String gameId, String playerName, String propertyName) {
        return delegate.buyHotel(gameId, playerName, propertyName);
    }


    @Override
    public void getOutOfJailFree(String gameId, String playerName) {
        delegate.getOutOfJailFree(gameId, playerName);
    }

}
