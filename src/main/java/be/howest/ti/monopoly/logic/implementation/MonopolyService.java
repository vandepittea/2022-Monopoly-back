package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.ServiceAdapter;
import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import be.howest.ti.monopoly.logic.implementation.tile.*;

import java.util.*;


public class MonopolyService extends ServiceAdapter {
    private Set<Game> games;
    private final List<Tile> tiles;

    public MonopolyService() {
        games = new HashSet<>();

        tiles = new ArrayList<>();
        generateTiles();
    }

    private void generateTiles() {
        tiles.add(new SimpleTile(0, "Peach Castle", TileType.GO));
        tiles.add(new Street(1, "Peach's Garden", 60, 30, 2, "PURPLE", new Integer[]{10, 30, 90, 160, 250}, 50, "PURPLE", 2));
        tiles.add(new CardExecutingTile(2, "Community Chest I", TileType.COMMUNITY_CHEST));
        tiles.add(new Street(3, "Yoshi Valley", 60, 30, 2, "PURPLE", new Integer[]{20, 60, 180, 320, 450}, 50, "PURPLE", 4));
        tiles.add(new CardExecutingTile(4, "Bowser Jr.", TileType.TAX_INCOME));
        tiles.add(new Railroad(5, "Fire Flower", 200, 100, 4, "BLACK", 25));
        tiles.add(new Street(6, "Pokey Pyramid", 100, 50, 3, "LIGHTBLUE", new Integer[]{30, 90, 270, 400, 550}, 50, "LIGHTBLUE", 6));
        tiles.add(new CardExecutingTile(7, "Chance I", TileType.CHANCE));
        tiles.add(new Street(8, "Smoldering Sands", 100, 50, 3, "LIGHTBLUE", new Integer[]{30, 90, 270, 400, 550}, 50, "LIGHTBLUE", 6));
        tiles.add(new Street(9, "Cheep-Cheep Oasis", 120, 60, 3, "LIGHTBLUE", new Integer[]{40, 100, 300, 450, 600}, 50, "LIGHTBLUE", 8));
        tiles.add(new SimpleTile(10, "Jail", TileType.JAIL));
        tiles.add(new Street(11, "Delfino Airship", 140, 70, 3, "VIOLET", new Integer[]{50, 150, 450, 625, 750}, 100, "VIOLET", 10));
        tiles.add(new Utility(12, "Electric Koopa Farm", 150, 75, 2, "WHITE", "4 or 10 times the dice roll"));
        tiles.add(new Street(13, "Delfino Plaza", 140, 70, 3, "VIOLET", new Integer[]{50, 150, 450, 625, 750}, 100, "VIOLET", 10));
        tiles.add(new Street(14, "MT Corona", 160, 80, 3, "VIOLET", new Integer[]{60, 180, 500, 700, 900}, 100, "VIOLET", 12));
        tiles.add(new Railroad(15, "F.L.U.D.D.", 200, 100, 4, "BLACK", 25));
        tiles.add(new Street(16, "Sirena Beach", 180, 90, 3, "ORANGE", new Integer[]{70, 200, 550, 750, 950}, 100, "ORANGE", 14));
        tiles.add(new CardExecutingTile(17, "Community Chest II", TileType.COMMUNITY_CHEST));
        tiles.add(new Street(18, "Pinna Park", 180, 90, 3, "ORANGE", new Integer[]{70, 200, 550, 750, 950}, 100, "ORANGE", 14));
        tiles.add(new Street(19, "Noki Bay", 200, 100, 3, "ORANGE", new Integer[]{80, 220, 600, 800, 1000}, 100, "ORANGE", 16));
        tiles.add(new SimpleTile(20, "Free Parking", TileType.FREE_PARKING));
        tiles.add(new Street(21, "Maple Treeway", 220, 110, 3, "RED", new Integer[]{90, 250, 700, 875, 1050}, 150, "RED", 18));
        tiles.add(new CardExecutingTile(22, "Chance II", TileType.CHANCE));
        tiles.add(new Street(23, "DK Summit", 220, 110, 3, "RED", new Integer[]{90, 250, 700, 875, 1050}, 150, "RED", 18));
        tiles.add(new Street(24, "Sherbet Land", 240, 120, 3, "RED", new Integer[]{100, 300, 750, 925, 1100}, 150, "RED", 20));
        tiles.add(new Railroad(25, "Mario Cart", 200, 100, 4, "Black", 25));
        tiles.add(new Street(26, "DK Mountain", 260, 130, 3, "YELLOW", new Integer[]{110, 330, 800, 975, 1150}, 150, "YELLOW", 22));
        tiles.add(new Street(27, "Wario's Goldmine", 260, 130, 3, "YELLOW", new Integer[]{110, 330, 800, 975, 1150,}, 150, "YELLOW", 22));
        tiles.add(new Utility(28, "Gas Pump", 150, 75, 2, "WHITE", "4 or 10 times the dice roll"));
        tiles.add(new Street(29, "Grumble Volcano", 280, 140, 3, "YELLOW", new Integer[]{120, 360, 850, 1025, 1200}, 150, "YELLOW", 24));
        tiles.add(new SimpleTile(30, "Go to Jail", TileType.GO_TO_JAIL));
        tiles.add(new Street(31, "Steam Gardens", 300, 150, 3, "DARKGREEN", new Integer[]{130, 390, 900, 1100, 1275}, 200, "DARKGREEN", 26));
        tiles.add(new Street(32, "Honeylune Ridge", 300, 150, 3, "DARKGREEN", new Integer[]{130, 390, 900, 1100, 1275}, 200, "DARKGREEN", 26));
        tiles.add(new CardExecutingTile(33, "Community Chest III", TileType.COMMUNITY_CHEST));
        tiles.add(new Street(34, "Nimbus Arena", 320, 160, 3, "DARKGREEN", new Integer[]{150, 450, 1000, 1200, 1400}, 200, "DARKGREEN", 28));
        tiles.add(new Railroad(35, "Cappy", 200, 100, 4, "BLACK", 25));
        tiles.add(new CardExecutingTile(36, "Chance III", TileType.CHANCE));
        tiles.add(new Street(37, "Crumbleden", 350, 175, 2, "DARKBLUE", new Integer[]{175, 500, 1100, 1300, 1500}, 200, "DARKGREEN", 35));
        tiles.add(new CardExecutingTile(38, "Luxury Tax", TileType.LUXURY_TAX));
        tiles.add(new Street(39, "Bowser Castle", 400, 200, 2, "DARKBLUE", new Integer[]{200, 600, 1400, 1700, 2000}, 200, "DARKBLUE", 50));
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
        Game g = getGame(gameId);

        g.joinGame(playerName);
    }

