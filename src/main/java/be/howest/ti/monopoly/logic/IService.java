package be.howest.ti.monopoly.logic;

import be.howest.ti.monopoly.logic.implementation.Game;

import java.util.List;

public interface IService {
    String getVersion();
    Game createGame(int numberOfPlayers, String prefix);

    List<Game> getGames(Boolean started, Integer numberOfPlayers, String prefix);
}
