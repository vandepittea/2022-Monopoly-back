package be.howest.ti.monopoly.logic;

import be.howest.ti.monopoly.logic.implementation.tile.Tile;

import java.util.List;

import be.howest.ti.monopoly.logic.implementation.Game;

public interface IService {
    String getVersion();
    List<Tile> getTiles();
    Tile getTile(int position);
    Tile getTile(String name);
    Game createGame(int numberOfPlayers, String prefix, String gameName);
    List<Game> getGames(Boolean started, Integer numberOfPlayers, String prefix);
    Game getGame(String gameId);
    List<String> getChance();
    List<String> getCommunityChest();
    void joinGame(String gameId, String playerName);
    String buyProperty(String gameId, String playerName, String propertyName);
    int buyHouse(String gameId, String playerName, String propertyName);
    int sellHouse(String gameId, String playerName, String propertyName);
    Game rollDice(String gameId, String playerName);
    Game declareBankruptcy(String gameId, String playerName);
    String dontBuyProperty(String gameId, String playerName, String propertyName);
    boolean collectDebt(String gameId, String playerName, String propertyName, String debtorName);
    int buyHotel(String gameId, String playerName, String propertyName);
    int sellHotel(String gameId, String playerName, String propertyName);
    void getOutOfJailFree(String gameId, String playerName);
    void getOutOfJailFine(String gameId, String playerName);
    void useComputeTax(String gameId, String playerName);
    void useEstimateTax(String gameId, String playerName);
    void assignPawn(String gameId, String playerName, String pawn);
}
