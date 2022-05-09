package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.ChanceCards;
import be.howest.ti.monopoly.logic.implementation.CommunityChests;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
        chances.put(ChanceCards.ADV_BOARDWALK, "Advance to Boardwalk");
        chances.put(ChanceCards.ADV_GO, "Advance to Go (Collect $200)");
        chances.put(ChanceCards.ADV_ILL, "Advance to Illinois Avenue. If you pass Go, collect $200");
        chances.put(ChanceCards.ADV_CHARLES, "Advance to St. Charles Place. If you pass Go, collect $200");
        chances.put(ChanceCards.ADV_RR, "Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay owner twice the rental to which they are otherwise entitled");
        chances.put(ChanceCards.ADV_UT, "Advance token to nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total ten times amount thrown.");
        chances.put(ChanceCards.REC_BANK_50, "Bank pays you dividend of $50");
        chances.put(ChanceCards.JAIL_CARD, "Get Out of Jail Free");
        chances.put(ChanceCards.RETURN_3, "Go Back 3 Spaces");
        chances.put(ChanceCards.GO_JAIL, "Go to Jail. Go directly to Jail, do not pass Go, do not collect $200");
        chances.put(ChanceCards.PAY_REPAIRS, "Make general repairs on all your property. For each house pay $25. For each hotel pay $100");
        chances.put(ChanceCards.PAY_15, "Speeding fine $15");
        chances.put(ChanceCards.GO_READING, "Take a trip to Reading Railroad. If you pass Go, collect $200");
        chances.put(ChanceCards.PAY_PLAYERS_50, "You have been elected Chairman of the Board. Pay each player $50");
        chances.put(ChanceCards.REC_150, "Your building loan matures. Collect $150");
    }

    private static void generateCommunityChests() {
        communityChests.put(CommunityChests.ADV_GO, "Advance to Go (Collect $200)");
        communityChests.put(CommunityChests.REC_BANK_ERR, "Bank error in your favor. Collect $200");
        communityChests.put(CommunityChests.PAY_DOCTOR_FEE, "Doctor's fee. Pay $50");
        communityChests.put(CommunityChests.REC_SOLD_STOCK, "From sale of stock you get $50");
        communityChests.put(CommunityChests.GET_JAIL_CARD, "Get Out of Jail Free");
        communityChests.put(CommunityChests.GO_JAIL, "Go to Jail. Go directly to jail, do not pass Go, do not collect $200)");
        communityChests.put(CommunityChests.REC_HOLIDAY, "Holiday fund matures. Receive $100");
        communityChests.put(CommunityChests.REC_TAX, "Income tax refund. Collect $20");
        communityChests.put(CommunityChests.REC_BIRTHDAY, "It is your birthday. Collect $10 from every player");
        communityChests.put(CommunityChests.REC_INSURANCE, "Life insurance matures. Collect $100");
        communityChests.put(CommunityChests.PAY_HOSPITAL, "Pay hospital fees of $100");
        communityChests.put(CommunityChests.PAY_SCHOOL, "Pay school fees of $50");
        communityChests.put(CommunityChests.REC_CONSULTANCY, "Receive $25 consultancy fee");
        communityChests.put(CommunityChests.PAY_REPAIRS, "You are assessed for street repair. $40 per house. $115 per hotel");
        communityChests.put(CommunityChests.REC_PRIZE, "You have won second prize in a beauty contest. Collect $10");
        communityChests.put(CommunityChests.REC_INHERIT, "You inherit $100");
    }

    public void execute() {

    }
}
