package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.*;
import be.howest.ti.monopoly.logic.implementation.turn.Turn;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class CardExecutingTile extends Tile {
    private static final Map<ChanceCards, String> chances = new EnumMap<>(ChanceCards.class);
    private static final Map<CommunityChests, String> communityChests = new EnumMap<>(CommunityChests.class);
    private static final List<Integer> powerupLocations = new ArrayList<>();
    private static final List<Integer> utilityLocations = new ArrayList<>();

    public CardExecutingTile(int position, String name, TileType type) {
        super(position, name, type);

        if (chances.size() == 0) {
            generateChances();
            generateCommunityChests();
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
        for (ChanceCards type : chances.keySet()) {
            if (chances.get(type).equals(message)) {
                return type;
            }
        }
        return null;
    }

    @JsonIgnore
    public static CommunityChests getCommunityType(String message) {
        for (CommunityChests type : communityChests.keySet()) {
            if (communityChests.get(type).equals(message)) {
                return type;
            }
        }
        return null;
    }

    private static void generateChances() {
        chances.put(ChanceCards.ADV_BOWSER_CASTLE, "You got launched by a cannon right to Bowsers Castle");
        chances.put(ChanceCards.ADV_GO, "Head to Peach's Castle (Collect 200 coins)");
        chances.put(ChanceCards.ADV_SHERBET, "You are sent to Sherbet Land. If you pass Peach's Castle, collect 200 coins");
        chances.put(ChanceCards.ADV_DELFINO, "Take a plane to Delfino Airstrip. If you pass Peach's Castle, collect 200 coins");
        chances.put(ChanceCards.ADV_POWERUP, "Advance to the nearest Powerup. If unowned, you may buy it from the Bank. If owned, pay owner twice the rental to which they are otherwise entitled");
        chances.put(ChanceCards.ADV_UT, "Advance token to nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total ten times amount thrown.");
        chances.put(ChanceCards.REC_BANK_50, "The Toad Bank pays you dividend of 50 coins");
        chances.put(ChanceCards.JAIL_CARD, "Get Out of Jail Free");
        chances.put(ChanceCards.RETURN_3, "Go Back 3 Spaces");
        chances.put(ChanceCards.GO_JAIL, "Go to Jail. Go directly to Jail, do not pass Go, do not collect 200 coins");
        chances.put(ChanceCards.PAY_REPAIRS, "Make general repairs on all your property. For each house pay 25 coins. For each castle pay 100 coins");
        chances.put(ChanceCards.PAY_15, "Speeding fine 15 coins");
        chances.put(ChanceCards.GO_FLOWER, "Take a trip to the FireFlower. If you pass Peach's Castle, collect 200 coins");
        chances.put(ChanceCards.PAY_PLAYERS_50, "You lost a bet that you would defeat Bowser. Pay each player 50 coins");
        chances.put(ChanceCards.REC_150, "You won an important race! Collect 150 coins");
    }

    private static void generateCommunityChests() {
        communityChests.put(CommunityChests.ADV_GO, "Head to Peach's Castle (Collect 200 coins)");
        communityChests.put(CommunityChests.REC_BANK_ERR, "The Toad Bank made an error in your favor. Collect 200 coins");
        communityChests.put(CommunityChests.PAY_DOCTOR_FEE, "You are sick and go to Doctor Mario. Pay 50 coins");
        communityChests.put(CommunityChests.REC_SOLD_STOCK, "You found a hidden chest with coins. You get 50 coins");
        communityChests.put(CommunityChests.GET_JAIL_CARD, "Get Out of Jail Free");
        communityChests.put(CommunityChests.GO_JAIL, "Go to Jail. Go directly to jail, do not pass Peach's Castle, do not collect $200)");
        communityChests.put(CommunityChests.REC_DELFINO, "You guide tourists around Delfino Island. Receive 100 coins");
        communityChests.put(CommunityChests.REC_BANK_OWES, "The toad Bank owes you some coins. Collect 20 coins");
        communityChests.put(CommunityChests.REC_BIRTHDAY, "It is your birthday. Collect 10 coins from every player");
        communityChests.put(CommunityChests.REC_KOOPALING, "You defeated a Koopaling. Collect 100 coins as a reward");
        communityChests.put(CommunityChests.PAY_BOO, "A Boo scares you and steals some coins, you lose 100 coins");
        communityChests.put(CommunityChests.PAY_STAR, "You lose a star and need to buy a new one for 50 coins");
        communityChests.put(CommunityChests.REC_FRUIT, "Receive 25 coins from selling fruit");
        communityChests.put(CommunityChests.PAY_REPAIRS, "You are assessed for street repair. $40 per house. $115 per castle");
        communityChests.put(CommunityChests.REC_PRIZE, "You have won second prize in a run contest against some Coopas. Collect 10 coins");
        communityChests.put(CommunityChests.REC_ROSALINA, "Rosalina sends you a gift from space. Receive 100 coins ");
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
        Random random = new Random();
        int randomChance = random.nextInt(chances.size());

        ChanceCards type = ChanceCards.values()[randomChance];
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
                goToTile(service, 13, turn, type, game, true);
                break;
            case ADV_POWERUP:
                for (Integer powerupPosition : powerupLocations) {
                    if (getPosition() < powerupPosition) {
                        goToTile(service, powerupPosition, turn, type, game, false);
                    }
                }
                goToTile(service, powerupLocations.get(0), turn, type, game, false);
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
                turn.addMove(getName(), chances.get(type));
                game.changeCurrentPlayer(false);
                break;
            case JAIL_CARD:
                game.getCurrentplayerObject().receiveGetOutOfJailCard();
                turn.addMove(getName(), chances.get(type));
                game.changeCurrentPlayer(false);
                break;
            case RETURN_3:
                int currentTileIdx = service.getTile(game.getCurrentplayerObject().getCurrentTile()).getPosition();
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
                Set<Property> properties = game.getCurrentplayerObject().getProperties();
                for (Property property : properties) {
                    if (property.type == TileType.STREET){
                        Street street = (Street) property;
                        cost += game.receiveHouseCount(street) * 25;
                        cost += game.receiveHotelCount(street) * 100;
                    }
                }
                game.getCurrentplayerObject().payDebt(cost, null);
                turn.addMove(getName(), chances.get(type));
                game.changeCurrentPlayer(false);
                break;
            case PAY_15:
                game.getCurrentplayerObject().payDebt(15, null);
                turn.addMove(getName(), chances.get(type));
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
                }
                turn.addMove(getName(), chances.get(type));
                game.changeCurrentPlayer(false);
                break;
            case REC_150:
                game.getCurrentplayerObject().receiveMoney(150);
                turn.addMove(getName(), chances.get(type));
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
        Random random = new Random();
        int randomCommunityChest = random.nextInt(communityChests.size());

        CommunityChests type = CommunityChests.values()[randomCommunityChest];
        switch (type) {
            case ADV_GO:
                goToTile(service, 0, turn, type, game, true);
                break;
            case REC_BANK_ERR:
                game.getCurrentplayerObject().receiveMoney(200);
                turn.addMove(getName(), communityChests.get(type));
                game.changeCurrentPlayer(false);
                break;
            case PAY_DOCTOR_FEE:
            case PAY_STAR:
                game.getCurrentplayerObject().payDebt(50, null);
                turn.addMove(getName(), communityChests.get(type));
                game.changeCurrentPlayer(false);
                break;
            case REC_SOLD_STOCK:
                game.getCurrentplayerObject().receiveMoney(50);
                turn.addMove(getName(), communityChests.get(type));
                game.changeCurrentPlayer(false);
                break;
            case GET_JAIL_CARD:
                game.getCurrentplayerObject().receiveGetOutOfJailCard();
                turn.addMove(getName(), communityChests.get(type));
                game.changeCurrentPlayer(false);
                break;
            case GO_JAIL:
                int jailTileIdx = service.getTile("Jail").getPosition();
                goToTile(service, jailTileIdx, turn, type, game, false);
                break;
            case REC_DELFINO:
            case REC_KOOPALING:
            case REC_INHERIT:
                game.getCurrentplayerObject().receiveMoney(100);
                turn.addMove(getName(), communityChests.get(type));
                game.changeCurrentPlayer(false);
                break;
            case REC_BANK_OWES:
                game.getCurrentplayerObject().receiveMoney(20);
                turn.addMove(getName(), communityChests.get(type));
                game.changeCurrentPlayer(false);
                break;
            case REC_BIRTHDAY:
                Player currentPlayer = game.getCurrentplayerObject();
                for (Player player : game.getPlayers()) {
                    if (player.equals(currentPlayer)) {
                        continue;
                    }
                    player.payDebt(10, currentPlayer);
                }

                turn.addMove(getName(), communityChests.get(type));
                game.changeCurrentPlayer(false);
                break;
            case PAY_BOO:
                game.getCurrentplayerObject().payDebt(100, null);
                turn.addMove(getName(), communityChests.get(type));
                game.changeCurrentPlayer(false);
                break;
            case REC_FRUIT:
                game.getCurrentplayerObject().receiveMoney(25);
                turn.addMove(getName(), communityChests.get(type));
                game.changeCurrentPlayer(false);
                break;
            case PAY_REPAIRS:
                int cost = 0;
                Set<Property> properties = game.getCurrentplayerObject().getProperties();
                for (Property property : properties) {
                    if (property.type == TileType.STREET){
                        Street street = (Street) property;
                        cost += game.receiveHouseCount(street) * 40;
                        cost += game.receiveHotelCount(street) * 115;
                    }
                }
                game.getCurrentplayerObject().payDebt(cost, null);
                turn.addMove(getName(), communityChests.get(type));
                game.changeCurrentPlayer(false);
                break;
            case REC_PRIZE:
                game.getCurrentplayerObject().receiveMoney(10);
                turn.addMove(getName(), communityChests.get(type));
                game.changeCurrentPlayer(false);
                break;
        }
    }
    private void goToTile(MonopolyService service, int tileToAdvance, Turn turn, CommunityChests chanceType, Game game, boolean passGo) {
        Tile newTile = service.getTile(tileToAdvance);
        turn.addMove(this.getName(), communityChests.get(chanceType));
        game.getCurrentplayerObject().moveTo(newTile);
        game.movePlayer(passGo, turn, newTile);
    }
}