    @Override
    public String buyProperty(String gameId, String playerName, String propertyName) {
        Game g = getGame(gameId);
        Player pl = g.getPlayer(playerName);
        Tile t = getTile(propertyName);
        Property pr = (Property) t;

        if (playerName.equals(g.getCurrentPlayer()) && t.getName().equals(g.getDirectSale())) {
            pl.buyProperty(pr);
            g.handlePropertySale();
            return pr.getName();
        } else {
            throw new IllegalMonopolyActionException("You tried to do something which is against the rules of " +
                    "Monopoly. In this case, it is most likely not your place to buy this property and/or you are " +
                    "trying to buy the wrong property.");
        }
    }

    @Override
    public String dontBuyProperty(String gameId, String playerName, String propertyName) {
        Game game = getGame(gameId);
        Player player = game.getPlayer(playerName);
        Property property = (Property) getTile(propertyName);

        if (!player.getName().equals(game.getCurrentPlayer())) {
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
        Game g = getGame(gameId);
        g.declareBankruptcy(playerName);
        return g;
    }

    @Override
    public boolean collectDebt(String gameId, String playerName, String propertyName, String debtorName) {
        Game g = getGame(gameId);
        Player pl = g.getPlayer(playerName);
        Player d = g.getPlayer(debtorName);
        Property pr = (Property) getTile(propertyName);
        pl.collectDebt(pr, d, g);
        return true;
    }

    @Override
    public int buyHouse(String gameId, String playerName, String propertyName) {
        Game g = getGame(gameId);
        Player p = g.getPlayer(playerName);
        Street s = (Street) getTile(propertyName);
        return p.buyHouseOrHotel(this, g, s);
    }

    @Override
    public int sellHouse(String gameId, String playerName, String propertyName) {
        Game g = getGame(gameId);
        Player p = g.getPlayer(playerName);
        Street s = (Street) getTile(propertyName);
        return p.sellHouseOrHotel(this, g, s);
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
        Game g = getGame(gameId);
        Player p = g.getPlayer(playerName);
        p.getOutOfJailFree();
    }

    @Override
    public void getOutOfJailFine(String gameId, String playerName) {
        Game g = getGame(gameId);
        Player p = g.getPlayer(playerName);
        p.getOutOfJailFine();
    }
}
