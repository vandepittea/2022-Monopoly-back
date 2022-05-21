package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.implementation.enums.StreetColor;
import be.howest.ti.monopoly.logic.implementation.tile.Railroad;
import be.howest.ti.monopoly.logic.implementation.tile.Street;
import be.howest.ti.monopoly.logic.implementation.turn.Turn;
import be.howest.ti.monopoly.logic.implementation.tile.OwnedProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    MonopolyService service;
    Game game;

    Player player1;
    Player player2;

    Railroad railroad;

    Street peachGarden;
    Street yoshiValley;
    Street sherbetLand;
    Street warioGoldmine;
    Street grumbleVolcano;

    @BeforeEach
    void init() {
        service = new MonopolyService();
        game = service.createGame(2, "group17", "gameName");

        player1 = new Player("Bob", null);
        player2 = new Player("Jan", null);

        railroad = new Railroad(1, "name", 5, 3, 2, StreetColor.PURPLE, 20);
        peachGarden = (Street) service.getTile(1);
        yoshiValley = (Street) service.getTile(3);
        sherbetLand = (Street) service.getTile(24);
        warioGoldmine = (Street) service.getTile(27);
        grumbleVolcano = (Street) service.getTile(29);
    }

    @Test
    void buyPropertySuccessful(){
        player1.buyProperty(railroad);

        assertEquals(1500 - 5, player1.getMoney());
        assertTrue(player1.getProperties().contains(new OwnedProperty(railroad)));
    }

    @Test
    void buyPropertyFailPayment(){
        Railroad railroad1 = new Railroad(1, "name", 1505, 3, 2, StreetColor.PURPLE, 20);
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.buyProperty(railroad1));
    }

    @Test
    void turnOverAssets(){
        player2.buyProperty(sherbetLand);
        player2.buyProperty(warioGoldmine);
        player1.buyProperty(grumbleVolcano);

        player2.turnOverAssetsTo(player1);

        assertEquals(0, player2.getMoney());
        assertTrue(player2.getProperties().isEmpty());
        assertEquals(1500 - 280 + (1500 - 240 - 260), player1.getMoney());
        assertTrue(player1.getProperties().contains(new OwnedProperty(sherbetLand)));
        assertTrue(player1.getProperties().contains(new OwnedProperty(warioGoldmine)));
        assertTrue(player1.getProperties().contains(new OwnedProperty(grumbleVolcano)));
    }

    @Test
    void collectDebtOwnershipFailed(){
        game.joinGame("Bob");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.collectDebt(sherbetLand, player1, game));
    }

    @Test
    void collectDebtDebtorNotOnProperty(){
        player1.getProperties().add(new OwnedProperty(sherbetLand));

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.collectDebt(sherbetLand, player2, game));
    }

    @Test
    void collectDebtTooLateDiceRolledAlready(){
        player1.getProperties().add(new OwnedProperty(sherbetLand));

        Turn turn = new Turn(player2);
        Turn turn2 = new Turn(player1);
        turn2.addMove(service.getTile(0), "can buy this property in direct sale");
        turn.addMove(sherbetLand, "should pay rent");
        game.getTurns().add(turn);
        game.getTurns().add(turn2);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.collectDebt(sherbetLand, player2, game));
    }

    @Test
    void collectDebtNotEnoughMoney(){
        Street street4 = new Street(29, "Rumble Volcano", 280, 140, 3,
                StreetColor.YELLOW, new Integer[]{120, 360, 850, 1025, 1200}, 150, 1505);

        player1.buyProperty(street4);

        Turn turn = new Turn(player2);
        player2.moveTo(street4);
        turn.addMove(street4, "should pay rent");
        game.getTurns().add(turn);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.collectDebt(street4, player2, game));
        assertEquals(1505, player2.getDebt());
        assertEquals(1500, player2.getMoney());
        assertEquals(player1, player2.getCreditor());
    }

    @Test
    void collectDebtSuccessful(){
        player1.buyProperty(sherbetLand);

        Turn turn = new Turn(player2);
        player2.moveTo(sherbetLand);
        turn.addMove(sherbetLand, "should pay rent");
        game.getTurns().add(turn);

        player1.collectDebt(sherbetLand, player2, game);
        assertEquals(1500 - 20, player2.getMoney());
        assertEquals(1500 - 240 + 20, player1.getMoney());
    }

    @Test
    void buyHousePlayerFailOwnershipStreetGroup(){
        player1.buyProperty(sherbetLand);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.buyHouseOrHotel(service, game, sherbetLand));
    }

    @Test
    void buyHousePlayerFailStreetHouseDifference(){
        player1.buyProperty(peachGarden);
        player1.buyProperty(yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.buyHouseOrHotel(service, game, peachGarden));
    }

    @Test
    void buyHouseNotEnoughMoney(){
        player1.buyProperty(sherbetLand);
        player1.buyProperty(warioGoldmine);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.buyHouseOrHotel(service, game, sherbetLand));
    }

    @Test
    void buyHouseNoHousesAvailable(){
        player1.buyProperty(sherbetLand);
        player1.buyProperty(warioGoldmine);
        game.setAvailableHouses(0);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.buyHouseOrHotel(service, game, sherbetLand));
    }

    @Test
    void buyHouseSuccessful(){
        player1.buyProperty(peachGarden);
        player1.buyProperty(yoshiValley);

        assertEquals(1, player1.buyHouseOrHotel(service, game, peachGarden));
        assertEquals(1, player1.getPropertyView(peachGarden).getHouseCount());
        assertEquals(1500 - 60 - 60 - 50, player1.getMoney());
        assertEquals(32 - 1, game.getAvailableHouses());
    }

    @Test
    void buyHotelNoHotelsAvailable(){
        player1.buyProperty(peachGarden);
        player1.buyProperty(yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        game.setAvailableHotels(0);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.buyHouseOrHotel(service, game, peachGarden));
    }

    @Test
    void buyHotelMultipleHotels(){
        player1.buyProperty(peachGarden);
        player1.buyProperty(yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        assertEquals(1, player1.buyHouseOrHotel(service, game, peachGarden));
        assertEquals(1, player1.getPropertyView(peachGarden).getHotelCount());

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.buyHouseOrHotel(service, game, peachGarden));
    }

    @Test
    void buyHotelSuccessful(){
        player1.buyProperty(peachGarden);
        player1.buyProperty(yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);

        assertEquals(1, player1.buyHouseOrHotel(service, game, peachGarden));
        assertEquals(0, player1.getPropertyView(peachGarden).getHouseCount());
        assertEquals(1, player1.getPropertyView(peachGarden).getHotelCount());
        assertEquals(1500 - 60 - 60 - 50 - 50 - 50 - 50 - 50 - 50 - 50 - 50 - 50, player1.getMoney());
        assertEquals(12 - 1, game.getAvailableHotels());
    }

    @Test
    void sellHouseNoHousesToSell() {
        player1.buyProperty(peachGarden);
        player1.buyProperty(yoshiValley);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.sellHouseOrHotel(service, game, peachGarden));
    }

    @Test
    void sellHouseCheckReturnPrice() {
        player1.buyProperty(peachGarden);
        player1.buyProperty(yoshiValley);

        player1.buyHouseOrHotel(service, game, peachGarden);
        int moneyBeforeSelling = player1.getMoney();
        player1.sellHouseOrHotel(service, game, peachGarden);
        int moneyAfterSelling = player1.getMoney();

        assertEquals(moneyAfterSelling - moneyBeforeSelling, peachGarden.getHousePrice()/2);
    }

    @Test
    void sellHouseSuccess() {
        player1.buyProperty(peachGarden);
        player1.buyProperty(yoshiValley);

        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.sellHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, peachGarden);

        assertEquals(1, player1.getPropertyView(peachGarden).getHouseCount());
    }

    @Test
    void sellHousePlayerFailStreetHouseDifference() {
        player1.buyProperty(peachGarden);
        player1.buyProperty(yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.sellHouseOrHotel(service, game, yoshiValley));
    }

    @Test
    void sellHotelNoHousesLeft() {
        player1.buyProperty(peachGarden);
        player1.buyProperty(yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        game.setAvailableHouses(0);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.sellHouseOrHotel(service, game, peachGarden));
    }

    @Test
    void sellHotelCheckReturnPrice() {
        player1.buyProperty(peachGarden);
        player1.buyProperty(yoshiValley);

        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);
        player1.buyHouseOrHotel(service, game, peachGarden);
        int moneyBeforeSelling = player1.getMoney();
        player1.sellHouseOrHotel(service, game, peachGarden);
        int moneyAfterSelling = player1.getMoney();

        assertEquals(moneyAfterSelling - moneyBeforeSelling, peachGarden.getHousePrice()/2);
    }

    @Test
    void sellHotelSuccessfull() {
        player1.buyProperty(peachGarden);
        player1.buyProperty(yoshiValley);

        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);

        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);

        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);

        player1.buyHouseOrHotel(service, game, peachGarden);
        player1.buyHouseOrHotel(service, game, yoshiValley);

        player1.buyHouseOrHotel(service, game, peachGarden);


        assertEquals(0, player1.sellHouseOrHotel(service, game, peachGarden));
        assertEquals(4, player1.getPropertyView(peachGarden).getHouseCount());
        assertEquals(0, player1.getPropertyView(peachGarden).getHotelCount());
        assertEquals(1500 - 60 - 60 - 50 - 50 - 50 - 50 - 50 - 50 - 50 - 50 - 50 + 25, player1.getMoney());
        assertEquals(11 + 1, game.getAvailableHotels());

    }

    @Test
    void getOutOfJailFreeNoCards(){
        player1.goToJail(null);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.getOutOfJailFree());
    }

    @Test
    void getOutOfJailFreeNotJailed(){
        player1.receiveGetOutOfJailFreeCard();

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.getOutOfJailFree());
    }

    @Test
    void getOutOfJailFreeSuccessful(){
        player1.goToJail(null);
        player1.receiveGetOutOfJailFreeCard();

        assertTrue(player1.isJailed());
        assertEquals(1, player1.getGetOutOfJailFreeCards());

        player1.getOutOfJailFree();

        assertFalse(player1.isJailed());
        assertEquals(0, player1.getGetOutOfJailFreeCards());
    }

    @Test
    void getOutOfJailFineNotInJail(){
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.getOutOfJailFine());
    }

    @Test
    void getOutOfJailFineNotEnoughMoney(){
        player1.goToJail(null);
        player1.payDebt(1470, null);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> player1.getOutOfJailFine());
    }

    @Test
    void getOutOfJailFineSuccessful(){
        player1.goToJail(null);
        player1.getOutOfJailFine();

        assertFalse(player1.isJailed());
        assertEquals(1500-50, player1.getMoney());
    }

    @Test
    void payTaxesEstimate() {
        player1.useComputeTax();
        player1.useEstimateTax();
        assertEquals(1500, player1.getMoney());
        player1.payTaxes();
        assertEquals(1300, player1.getMoney());
    }

    @Test
    void payTaxesCompute() {
        player2.useComputeTax();
        player2.buyProperty(peachGarden);
        player2.buyProperty(yoshiValley);
        player2.buyHouseOrHotel(service, game, peachGarden);

        int money = 1500 - 60 - 60 - 50;
        assertEquals(money, player2.getMoney());
        player2.payTaxes();
        assertEquals(money - ((money + 60 + 60 + 50) / 10), player2.getMoney());
    }

    @Test
    void assignPawn() {
        player1.assignPawn(game, "Mario");
        assertEquals("Mario", player1.getPawn());
    }
}