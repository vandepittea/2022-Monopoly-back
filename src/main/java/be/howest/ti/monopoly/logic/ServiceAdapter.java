package be.howest.ti.monopoly.logic;

import be.howest.ti.monopoly.logic.implementation.Game;

import java.util.List;

public class ServiceAdapter implements IService {

    @Override
    public String getVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Game createGame(int numberOfPlayers, String prefix) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Game> getGames(boolean started, boolean startedMatters, int numberOfPlayers, boolean playersMatter, String prefix) {
        throw new UnsupportedOperationException();
    }
}
