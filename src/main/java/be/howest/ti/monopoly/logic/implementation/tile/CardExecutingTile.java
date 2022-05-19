package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.*;
import be.howest.ti.monopoly.logic.implementation.enums.*;
import be.howest.ti.monopoly.logic.implementation.generator.Generator;
import be.howest.ti.monopoly.logic.implementation.turn.Turn;
import be.howest.ti.monopoly.web.views.PropertyView;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.security.SecureRandom;
import java.util.*;

public class CardExecutingTile extends Tile {
    private static final SecureRandom random = new SecureRandom();
    private static final Map<ChanceCard, String> chances = Generator.generateChances();
    private static final Map<CommunityChest, String> communityChests = Generator.generateCommunityChests();
    private static final Map<PowerUp, Integer> powerUpLocations = Generator.generatePowerUpLocations();
    private static final Map<UtilityEnum, Integer> utilityLocations = Generator.generateUtilityLocations();
    private static final Map<TilesToAdvance, Integer> tilesToAdvance = Generator.generateTilesToAdvance();

    public CardExecutingTile(int position, String name, TileType type) {
        super(position, name, type);
    }

    @JsonIgnore
    public static List<String> getChances() {
        return new ArrayList<>(chances.values());
    }

    @JsonIgnore
    public static List<String> getCommunityChests() {
        return new ArrayList<>(communityChests.values());
    }

