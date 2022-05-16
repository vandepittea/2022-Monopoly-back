package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import be.howest.ti.monopoly.logic.implementation.tile.*;
import be.howest.ti.monopoly.logic.implementation.turn.Turn;
import be.howest.ti.monopoly.logic.implementation.turn.TurnType;
import be.howest.ti.monopoly.web.views.PropertyView;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class Game {
    private static final Map<String, Integer> idCounter = new HashMap<>();

    private final int numberOfPlayers;
    private final String id;

    private boolean started;
    private List<Player> players;
    private String directSale;
    private int availableHouses;
    private int availableHotels;
    private List<Turn> turns;
    private DiceRoll lastDiceRoll;
    private boolean canRoll;
    private boolean ended;
    private Player currentPlayer;
    private Player winner;
    private Tile startingTile;

    private MonopolyService service;

    public Game(MonopolyService service, int numberOfPlayers, String prefix, Tile startingTile) {
        this.numberOfPlayers = numberOfPlayers;
        if (idCounter.containsKey(prefix)) {
            this.id = prefix + "_" + idCounter.get(prefix);
            idCounter.put(prefix, idCounter.get(prefix) + 1);
        } else {
            this.id = prefix + "_0";
            idCounter.put(prefix, 1);
        }
        this.started = false;
        this.players = new ArrayList<>();
        this.directSale = null;
        this.availableHouses = 32;
        this.availableHotels = 12;
        this.turns = new ArrayList<>();
        this.lastDiceRoll = new DiceRoll(0, 0);
        this.canRoll = true;
        this.ended = false;
        this.currentPlayer = null;
        this.winner = null;
        this.startingTile = startingTile;
        this.service = service;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    public String getId() {
        return this.id;
    }

    public boolean isStarted() {
        return this.started;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getDirectSale() {
        return directSale;
    }

    public int getAvailableHouses() {
        return availableHouses;
    }

    public int getAvailableHotels() {
        return availableHotels;
    }

    public void setAvailableHotels(int availableHotels) { /* for tests */
        this.availableHotels = availableHotels;
    }

    public void setAvailableHouses(int availableHouses) { /* for tests */
        this.availableHouses = availableHouses;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    public Integer[] getLastDiceRoll() {
        return lastDiceRoll.getRoll();
    }

    public boolean isCanRoll() {
        return canRoll;
    }

    public boolean isEnded() {
        return ended;
    }

    public String getCurrentPlayer() {
        if (currentPlayer == null) {
            return null;
        }
        return currentPlayer.getName();
    }

    @JsonIgnore
    public Player getCurrentplayerObject() {
        return currentPlayer;
    }

    public String getWinner() {
        return winner.getName();
    }

    public void setDirectSale(String directSale) {
        this.directSale = directSale;
    }

    public void setCurrentPlayer(String currentPlayerName) {
        Player currentPlayerObject = null;

        for (Player player : players) {
            if (player.getName().equals(currentPlayerName)) {
                currentPlayerObject = player;
                break;
            }
        }

        this.currentPlayer = currentPlayerObject;
    }

    public void joinGame(String playerName) {
        if (isExistedUser(playerName) || isStartedGame()) {
            throw new IllegalMonopolyActionException("You tried to do something which is against the " +
                    "rules of Monopoly. In this case, it is most likely that you tried to join a game which has " +
                    "already started, or you used a name that is already taken in this game.");
        } else {
            Player p = new Player(playerName, startingTile);
            addPlayer(p);
            changeStartedIfNeeded();
        }
    }

    private void addPlayer(Player p) {
        players.add(p);
    }

    private void changeStartedIfNeeded() {
        if (checkForReachingOfMaximumPlayers()) {
            started = true;
            currentPlayer = players.get(0);
        }
    }

    private boolean checkForReachingOfMaximumPlayers() {
        return players.size() >= numberOfPlayers;
    }

    private boolean isExistedUser(String playerName) {
        for (Player p : players) {
            if (p.getName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isStartedGame() {
        return this.isStarted();
    }

    public Player getPlayer(String playerName) {
        for (Player p : players) {
            if (p.getName().equals(playerName)) {
                return p;
            }
        }
        throw new MonopolyResourceNotFoundException("The player you are looking for do not exist. " +
                "Double check the name.");
    }

    public void handlePropertySale() {
        canRoll = true;
        directSale = null;
        changeCurrentPlayer(false);
    }

    public void rollDice(String playerName) {
        checkIllegalRollDiceActions(playerName);

        Turn turn = new Turn(currentPlayer);
        lastDiceRoll = turn.getRoll();

        if (currentPlayer.isJailed()) {
            checkRollInJail(turn);
        } else if (doesCurrentPlayerGetJailed()) {
            JailCurrentPlayer(turn);
        } else {
            movePlayer(turn, lastDiceRoll);
        }
        turns.add(turn);
    }

    private void checkRollInJail(Turn turn) {
        if (lastDiceRoll.isDoubleRoll()) {
            currentPlayer.getOutOfJail();
            movePlayer(turn, lastDiceRoll);
        } else {
            turn.addMove("Jail", "");
            turn.setType(TurnType.JAIL_STAY);
            changeCurrentPlayer(true);
        }
    }

    private void checkIllegalRollDiceActions(String playerName) {
        if (!started) {
            throw new IllegalMonopolyActionException("The game has not started yet.");
        }

        if (ended) {
            throw new IllegalMonopolyActionException("The game has already ended.");
        }

        if (!currentPlayer.getName().equals(playerName)) {
            throw new IllegalMonopolyActionException("It is not your turn.");
        }

        if (currentPlayer.getDebt() > 0) {
            throw new IllegalMonopolyActionException("The player is in debt.");
        }

        if (directSale != null) {
            throw new IllegalMonopolyActionException("The current player has to decide on a property.");
        }

        if (currentPlayer.isBankrupt()) {
            throw new IllegalMonopolyActionException("You are bankrupt. Rolling the dice isn't allowed.");
        }
    }

    private boolean doesCurrentPlayerGetJailed() {
        if (turns.size() >= 2) {
            Turn previousTurn = turns.get(turns.size() - 1);
            Turn beforePreviousTurn = turns.get(turns.size() - 2);

            if ((currentPlayer.getName().equals(previousTurn.getPlayer())) && (currentPlayer.getName().equals(beforePreviousTurn.getPlayer()))) {
                return lastDiceRoll.isDoubleRoll();
            }
        }
        return false;
    }

    private void JailCurrentPlayer(Turn turn) {
        Tile jail = service.getTile("Jail");
        currentPlayer.goToJail(jail);
        turn.setType(TurnType.GO_TO_JAIL);
        decideNextAction(jail, turn);
    }

    public void movePlayer(boolean passGo, Turn turn, Tile newTile) {
        List<Tile> tiles = service.getTiles();
        Tile currentPlayerTile = service.getTile(Tile.decideNameAsPathParameter(currentPlayer.getCurrentTile()));

        if (passGo && (newTile.getPosition() < currentPlayerTile.getPosition())) {
            currentPlayer.receiveMoney(200);
        }

        currentPlayer.moveTo(newTile);
        decideNextAction(newTile, turn);
    }

    private void movePlayer(Turn turn, DiceRoll roll) {
        List<Tile> tiles = service.getTiles();
        Tile currentPlayerTile = service.getTile(Tile.decideNameAsPathParameter(currentPlayer.getCurrentTile()));
        int nextTileIdx = currentPlayerTile.getPosition() + roll.getDie1() + roll.getDie2();
        if (nextTileIdx >= tiles.size()) {
            currentPlayer.receiveMoney(200);
            nextTileIdx -= tiles.size();
        }
        Tile newTile = service.getTile(nextTileIdx);
        currentPlayer.moveTo(newTile);

        turn.setType(TurnType.DEFAULT);

        decideNextAction(newTile, turn);
    }

    private void decideNextAction(Tile newTile, Turn turn) {
        switch (newTile.getActualType()) {
            case STREET:
                executeStreetFunctionality(newTile, turn);
                break;
            case GO_TO_JAIL:
                executeGoToJail(newTile, turn);
                break;
            case COMMUNITY_CHEST:
            case CHANCE:
                CardExecutingTile execTile = (CardExecutingTile) newTile;
                execTile.execute(service, this, turn);
                break;
            default:
                turn.addMove(newTile.getName(), "");
                changeCurrentPlayer(false);
                break;
        }
    }

    private void executeGoToJail(Tile newTile, Turn turn) {
        Tile jail = service.getTile("Jail");
        currentPlayer.goToJail(jail);
        turn.addMove(newTile.getName(), "");
        turn.addMove("Jail", "");
        changeCurrentPlayer(true);
    }

    private void executeStreetFunctionality(Tile newTile, Turn turn) {
        if (!propertyOwnedByOtherPlayer(newTile)) {
            directSale = newTile.getName();
            canRoll = false;
            turn.addMove(newTile.getName(), "Can buy this property in a direct sale");
            return;
        }
        turn.addMove(newTile.getName(), "Can be asked to pay rent if the property isn't mortgaged");
        changeCurrentPlayer(true);
    }

    private boolean propertyOwnedByOtherPlayer(Tile newTile) {
        for (Player player : players) {
            if (player.getName().equals(currentPlayer.getName())) {
                continue;
            }

            for (PropertyView property : player.getProperties()) {
                if (property.getPropertyObject().getName().equals(newTile.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void changeCurrentPlayer(boolean endTurn) {
        if (!endTurn && lastDiceRoll.isDoubleRoll()) {
            return;
        }

        int playerIdx = players.indexOf(currentPlayer);
        do {
            playerIdx++;
            if (playerIdx >= players.size()) {
                playerIdx = 0;
            }
            currentPlayer = players.get(playerIdx);
        } while (currentPlayer.isBankrupt());
    }

    public void declareBankruptcy(String playerName) {
        Player p = getPlayer(playerName);
        if (p.getCreditor() != null) {
            p.turnOverAssetsTo(p.getCreditor());
        } else {
            p.turnOverAssetsToBank();
        }

        p.becomeBankrupt();
        checkForWinner();
        changePlayerIfItsYourTurn(playerName);
    }

    private void checkForWinner() {
        int alivePlayers = 0;
        Player lastAlivePlayer = null;
        for(Player player: players){
            if(!player.isBankrupt()){
                alivePlayers++;
                lastAlivePlayer = player;
            }
        }
        if(alivePlayers == 1){
            ended = true;
            winner = lastAlivePlayer;
        }
    }

    private void changePlayerIfItsYourTurn(String playerName){
        if (currentPlayer.getName().equals(playerName)) {
            changeCurrentPlayer(true);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return numberOfPlayers == game.numberOfPlayers && id.equals(game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfPlayers, id);
    }
}
