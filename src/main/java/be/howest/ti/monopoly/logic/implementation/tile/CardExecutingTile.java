package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.ChanceCards;
import be.howest.ti.monopoly.logic.implementation.CommunityChests;
import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.turn.Turn;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class CardExecutingTile extends Tile {
    private static final Map<ChanceCards, String> chances = new EnumMap<>(ChanceCards.class);
    private static final Map<CommunityChests, String> communityChests = new EnumMap<>(CommunityChests.class);

    public CardExecutingTile(int position, String name, TileType type) {
        super(position, name, type);

        if (chances.size() == 0) {
            generateChances();
            generateCommunityChests();
        }
    }

    @JsonIgnore
    public static List<String> getChances() {
        return new ArrayList<>(chances.values());
    }

    @JsonIgnore
    public static List<String> getCommunityChests() {
        return new ArrayList<>(communityChests.values());
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
        communityChests.put(CommunityChests.GO_JAIL, "Go to Jail. Go directly to jail, do not pass Go, do not collect $200)");
        communityChests.put(CommunityChests.REC_DELFINO, "You guide tourists around Delfino Island. Receive 100 coins");
        communityChests.put(CommunityChests.REC_BANK_OWES, "The toad Bank owes you some coins. Collect 20 coins");
        communityChests.put(CommunityChests.REC_BIRTHDAY, "It is your birthday. Collect 10 coins from every player");
        communityChests.put(CommunityChests.REC_KOOPALING, "You defeated a Koopaling. Collect 100 coins as a reward");
        communityChests.put(CommunityChests.PAY_BOO, "A Boo scares you and steals some coins, you lose 100 coins");
        communityChests.put(CommunityChests.PAY_STAR, "You lose a star and need to buy a new one for 50 coins");
        communityChests.put(CommunityChests.REC_FRUIT, "Receive 25 coins from selling fruit");
        communityChests.put(CommunityChests.PAY_REPAIRS, "You are assessed for street repair. $40 per house. $115 per castle");
        communityChests.put(CommunityChests.REC_PRIZE, "You have won second prize in a run contest against Coopa. Collect 10 coind");
        communityChests.put(CommunityChests.REC_ROSALINA, "Rosalina sends you a gift from space. Recieve 100 coins ");
    }

    public void execute(Game game, Turn turn) {
        switch (type) {
            case COMMUNITY_CHEST:
                executeRandomCommunityChest(game, turn);
                break;
            case CHANCE:
                executeRandomChance(game, turn);
                break;
            default:
                break;
        }
    }

    private void executeRandomChance(Game game, Turn turn) {
        Random random = new Random();
        int randomChance = random.nextInt(chances.size());

        switch (ChanceCards.values()[randomChance]) {
            case ADV_BOWSER_CASTLE:

                break;
            case ADV_GO:
                break;
            case ADV_SHERBET:
                break;
            case ADV_DELFINO:
                break;
            case ADV_POWERUP:
                break;
            case ADV_UT:
                break;
            case REC_BANK_50:
                break;
            case JAIL_CARD:
                break;
            case RETURN_3:
                break;
            case GO_JAIL:
                break;
            case PAY_REPAIRS:
                break;
            case PAY_15:
                break;
            case GO_FLOWER:
                break;
            case PAY_PLAYERS_50:
                break;
            case REC_150:
                break;
        }
    }

    private void executeRandomCommunityChest(Game game, Turn turn) {
        Random random = new Random();
        int randomUtility = random.nextInt(communityChests.size());

        switch (CommunityChests.values()[randomUtility]) {
            case ADV_GO:
                break;
            case REC_BANK_ERR:
                break;
            case PAY_DOCTOR_FEE:
                break;
            case REC_SOLD_STOCK:
                break;
            case GET_JAIL_CARD:
                break;
            case GO_JAIL:
                break;
            case REC_HOLIDAY:
                break;
            case REC_TAX:
                break;
            case REC_BIRTHDAY:
                break;
            case REC_INSURANCE:
                break;
            case PAY_HOSPITAL:
                break;
            case PAY_SCHOOL:
                break;
            case REC_CONSULTANCY:
                break;
            case PAY_REPAIRS:
                break;
            case REC_PRIZE:
                break;
            case REC_INHERIT:
                break;
        }
    }
}
