package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.*;
import be.howest.ti.monopoly.logic.implementation.enums.ChanceCards;
import be.howest.ti.monopoly.logic.implementation.enums.CommunityChests;
import be.howest.ti.monopoly.logic.implementation.enums.TileType;
import be.howest.ti.monopoly.logic.implementation.generator.Generator;
import be.howest.ti.monopoly.logic.implementation.turn.Turn;
import be.howest.ti.monopoly.web.views.PropertyView;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.security.SecureRandom;
import java.util.*;

public class CardExecutingTile extends Tile {
    private static final Map<ChanceCards, String> chances = Generator.generateChances();
    private static final Map<CommunityChests, String> communityChests = Generator.generateCommunityChests();
    private static final List<Integer> powerupLocations = new ArrayList<>();
    private static final List<Integer> utilityLocations = new ArrayList<>();
    private static final SecureRandom random = new SecureRandom();

    public CardExecutingTile(int position, String name, TileType type) {
        super(position, name, type);

        if (chances.size() == 0) {
            generatePowerupLocations();
            generateUtilityLocations();
        }
    }

    private void generateUtilityLocations() {
        utilityLocations.add(12);
        utilityLocations.add(28);
    }

    private void generatePowerupLocations() {
        powerupLocations.add(5);
        powerupLocations.add(15);
        powerupLocations.add(25);
        powerupLocations.add(35);
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
    public static ChanceCards getChanceType(String message) {
        for (Map.Entry<ChanceCards, String> entry : chances.entrySet()) {
            if (entry.getValue().equals(message)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @JsonIgnore
    public static CommunityChests getCommunityType(String message) {
        for (Map.Entry<CommunityChests, String> entry : communityChests.entrySet()) {
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

        ChanceCards type = ChanceCards.values()[randomChance];
        turn.addMove(getName(), chances.get(type));
        switch (type) {
            case ADV_BOWSER_CASTLE:
                goToTile(service, 39, turn, type, game, false);
                break;
            case ADV_GO:
                goToTile(service, 0, turn, type, game, true);
                break;
            case ADV_SHERBET:
                goToTile(service, 24, turn, type, game, true);
                break;
            case ADV_DELFINO:
                goToTile(service, 11, turn, type, game, true);
                break;
            case ADV_POWERUP:
                boolean moved = false;
                for (Integer powerupPosition : powerupLocations) {
                    if (getPosition() < powerupPosition) {
                        goToTile(service, powerupPosition, turn, type, game, false);
                        moved = true;
                        break;
                    }
                }
                if (!moved) {
                    goToTile(service, powerupLocations.get(0), turn, type, game, false);
                }
                break;
            case ADV_UT:
                for (Integer utilityPosition : utilityLocations) {
                    if (getPosition() < utilityPosition) {
                        goToTile(service, utilityPosition, turn, type, game, false);
                    }
                }
                goToTile(service, utilityLocations.get(0), turn, type, game, false);
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
                int jailTileIdx = service.getTile("Jail").getPosition();
                goToTile(service, jailTileIdx, turn, type, game, false);
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
                goToTile(service, 5, turn, type, game, true);
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

    private void goToTile(MonopolyService service, int tileToAdvance, Turn turn, ChanceCards chanceType, Game game, boolean passGo) {
        Tile newTile = service.getTile(tileToAdvance);
        turn.addMove(this.getName(), chances.get(chanceType));
        game.movePlayer(passGo, turn, newTile);
    }

    private void executeRandomCommunityChest(MonopolyService service, Game game, Turn turn) {
        int randomCommunityChest = random.nextInt(communityChests.size());

        CommunityChests type = CommunityChests.values()[randomCommunityChest];
        turn.addMove(getName(), communityChests.get(type));
        switch (type) {
            case ADV_GO:
                goToTile(service, 0, turn, type, game, true);
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
                int jailTileIdx = service.getTile("Jail").getPosition();
                goToTile(service, jailTileIdx, turn, type, game, false);
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
    private void goToTile(MonopolyService service, int tileToAdvance, Turn turn, CommunityChests chanceType, Game game, boolean passGo) {
        Tile newTile = service.getTile(tileToAdvance);
        turn.addMove(this.getName(), communityChests.get(chanceType));
        game.movePlayer(passGo, turn, newTile);
    }
}
