package be.howest.ti.monopoly.logic;

import be.howest.ti.monopoly.logic.implementation.tile.Tile;

import java.util.List;
import java.util.Set;

import be.howest.ti.monopoly.logic.implementation.Game;

public interface IService {
    String getVersion();
    List<Tile> getTiles();
    Tile getTile(int position);
    Tile getTile(String name);
    Game createGame(int numberOfPlayers, String prefix);
    List<Game> getGames(Boolean started, Integer numberOfPlayers, String prefix);
    String[] getChance();
    String[] getCommunityChest();
    void joinGame(String gameId, String playerName);
    void buyProperty(String gameId, String playerName, String propertyName);
}
