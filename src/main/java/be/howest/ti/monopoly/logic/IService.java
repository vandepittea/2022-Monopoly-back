package be.howest.ti.monopoly.logic;

import be.howest.ti.monopoly.logic.implementation.tile.Tile;

import java.util.List;

import be.howest.ti.monopoly.logic.implementation.Game;

import java.util.List;

public interface IService {
    String getVersion();
    List<Tile> getTiles();
    Tile getTile(int position);
    Tile getTile(String name);
    Game createGame(int numberOfPlayers, String prefix);

    List<Game> getGames(Boolean started, Integer numberOfPlayers, String prefix);
}
