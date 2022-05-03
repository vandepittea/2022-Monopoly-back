package be.howest.ti.monopoly.logic;

import be.howest.ti.monopoly.logic.implementation.tile.Tile;

import java.util.List;

import be.howest.ti.monopoly.logic.implementation.Game;

public interface IService {
    String getVersion();
    List<Tile> getTiles();
    Game createGame(int numberOfPlayers, String prefix);
}
