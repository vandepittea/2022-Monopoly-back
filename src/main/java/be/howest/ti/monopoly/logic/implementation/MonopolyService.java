package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.ServiceAdapter;
import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import be.howest.ti.monopoly.logic.implementation.generator.Generator;
import be.howest.ti.monopoly.logic.implementation.tile.*;

import java.util.*;


public class MonopolyService extends ServiceAdapter {
    private final List<Tile> tiles;

    private Set<Game> games;

    public MonopolyService() {
        games = new HashSet<>();
        tiles = Generator.generateTiles();
    }

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public List<Tile> getTiles() {
        return tiles;
    }

    @Override
    public List<String> getChance() {
        return CardExecutingTile.getChances();
    }

    @Override
    public List<String> getCommunityChest() {
        return CardExecutingTile.getCommunityChests();
    }

    @Override
    public Tile getTile(int position) {
        for (Tile tile : getTiles()) {
            if (tile.getPosition() == position) {
                return tile;
            }
        }
        throw new MonopolyResourceNotFoundException("No such tile, index out of bounds.");
    }

    @Override
    public Tile getTile(String name) {
        for (Tile tile : getTiles()) {
            if (tile.getNameAsPathParameter().equals(name)) {
                return tile;
            }
        }
        throw new MonopolyResourceNotFoundException("No such tile, name doesn't exist. Check if the name of the " +
                "tile is spelled correctly.");
    }

    @Override
    public List<Game> getGames(Boolean started, Integer numberOfPlayers, String prefix) {
        List<Game> gamesList = new ArrayList<>();

        games.forEach(game -> {
            if ((started == null) || (started == game.isStarted())) {
                if ((numberOfPlayers == null) || (numberOfPlayers == game.getNumberOfPlayers())) {
                    String gamePrefix = game.getId().split("_")[0];
                    if (gamePrefix.equals(prefix)) {
                        gamesList.add(game);
                    }
                }
            }
        });

        return gamesList;
    }

    @Override
    public Game createGame(int numberOfPlayers, String prefix) {
        Game newGame = new Game(this, numberOfPlayers, prefix, tiles.get(0));
        games.add(newGame);
        return newGame;
    }

    @Override
    public void joinGame(String gameId, String playerName) {
        Game game = getGame(gameId);

        game.joinGame(playerName);
    }

    @Override
    public String buyProperty(String gameId, String playerName, String propertyName) {
        Game game = getGame(gameId);
        Player player = game.getPlayer(playerName);
        Tile tile = getTile(propertyName);
        Property property = (Property) tile;

        if (!player.equals(game.getCurrentPlayer()) || !tile.getName().equals(game.getDirectSale())) {
            throw new IllegalMonopolyActionException("You tried to do something which is against the rules of " +
                    "Monopoly. In this case, it is most likely not your place to buy this property and/or you are " +
                    "trying to buy the wrong property.");
        }

        player.buyProperty(property);
        game.handlePropertySale();
        return property.getName();
    }

    @Override
    public String dontBuyProperty(String gameId, String playerName, String propertyName) {
        Game game = getGame(gameId);
        Player player = game.getPlayer(playerName);
        Property property = (Property) getTile(propertyName);

        if (!player.equals(game.getCurrentPlayer())) {
            throw new IllegalMonopolyActionException("Only the current player can choose not to buy a property");
        }
        if (!property.getName().equals(game.getDirectSale())) {
            throw new IllegalMonopolyActionException("You can only choose not to buy the property you're standing on");
        }

        game.handlePropertySale();
        return property.getName();
    }

    @Override
    public Game getGame(String gameId) {
        for (Game game : games) {
            if (game.getId().equals(gameId)) {
                return game;
            }
        }

        throw new MonopolyResourceNotFoundException("The game you are looking for does not exist. Double check the ID.");
    }

    @Override
    public Game rollDice(String gameId, String playerName) {
        Game game = getGame(gameId);
        game.rollDice(playerName);
        return game;
    }

    @Override
    public Game declareBankruptcy(String gameId, String playerName) {
        Game game = getGame(gameId);
        game.declareBankruptcy(playerName);
        return game;
    }

    @Override
    public boolean collectDebt(String gameId, String playerName, String propertyName, String debtorName) {
        Game game = getGame(gameId);
        Player player = game.getPlayer(playerName);
        Player debtor = game.getPlayer(debtorName);
        Property property = (Property) getTile(propertyName);
        player.collectDebt(property, debtor, game);
        return true;
    }

    @Override
    public int buyHouse(String gameId, String playerName, String propertyName) {
        Game game = getGame(gameId);
        Player player = game.getPlayer(playerName);
        Street street = (Street) getTile(propertyName);
        return player.buyHouseOrHotel(this, game, street);
    }

    @Override
    public int sellHouse(String gameId, String playerName, String propertyName) {
        Game game = getGame(gameId);
        Player player = game.getPlayer(playerName);
        Street street = (Street) getTile(propertyName);
        return player.sellHouseOrHotel(this, game, street);
    }

    @Override
    public int buyHotel(String gameId, String playerName, String propertyName){
        return buyHouse(gameId, playerName, propertyName);
    }

    @Override
    public int sellHotel(String gameId, String playerName, String propertyName){
        return sellHouse(gameId, playerName, propertyName);
    }

    @Override
    public void getOutOfJailFree(String gameId, String playerName){
        Game game = getGame(gameId);
        Player player = game.getPlayer(playerName);
        player.getOutOfJailFree();
    }

    @Override
    public void getOutOfJailFine(String gameId, String playerName) {
        Game game = getGame(gameId);
        Player player = game.getPlayer(playerName);
        player.getOutOfJailFine();
    }
}
