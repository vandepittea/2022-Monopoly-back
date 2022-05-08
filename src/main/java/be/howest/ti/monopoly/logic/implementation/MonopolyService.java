package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.ServiceAdapter;
import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import be.howest.ti.monopoly.logic.implementation.tile.*;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

import java.util.ArrayList;
import java.util.List;

import java.util.HashSet;
import java.util.Set;


public class MonopolyService extends ServiceAdapter {
    private Set<Game> games;
    private final List<Tile> tiles;
    private final String[] chances;
    private final String[] communityChests;

    public MonopolyService() {
        games = new HashSet<>();

        chances = generateChances();

        communityChests = generateCommunityChests();

        tiles = new ArrayList<>();
        generateTiles();
    }

    private void generateTiles(){
        tiles.add(new SimpleTile(0, "Peach Castle", TileType.Go));
        tiles.add(new Street(1, "Peach's Garden", 60, 30, 2, "PURPLE", new Integer[]{10, 30, 90, 160, 250}, 50, "PURPLE", 2));
        tiles.add(new CardExecutingTile(2, "Community Chest I", TileType.community_chest));
        tiles.add(new Street(3, "Yoshi Valley", 60, 30, 2, "PURPLE", new Integer[]{20, 60, 180, 320, 450}, 50, "PURPLE", 4));
        tiles.add(new CardExecutingTile(4, "Bowser Jr.", TileType.Tax_Income));
        tiles.add(new Railroad(5, "Fire Flower", 200, 100, 4, "BLACK", 25));
        tiles.add(new Street(6, "Pokey Pyramid", 100, 50, 3, "LIGHTBLUE", new Integer[]{30, 90, 270, 400, 550}, 50, "LIGHTBLUE", 6));
        tiles.add(new CardExecutingTile(7, "Chance I", TileType.chance));
        tiles.add(new Street(8, "Smoldering Sands", 100, 50, 3, "LIGHTBLUE", new Integer[]{30, 90, 270, 400, 550}, 50, "LIGHTBLUE", 6));
        tiles.add(new Street(9, "Cheep-Cheep Oasis", 120, 60, 3, "LIGHTBLUE", new Integer[]{40, 100, 300, 450, 600}, 50, "LIGHTBLUE", 8));
        tiles.add(new SimpleTile(10, "Jail", TileType.Jail));
        tiles.add(new Street(11, "Delfino Airship", 140, 70, 3, "VIOLET", new Integer[]{50, 150, 450, 625, 750}, 100, "VIOLET", 10));
        tiles.add(new Utility(12, "Electric Koopa Farm", 150, 75, 2, "WHITE", "4 or 10 times the dice roll"));
        tiles.add(new Street(13, "Delfino Plaza", 140, 70, 3, "VIOLET", new Integer[]{50, 150, 450, 625, 750}, 100, "VIOLET", 10));
        tiles.add(new Street(14, "MT Corona", 160, 80, 3, "VIOLET", new Integer[]{60, 180, 500, 700, 900}, 100, "VIOLET", 12));
        tiles.add(new Railroad(15, "F.L.U.D.D.", 200, 100, 4, "BLACK", 25));
        tiles.add(new Street(16, "Sirena Beach", 180, 90, 3, "ORANGE", new Integer[]{70, 200, 550, 750, 950}, 100, "ORANGE", 14));
        tiles.add(new CardExecutingTile(17, "Community Chest II", TileType.community_chest));
        tiles.add(new Street(18, "Pinna Park", 180, 90, 3, "ORANGE", new Integer[]{70, 200, 550, 750, 950}, 100, "ORANGE", 14));
        tiles.add(new Street(19, "Noki Bay", 200, 100, 3, "ORANGE", new Integer[]{80, 220, 600, 800, 1000}, 100, "ORANGE", 16));
        tiles.add(new SimpleTile(20, "Free Parking", TileType.Free_Parking));
        tiles.add(new Street(21, "Maple Treeway", 220, 110, 3, "RED", new Integer[]{90, 250, 700, 875, 1050}, 150, "RED", 18));
        tiles.add(new CardExecutingTile(22, "Chance II", TileType.chance));
        tiles.add(new Street(23, "DK Summit", 220, 110, 3, "RED", new Integer[]{90, 250, 700, 875, 1050}, 150, "RED", 18));
        tiles.add(new Street(24, "Sherbet Land", 240, 120, 3, "RED", new Integer[]{100, 300, 750, 925, 1100}, 150, "RED", 20));
        tiles.add(new Railroad(25, "Mario Cart", 200, 100, 4, "Black", 25));
        tiles.add(new Street(26, "DK Mountain", 260, 130, 3, "YELLOW", new Integer[]{110, 330, 800, 975, 1150}, 150, "YELLOW", 22));
        tiles.add(new Street(27, "Wario's Goldmine", 260, 130, 3, "YELLOW", new Integer[]{110, 330, 800, 975, 1150,}, 150, "YELLOW", 22));
        tiles.add(new Utility(28, "Gas Puump", 150, 75, 2, "WHITE", "4 or 10 times the dice roll"));
        tiles.add(new Street(29, "Grumble Volcano", 280, 140, 3, "YELLOW", new Integer[]{120, 360, 850, 1025, 1200}, 150, "YELLOW", 24));
        tiles.add(new SimpleTile(30, "Go to Jail", TileType.Go_to_Jail));
        tiles.add(new Street(31, "Steam Gardens", 300, 150, 3, "DARKGREEN", new Integer[]{130, 390, 900, 1100, 1275}, 200, "DARKGREEN", 26));
        tiles.add(new Street(32, "Honeylune Ridge", 300, 150, 3, "DARKGREEN", new Integer[]{130, 390, 900, 1100, 1275}, 200, "DARKGREEN", 26));
        tiles.add(new CardExecutingTile(33, "Community Chest III", TileType.community_chest));
        tiles.add(new Street(34, "Nimbus Arena", 320, 160, 3, "DARKGREEN", new Integer[]{150, 450, 1000, 1200, 1400}, 200, "DARKGREEN", 28));
        tiles.add(new Railroad(35, "Cappy", 200, 100, 4, "BLACK", 25));
        tiles.add(new CardExecutingTile(36, "Chance III", TileType.chance));
        tiles.add(new Street(37, "Crumbleden", 350, 175, 2, "DARKBLUE", new Integer[]{175, 500, 1100, 1300, 1500}, 200, "DARKGREEN", 35));
        tiles.add(new CardExecutingTile(38, "Luxury Tax", TileType.Luxury_Tax));
        tiles.add(new Street(39, "Bowser Castle", 400, 200, 2, "DARKBLUE", new Integer[]{200, 600, 1400, 1700, 2000}, 200, "DARKBLUE", 50));
    }

