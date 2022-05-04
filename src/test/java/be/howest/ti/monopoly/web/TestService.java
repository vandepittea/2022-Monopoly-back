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
    public String[] getChance() {
        return delegate.getChance();
    }

    @Override
    public String[] getCommunityChest() {
        return delegate.getCommunityChest();
    }
}
