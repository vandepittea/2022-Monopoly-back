package be.howest.ti.monopoly.logic.implementation.generator;

import be.howest.ti.monopoly.logic.implementation.enums.*;
import be.howest.ti.monopoly.logic.implementation.tile.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Generator {
    private Generator() {}

    public static List<Tile> generateTiles() {
        List<Tile> tiles = new ArrayList<>();

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

        return tiles;
    }

    public static Map<ChanceCards, String> generateChances() {
        Map<ChanceCards, String> chances = new EnumMap<>(ChanceCards.class);

        chances.put(ChanceCards.ADV_BOWSER_CASTLE, "You got launched by a cannon right to Bowsers Castle");
        chances.put(ChanceCards.ADV_GO, "Head to Peach's Castle (Collect 200 coins)");
        chances.put(ChanceCards.ADV_SHERBET, "You are sent to Sherbet Land. If you pass Peach's Castle, collect 200 coins");
        chances.put(ChanceCards.ADV_DELFINO, "Take a plane to Delfino Airstrip. If you pass Peach's Castle, collect 200 coins");
        chances.put(ChanceCards.ADV_POWERUP, "Advance to the nearest Powerup. If unowned, you may buy it from the Bank. If owned, pay owner twice the rental to which they are otherwise entitled");
        chances.put(ChanceCards.ADV_UT, "Advance token to nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total ten times amount thrown.");
        chances.put(ChanceCards.REC_BANK_50, "The Toad Bank pays you dividend of 50 coins");
        chances.put(ChanceCards.GET_JAIL_CARD, "Get Out of Jail Free");
        chances.put(ChanceCards.RETURN_3, "Go Back 3 Spaces");
        chances.put(ChanceCards.GO_JAIL, "Go to Jail. Go directly to Jail, do not pass Go, do not collect 200 coins");
        chances.put(ChanceCards.PAY_REPAIRS, "Make general repairs on all your property. For each house pay 25 coins. For each castle pay 100 coins");
        chances.put(ChanceCards.PAY_15, "Speeding fine 15 coins");
        chances.put(ChanceCards.GO_FLOWER, "Take a trip to the FireFlower. If you pass Peach's Castle, collect 200 coins");
        chances.put(ChanceCards.PAY_PLAYERS_50, "You lost a bet that you would defeat Bowser. Pay each player 50 coins");
        chances.put(ChanceCards.REC_150, "You won an important race! Collect 150 coins");

        return chances;
    }

    public static Map<CommunityChests, String> generateCommunityChests() {
        Map<CommunityChests, String> communityChests = new EnumMap<>(CommunityChests.class);

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

        return communityChests;
    }

    public static Map<Utilities, Integer> generateUtilityLocations() {
        Map<Utilities, Integer> utilityLocations = new EnumMap<>(Utilities.class);

        utilityLocations.put(Utilities.ELECTRIC_KOOPA_FARM, 12);
        utilityLocations.put(Utilities.GAS_PUMP, 28);

        return utilityLocations;
    }

    public static Map<PowerUps, Integer> generatePowerUpLocations() {
        Map<PowerUps, Integer> powerUpLocations = new EnumMap<>(PowerUps.class);

        powerUpLocations.put(PowerUps.FIRE_FLOWER, 5);
        powerUpLocations.put(PowerUps.FLUTT, 15);
        powerUpLocations.put(PowerUps.MARIO_CART, 25);
        powerUpLocations.put(PowerUps.CAPPY, 35);

        return powerUpLocations;
    }

    public static Map<TilesToAdvance, Integer> generateTilesToAdvance() {
        Map<TilesToAdvance, Integer> tilesToAdvance = new EnumMap<>(TilesToAdvance.class);

        tilesToAdvance.put(TilesToAdvance.GO, 0);
        tilesToAdvance.put(TilesToAdvance.JAIL, 10);
        tilesToAdvance.put(TilesToAdvance.DELFINO, 11);
        tilesToAdvance.put(TilesToAdvance.SHERBET, 24);
        tilesToAdvance.put(TilesToAdvance.BOWSER_CASTLE, 39);

        return tilesToAdvance;
    }
}