    @JsonIgnore
    public static ChanceCard getChanceType(String message) {
        for (Map.Entry<ChanceCard, String> entry : chances.entrySet()) {
            if (entry.getValue().equals(message)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @JsonIgnore
    public static CommunityChest getCommunityType(String message) {
        for (Map.Entry<CommunityChest, String> entry : communityChests.entrySet()) {
            if (entry.getValue().equals(message)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void execute(MonopolyService service, Game game, Turn turn) {
        switch (type) {
            case COMMUNITY_CHEST:
                executeRandomCommunityChest(service, game, turn);
                break;
            case CHANCE:
                executeRandomChance(service, game, turn);
                break;
            default:
                break;
        }
    }

    private void executeRandomChance(MonopolyService service, Game game, Turn turn) {
        int randomChance = random.nextInt(chances.size());
        ChanceCard type = ChanceCard.values()[randomChance];
        turn.addMove(getName(), chances.get(type));

        switch (type) {
            case ADV_BOWSER_CASTLE:
                goToTile(service, tilesToAdvance.get(TilesToAdvance.BOWSER_CASTLE), turn, game, false);
                break;
            case ADV_GO:
                goToTile(service, tilesToAdvance.get(TilesToAdvance.GO), turn, game, true);
                break;
            case ADV_SHERBET:
                goToTile(service, tilesToAdvance.get(TilesToAdvance.SHERBET), turn, game, true);
                break;
            case ADV_DELFINO:
                goToTile(service, tilesToAdvance.get(TilesToAdvance.DELFINO), turn, game, true);
                break;
            case ADV_POWERUP:
                advanceToNextPowerUp(service, game, turn);
                break;
            case ADV_UT:
                advanceToNextUtility(service, game, turn);
                break;
            case REC_BANK_50:
                game.getCurrentPlayer().receiveMoney(50);
                game.changeCurrentPlayer(false);
                break;
            case GET_JAIL_CARD:
                game.getCurrentPlayer().receiveGetOutOfJailCard();
                game.changeCurrentPlayer(false);
                break;
            case RETURN_3:
                returnThreeTiles(service, game, turn);
                break;
            case GO_JAIL:
                goToTile(service, tilesToAdvance.get(TilesToAdvance.JAIL), turn, game, false);
                break;
            case PAY_REPAIRS:
                payRepairs(game, 25, 100);
                break;
            case PAY_15:
                game.getCurrentPlayer().payDebt(15, null);
                game.changeCurrentPlayer(false);
                break;
            case GO_FLOWER:
                goToTile(service, powerUpLocations.get(PowerUp.FIRE_FLOWER), turn, game, true);
                break;
            case PAY_PLAYERS_50:
                payAllPlayers(game, 50);
                break;
            case REC_150:
                game.getCurrentPlayer().receiveMoney(150);
                game.changeCurrentPlayer(false);
                break;
            default:
                break;
        }
    }

    private void executeRandomCommunityChest(MonopolyService service, Game game, Turn turn) {
        int randomCommunityChest = random.nextInt(communityChests.size());
        CommunityChest type = CommunityChest.values()[randomCommunityChest];
        turn.addMove(getName(), communityChests.get(type));

        switch (type) {
            case ADV_GO:
                goToTile(service, tilesToAdvance.get(TilesToAdvance.GO), turn, game, true);
                break;
            case REC_BANK_ERR:
                game.getCurrentPlayer().receiveMoney(200);
                game.changeCurrentPlayer(false);
                break;
            case PAY_DOCTOR_FEE:
            case PAY_STAR:
                game.getCurrentPlayer().payDebt(50, null);
                game.changeCurrentPlayer(false);
                break;
            case REC_SOLD_STOCK:
                game.getCurrentPlayer().receiveMoney(50);
                game.changeCurrentPlayer(false);
                break;
            case GET_JAIL_CARD:
                game.getCurrentPlayer().receiveGetOutOfJailCard();
                game.changeCurrentPlayer(false);
                break;
            case GO_JAIL:
                goToTile(service, tilesToAdvance.get(TilesToAdvance.JAIL), turn, game, false);
                break;
            case REC_DELFINO:
            case REC_KOOPALING:
            case REC_ROSALINA:
                game.getCurrentPlayer().receiveMoney(100);
                game.changeCurrentPlayer(false);
                break;
            case REC_BANK_OWES:
                game.getCurrentPlayer().receiveMoney(20);
                game.changeCurrentPlayer(false);
                break;
            case REC_BIRTHDAY:
                receiveFromAllPlayers(game, 10);
                break;
            case PAY_BOO:
                game.getCurrentPlayer().payDebt(100, null);
                game.changeCurrentPlayer(false);
                break;
            case REC_FRUIT:
                game.getCurrentPlayer().receiveMoney(25);
                game.changeCurrentPlayer(false);
                break;
            case PAY_REPAIRS:
                payRepairs(game, 40, 115);
                break;
            case REC_PRIZE:
                game.getCurrentPlayer().receiveMoney(10);
                game.changeCurrentPlayer(false);
                break;
            default:
                break;
        }
    }

    private void goToTile(MonopolyService service, int tileToAdvance, Turn turn, Game game, boolean passGo) {
        Tile newTile = service.getTile(tileToAdvance);
        game.movePlayer(passGo, turn, newTile);
    }

    private void receiveFromAllPlayers(Game game, int amount) {
        Player currentPlayer = game.getCurrentPlayer();

        for (Player player : game.getPlayers()) {
            if (player.equals(currentPlayer)) {
                continue;
            }
            player.payDebt(amount, currentPlayer);
        }

        game.changeCurrentPlayer(false);
    }

    private void advanceToNextUtility(MonopolyService service, Game game, Turn turn) {
        for (Integer utilityPosition : utilityLocations.values()) {
            if (getPosition() < utilityPosition) {
                goToTile(service, utilityPosition, turn, game, false);
                return;
            }
        }

        goToTile(service, utilityLocations.get(UtilityEnum.ELECTRIC_KOOPA_FARM), turn, game, false);
    }

    private void advanceToNextPowerUp(MonopolyService service, Game game, Turn turn) {
        for (Integer powerUpPosition : powerUpLocations.values()) {
            if (getPosition() < powerUpPosition) {
                goToTile(service, powerUpPosition, turn, game, false);
                return;
            }
        }

        goToTile(service, powerUpLocations.get(PowerUp.FIRE_FLOWER), turn, game, false);
    }

    private void payAllPlayers(Game game, int amount) {
        Player currentPlayer = game.getCurrentPlayer();

        for (Player player : game.getPlayers()) {
            if (!player.equals(currentPlayer)) {
                currentPlayer.payDebt(amount, player);
            }
        }

        game.changeCurrentPlayer(false);
    }

    private void payRepairs(Game game, int houseRepairCost, int hotelRepairCost) {
        int cost = 0;

        Set<PropertyView> properties = game.getCurrentPlayer().getProperties();
        for (PropertyView propertyView : properties) {
            if (propertyView.getProperty().type == TileType.STREET){
                cost += propertyView.getHouseCount() * houseRepairCost;
                cost += propertyView.getHotelCount() * hotelRepairCost;
            }
        }

        game.getCurrentPlayer().payDebt(cost, null);
        game.changeCurrentPlayer(false);
    }

    private void returnThreeTiles(MonopolyService service, Game game, Turn turn) {
        int currentTileIdx = game.getCurrentPlayer().getCurrentTile().getPosition();
        currentTileIdx -= 3;

        if (currentTileIdx < 0) {
            currentTileIdx = service.getTiles().size() - currentTileIdx;
        }

        goToTile(service, currentTileIdx, turn, game, false);
    }
}
