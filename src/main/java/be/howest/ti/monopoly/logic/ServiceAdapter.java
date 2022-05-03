package be.howest.ti.monopoly.logic;

import be.howest.ti.monopoly.logic.implementation.Game;

public class ServiceAdapter implements IService {

    @Override
    public String getVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Game createGame(int numberOfPlayers, String prefix) {
        throw new UnsupportedOperationException();
    }
}
