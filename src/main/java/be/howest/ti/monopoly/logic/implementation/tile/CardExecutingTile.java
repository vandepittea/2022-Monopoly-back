package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.*;
import be.howest.ti.monopoly.logic.implementation.enums.*;
import be.howest.ti.monopoly.logic.implementation.enums.Utility;
import be.howest.ti.monopoly.logic.implementation.generator.Generator;
import be.howest.ti.monopoly.logic.implementation.turn.Turn;
import be.howest.ti.monopoly.web.views.PropertyView;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.security.SecureRandom;
import java.util.*;

public class CardExecutingTile extends Tile {
    private static final Map<ChanceCard, String> chances = Generator.generateChances();
    private static final Map<CommunityChest, String> communityChests = Generator.generateCommunityChests();
    private static final Map<PowerUp, Integer> powerUpLocations = Generator.generatePowerUpLocations();
    private static final Map<Utility, Integer> utilityLocations = Generator.generateUtilityLocations();
    private static final Map<TilesToAdvance, Integer> tilesToAdvance = Generator.generateTilesToAdvance();
    private static final SecureRandom random = new SecureRandom();

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
                goToTile(service, tilesToAdvance.get(TilesToAdvance.BOWSER_CASTLE), turn, type, game, false);
                break;
            case ADV_GO:
                goToTile(service, tilesToAdvance.get(TilesToAdvance.GO), turn, type, game, true);
                break;
            case ADV_SHERBET:
                goToTile(service, tilesToAdvance.get(TilesToAdvance.SHERBET), turn, type, game, true);
                break;
            case ADV_DELFINO:
                goToTile(service, tilesToAdvance.get(TilesToAdvance.DELFINO), turn, type, game, true);
                break;
            case ADV_POWERUP:
                boolean moved = false;
                for (Integer powerUpPosition : powerUpLocations.values()) {
                    if (getPosition() < powerUpPosition) {
                        goToTile(service, powerUpPosition, turn, type, game, false);
                        moved = true;
                        break;
                    }
                }
                if (!moved) {
                    goToTile(service, powerUpLocations.get(PowerUp.FIRE_FLOWER), turn, type, game, false);
                }
                break;
            case ADV_UT:
                for (Integer utilityPosition : utilityLocations.values()) {
                    if (getPosition() < utilityPosition) {
                        goToTile(service, utilityPosition, turn, type, game, false);
                    }
                }
                goToTile(service, utilityLocations.get(Utility.ELECTRIC_KOOPA_FARM), turn, type, game, false);
                break;
            case REC_BANK_50:
                game.getCurrentplayerObject().receiveMoney(50);
                game.changeCurrentPlayer(false);
                break;
            case GET_JAIL_CARD:
                game.getCurrentplayerObject().receiveGetOutOfJailCard();
                game.changeCurrentPlayer(false);
                break;
            case RETURN_3:
                int currentTileIdx = game.getCurrentplayerObject().getCurrentTileObject().getPosition();
                currentTileIdx -= 3;
                if (currentTileIdx < 0) {
                    currentTileIdx = service.getTiles().size() - currentTileIdx;
                }
                goToTile(service, currentTileIdx, turn, type, game, false);
                break;
            case GO_JAIL:
                goToTile(service, tilesToAdvance.get(TilesToAdvance.JAIL), turn, type, game, false);
                break;
            case PAY_REPAIRS:
                int cost = 0;
                Set<PropertyView> properties = game.getCurrentplayerObject().getProperties();
                for (PropertyView propertyView : properties) {
                    if (propertyView.getPropertyObject().type == TileType.STREET){
                        cost += propertyView.getHouseCount() * 25;
                        cost += propertyView.getHotelCount() * 100;
                    }
                }
                game.getCurrentplayerObject().payDebt(cost, null);
                game.changeCurrentPlayer(false);
                break;
            case PAY_15:
                game.getCurrentplayerObject().payDebt(15, null);
                game.changeCurrentPlayer(false);
                break;
            case GO_FLOWER:
                goToTile(service, powerUpLocations.get(PowerUp.FIRE_FLOWER), turn, type, game, true);
                break;
            case PAY_PLAYERS_50:
                Player currentPlayer = game.getCurrentplayerObject();
                for (Player player : game.getPlayers()) {
                    if (player.equals(currentPlayer)) {
                        continue;
                    }
                    currentPlayer.payDebt(50, player);
                    player.receiveMoney(50);
                }
                game.changeCurrentPlayer(false);
                break;
            case REC_150:
                game.getCurrentplayerObject().receiveMoney(150);
                game.changeCurrentPlayer(false);
                break;
        }
    }

    private void goToTile(MonopolyService service, int tileToAdvance, Turn turn, ChanceCard chanceType, Game game, boolean passGo) {
        Tile newTile = service.getTile(tileToAdvance);
        turn.addMove(this.getName(), chances.get(chanceType));
        game.movePlayer(passGo, turn, newTile);
    }

    private void executeRandomCommunityChest(MonopolyService service, Game game, Turn turn) {
        int randomCommunityChest = random.nextInt(communityChests.size());

        CommunityChest type = CommunityChest.values()[randomCommunityChest];
        turn.addMove(getName(), communityChests.get(type));
        switch (type) {
            case ADV_GO:
                goToTile(service, tilesToAdvance.get(TilesToAdvance.GO), turn, type, game, true);
                break;
            case REC_BANK_ERR:
                game.getCurrentplayerObject().receiveMoney(200);
                game.changeCurrentPlayer(false);
                break;
            case PAY_DOCTOR_FEE:
            case PAY_STAR:
                game.getCurrentplayerObject().payDebt(50, null);
                game.changeCurrentPlayer(false);
                break;
            case REC_SOLD_STOCK:
                game.getCurrentplayerObject().receiveMoney(50);
                game.changeCurrentPlayer(false);
                break;
            case GET_JAIL_CARD:
                game.getCurrentplayerObject().receiveGetOutOfJailCard();
                game.changeCurrentPlayer(false);
                break;
            case GO_JAIL:
                goToTile(service, tilesToAdvance.get(TilesToAdvance.JAIL), turn, type, game, false);
                break;
            case REC_DELFINO:
            case REC_KOOPALING:
            case REC_ROSALINA:
                game.getCurrentplayerObject().receiveMoney(100);
                game.changeCurrentPlayer(false);
                break;
            case REC_BANK_OWES:
                game.getCurrentplayerObject().receiveMoney(20);
                game.changeCurrentPlayer(false);
                break;
            case REC_BIRTHDAY:
                Player currentPlayer = game.getCurrentplayerObject();
                for (Player player : game.getPlayers()) {
                    if (player.equals(currentPlayer)) {
                        continue;
                    }
                    player.payDebt(10, currentPlayer);
                    currentPlayer.receiveMoney(10);
                }

                game.changeCurrentPlayer(false);
                break;
            case PAY_BOO:
                game.getCurrentplayerObject().payDebt(100, null);
                game.changeCurrentPlayer(false);
                break;
            case REC_FRUIT:
                game.getCurrentplayerObject().receiveMoney(25);
                game.changeCurrentPlayer(false);
                break;
            case PAY_REPAIRS:
                int cost = 0;
                Set<PropertyView> properties = game.getCurrentplayerObject().getProperties();
                for (PropertyView propertyView : properties) {
                    if (propertyView.getPropertyObject().type == TileType.STREET){
                        cost += propertyView.getHouseCount() * 40;
                        cost += propertyView.getHotelCount() * 115;
                    }
                }
                game.getCurrentplayerObject().payDebt(cost, null);
                game.changeCurrentPlayer(false);
                break;
            case REC_PRIZE:
                game.getCurrentplayerObject().receiveMoney(10);
                game.changeCurrentPlayer(false);
                break;
        }
    }
    private void goToTile(MonopolyService service, int tileToAdvance, Turn turn, CommunityChest chanceType, Game game, boolean passGo) {
        Tile newTile = service.getTile(tileToAdvance);
        turn.addMove(this.getName(), communityChests.get(chanceType));
        game.movePlayer(passGo, turn, newTile);
    }
}