    private String[] generateChances(){
        return new String[]{ "Advance to Boardwalk",
                "Advance to Go (Collect $200)",
                "Advance to Illinois Avenue. If you pass Go, collect $200",
                "Advance to St. Charles Place. If you pass Go, collect $200",
                "Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay owner twice the rental to which they are otherwise entitled",
                "Advance token to nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total ten times amount thrown.",
                "Bank pays you dividend of $50",
                "Get Out of Jail Free",
                "Go Back 3 Spaces",
                "Go to Jail. Go directly to Jail, do not pass Go, do not collect $200",
                "Make general repairs on all your property. For each house pay $25. For each hotel pay $100",
                "Speeding fine $15",
                "Take a trip to Reading Railroad. If you pass Go, collect $200",
                "You have been elected Chairman of the Board. Pay each player $50",
                "Your building loan matures. Collect $150"
        };
    }

    private String[] generateCommunityChests(){
        return new String[]{ "Advance to Go (Collect $200)",
                "Bank error in your favor. Collect $200",
                "Doctor's fee. Pay $50",
                "From sale of stock you get $50",
                "Get Out of Jail Free",
                "Go to Jail. Go directly to jail, do not pass Go, do not collect $200",
                "Holiday fund matures. Receive $100",
                "Income tax refund. Collect $20",
                "It is your birthday. Collect $10 from every player",
                "Life insurance matures. Collect $100",
                "Pay hospital fees of $100 ",
                "Pay school fees of $50",
                "Receive $25 consultancy fee ",
                "You are assessed for street repair. $40 per house. $115 per hotel",
                "You have won second prize in a beauty contest. Collect $10",
                "You inherit $100"
        };
    }

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public List<Tile> getTiles(){
        return tiles;
    }

    @Override
    public String[] getChance(){
        return chances;
    }

    @Override
    public String[] getCommunityChest(){
        return communityChests;
    }

    @Override
    public Tile getTile(int position){
        for(Tile tile: getTiles()){
            if(tile.getPosition() == position){
                return tile;
            }
        }
        throw new MonopolyResourceNotFoundException("No such tile, index out of bounds.");
    }

    @Override
    public Tile getTile(String name){
        for(Tile tile: getTiles()){
            if(tile.getNameAsPathParameter().equals(name)){
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
    public void joinGame(String gameId, String playerName){
        Game g = getGame(gameId);

        g.joinGame(playerName);
    }

    @Override
    public String buyProperty(String gameId, String playerName, String propertyName){
        Game g = getGame(gameId);
        Player pl = g.getPlayer(playerName);
        Tile t = getTile(propertyName);
        Property pr = (Property) t;

        if(playerName.equals(g.getCurrentPlayer()) && t.getName().equals(g.getDirectSale())) {
            pl.buyProperty(pr);
            g.handlePropertySale();
            return pr.getName();
        }
        else {
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
    public Game declareBankruptcy(String gameId, String playerName){
        Game g = getGame(gameId);
        g.declareBankruptcy(playerName);
        return g;
    }

    @Override
    public boolean collectDebt(String gameId, String playerName, String propertyName, String debtorName){
        Game g = getGame(gameId);
        String descriptionLastRoll = g.getTurns().get(g.getTurns().size() - 1).getMoves().get(0).getDescription();
        Player pl = g.getPlayer(playerName);
        Player d = g.getPlayer(debtorName);
        Property pr = (Property) getTile(propertyName);
        pl.collectDebt(pr, d, descriptionLastRoll);
        return true;
    }
}
