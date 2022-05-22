package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import be.howest.ti.monopoly.logic.implementation.tile.*;
import be.howest.ti.monopoly.logic.implementation.turn.DiceRoll;
import be.howest.ti.monopoly.logic.implementation.turn.Turn;
import be.howest.ti.monopoly.logic.implementation.enums.TurnType;
import be.howest.ti.monopoly.logic.implementation.tile.OwnedProperty;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private static final Map<String, Integer> idCounter = new HashMap<>();

    private final int numberOfPlayers;
    private final String id;
    private final String gameName;
    private final Tile startingTile;
    private final MonopolyService service;
    private final List<Turn> turns;
    private final List<Player> players;

    private boolean started;
    private boolean canRoll;
    private boolean ended;
    private int availableHouses;
    private int availableHotels;
    private String directSale;
    private DiceRoll lastDiceRoll;
    private Player currentPlayer;
    private Player winner;

    public Game(MonopolyService service, int numberOfPlayers, String prefix, Tile startingTile, String gameName) {
        this.started = false;
        this.canRoll = true;
        this.ended = false;

        this.availableHouses = 32;
        this.availableHotels = 12;
        this.numberOfPlayers = numberOfPlayers;

        this.directSale = null;
        this.lastDiceRoll = new DiceRoll(0, 0);
        this.currentPlayer = null;
        this.winner = null;
        this.startingTile = startingTile;
        this.service = service;

        this.turns = new ArrayList<>();
        this.players = new ArrayList<>();

        this.gameName = gameName;
        this.id = generateId(prefix);
    }

    private String generateId(String prefix) {
        final String idToGenerate;

        if (idCounter.containsKey(prefix)) {
            idToGenerate = prefix + "_" + idCounter.get(prefix);
            idCounter.put(prefix, idCounter.get(prefix) + 1);
        } else {
            idToGenerate = prefix + "_0";
            idCounter.put(prefix, 1);
        }

        return idToGenerate;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    public String getId() {
        return this.id;
    }

    public String getGameName() {
        return gameName;
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

    @JsonIdentityReference(alwaysAsId = true)
    public DiceRoll getLastDiceRoll() {
        return lastDiceRoll;
    }

    public boolean isCanRoll() {
        return canRoll;
    }

    public boolean isEnded() {
        return ended;
    }

    @JsonIdentityReference(alwaysAsId = true)
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @JsonIdentityReference(alwaysAsId = true)
    public Player getWinner() {
        return winner;
    }

    public void setDirectSale(String directSale) {
        this.directSale = directSale;
    }

    public void setCurrentPlayer(String currentPlayerName) {
        for (Player player : players) {
            if (player.getName().equals(currentPlayerName)) {
                this.currentPlayer = player;
                return;
            }
        }
    }

    public void joinGame(String playerName) {
        if (isExistingUser(playerName)) {
            throw new IllegalMonopolyActionException(playerName + " already exists in this game.");
        }
        if (started) {
            throw new IllegalMonopolyActionException("This game is already started.");
        }

        Player player = new Player(playerName, startingTile);
        players.add(player);
    }

    public void changeStartedIfNeeded() {
        if (checkForReachingOfMaximumPlayers() && checkIfPlayersChosePawns()) {
            started = true;
            currentPlayer = players.get(0);
        }
    }

    private boolean checkForReachingOfMaximumPlayers() {
        return players.size() >= numberOfPlayers;
    }

    private boolean checkIfPlayersChosePawns() {
        for (Player player : players) {
            if (player.getPawn() == null) {
                return false;
            }
        }
        return true;
    }

    private boolean isExistingUser(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    public Player getPlayer(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }

        throw new MonopolyResourceNotFoundException(playerName + " does not exist in this game.");
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
        } else if (checkLastTwoPlayerTurns(turns, true)) {
            jailCurrentPlayer(turn);
        } else {
            movePlayer(turn, lastDiceRoll);
        }

        turns.add(turn);
    }

    private void checkRollInJail(Turn turn) {
        if (lastDiceRoll.isDoubleRoll()) {
            currentPlayer.getOutOfJail();
            turn.setType(TurnType.GET_OUT_OF_JAIL);
            movePlayer(turn, lastDiceRoll);
        } else {
            List<Turn> currentPlayerTurns = getCurrentPlayerTurns();

            if (checkLastTwoPlayerTurns(currentPlayerTurns, false)) {
                currentPlayer.getOutOfJailFine();
                turn.setType(TurnType.GET_OUT_OF_JAIL);
                movePlayer(turn, lastDiceRoll);
                changeCurrentPlayer(true);
            } else {
                turn.addMove(service.getTile("Jail"), currentPlayer.getName() + " is still in jail.");
                turn.setType(TurnType.JAIL_STAY);
                changeCurrentPlayer(true);
            }
        }
    }

    @JsonIgnore
    private List<Turn> getCurrentPlayerTurns() {
        return turns.stream().filter(turn ->
                turn.getPlayer().equals(currentPlayer.getName())).collect(Collectors.toList());
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
            throw new IllegalMonopolyActionException("You are in debt. Earn money or declare bankruptcy");
        }
        if (directSale != null) {
            throw new IllegalMonopolyActionException("You have to decide on a property.");
        }
        if (currentPlayer.isBankrupt()) {
            throw new IllegalMonopolyActionException("You are bankrupt. Rolling the dice isn't allowed.");
        }
    }

    private boolean checkLastTwoPlayerTurns(List<Turn> playerTurns, boolean checkForDoubleRoll) {
        if (playerTurns.size() >= 2) {
            Turn previousTurn = playerTurns.get(playerTurns.size() - 1);
            Turn beforePreviousTurn = playerTurns.get(playerTurns.size() - 2);

            if ((currentPlayer.getName().equals(previousTurn.getPlayer())) &&
                    (currentPlayer.getName().equals(beforePreviousTurn.getPlayer()))) {
                if (!checkForDoubleRoll){
                    return true;
                }
                return lastDiceRoll.isDoubleRoll();
            }
        }

        return false;
    }

    public void jailCurrentPlayer(Turn turn) {
        Tile jail = service.getTile("Jail");
        currentPlayer.goToJail(jail);
        turn.setType(TurnType.GO_TO_JAIL);
        decideNextAction(jail, turn);
    }

    public void movePlayer(boolean passGo, Turn turn, Tile newTile) {
        Tile currentPlayerTile = currentPlayer.getCurrentTile();

        if (passGo && (newTile.getPosition() < currentPlayerTile.getPosition())) {
            currentPlayer.receiveMoney(200);
            turn.addMove(service.getTile(0), currentPlayer.getName() +
                    " passed go and received 200 coins!");
        }

        currentPlayer.moveTo(newTile);
        decideNextAction(newTile, turn);
    }

    private void movePlayer(Turn turn, DiceRoll roll) {
        List<Tile> tiles = service.getTiles();
        Tile currentPlayerTile = currentPlayer.getCurrentTile();
        int nextTileIdx = currentPlayerTile.getPosition() + roll.getDie1() + roll.getDie2();

        if (nextTileIdx >= tiles.size()) {
            currentPlayer.receiveMoney(200);
            nextTileIdx -= tiles.size();
            turn.addMove(service.getTile(0), currentPlayer.getName() +
                    " passed go and received 200 coins!");
        }

        Tile newTile = service.getTile(nextTileIdx);
        currentPlayer.moveTo(newTile);

        turn.setType(TurnType.DEFAULT);
        decideNextAction(newTile, turn);
    }

    private void decideNextAction(Tile newTile, Turn turn) {
        switch (newTile.getType()) {
            case UTILITY:
            case RAILROAD:
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
            case TAX_INCOME:
            case LUXURY_TAX:
                turn.addMove(newTile, currentPlayer.getName() + " has to pay taxes.");
                currentPlayer.payTaxes();
                changeCurrentPlayer(false);
                break;
            case FREE_PARKING:
                turn.addMove(newTile, currentPlayer.getName() + " passed this tile.");
                break;
            case JAIL:
                turn.addMove(newTile, currentPlayer.getName() + " is visiting the jail.");
                break;
            default:
                turn.addMove(newTile, "");
                changeCurrentPlayer(false);
                break;
        }
    }

    private void executeGoToJail(Tile newTile, Turn turn) {
        Tile jail = service.getTile("Jail");

        currentPlayer.goToJail(jail);
        turn.addMove(newTile, currentPlayer.getName() + " has to go to jail.");
        turn.addMove(service.getTile("Jail"), currentPlayer.getName() + " is in jail.");

        changeCurrentPlayer(true);
    }

    private void executeStreetFunctionality(Tile newTile, Turn turn) {
        if (!propertyOwnedByAPlayer(newTile)) {
            directSale = newTile.getName();
            canRoll = false;
            turn.addMove(newTile, currentPlayer.getName() + " can buy this property in a direct sale.");
            return;
        }

        turn.addMove(newTile,  currentPlayer.getName() + " can't buy this property.");
        changeCurrentPlayer(true);
    }

    private boolean propertyOwnedByAPlayer(Tile newTile) {
        for (Player player : players) {
            for (OwnedProperty property : player.getProperties()) {
                if (property.getProperty().getName().equals(newTile.getName())) {
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
        Player player = getPlayer(playerName);

        player.becomeBankrupt();
        checkForWinner();
        changePlayerIfItsYourTurn(playerName);
    }

    private void checkForWinner() {
        int alivePlayers = 0;
        Player lastAlivePlayer = null;

        for (Player player : players) {
            if (!player.isBankrupt()) {
                alivePlayers++;
                lastAlivePlayer = player;
            }
        }

        if (alivePlayers == 1) {
            ended = true;
            winner = lastAlivePlayer;
        }
    }

    private void changePlayerIfItsYourTurn(String playerName) {
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
