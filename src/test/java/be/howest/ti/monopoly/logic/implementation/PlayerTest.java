package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.implementation.tile.Railroad;
import be.howest.ti.monopoly.logic.implementation.tile.Street;
import be.howest.ti.monopoly.logic.implementation.turn.Turn;
import be.howest.ti.monopoly.web.views.PropertyView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void buyPropertySuccessful(){
        Player pl = new Player("Bob", null);
        Railroad pr = new Railroad(1, "name", 5, 3, 2, "PURPLE", 20);

        pl.buyProperty(pr);

        assertEquals(1500 - 5, pl.getMoney());
        assertTrue(pl.getProperties().contains(pr));
    }

    @Test
    void buyPropertyFailPayment(){
        Player pl = new Player("Bob", null);
        Railroad pr = new Railroad(1, "name", 1505, 3, 2, "PURPLE", 20);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> pl.buyProperty(pr));
    }

    @Test
    void turnOverAssets(){
        Player p = new Player("Bob", null);
        Player p2 = new Player("Jan", null);
        Street s = new Street(24, "Sherbet Land", 240, 120, 3, "RED",
                new Integer[]{100, 300, 750, 925, 1100}, 150, "RED", 20);
        Street s2 = new Street(27, "Wario's Goldmine", 260, 130, 3, "YELLOW",
                new Integer[]{110, 330, 800, 975, 1150,}, 150, "YELLOW", 22);
        Street s3 = new Street(29, "Grumble Volcano", 280, 140, 3, "YELLOW",
                new Integer[]{120, 360, 850, 1025, 1200}, 150, "YELLOW", 24);

        p2.buyProperty(s);
        p2.buyProperty(s2);
        p.buyProperty(s3);

        p2.turnOverAssetsTo(p);

        assertEquals(0, p2.getMoney());
        assertTrue(p2.getProperties().isEmpty());
        assertEquals(1500 - 280 + (1500 - 240 - 260), p.getMoney());
        assertTrue(p.getProperties().contains(s));
        assertTrue(p.getProperties().contains(s2));
        assertTrue(p.getProperties().contains(s3));
    }

    @Test
    void collectDebtOwnershipFailed(){
        MonopolyService service = new MonopolyService();
        Game g = service.createGame(2, "group17");
        Player p = new Player("Bob", null);
        Street s = new Street(1, "Peach's Garden", 60, 30, 2,
                "PURPLE", new Integer[]{10, 30, 90, 160, 250}, 50, "PURPLE", 2);

        g.joinGame("Bob");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> p.collectDebt(s, p, g));
    }

    @Test
    void collectDebtDebtorNotOnProperty(){
        MonopolyService service = new MonopolyService();
        Game g = service.createGame(2, "group17");
        Street s = new Street(1, "Peach's Garden", 60, 30, 2,
                "PURPLE", new Integer[]{10, 30, 90, 160, 250}, 50, "PURPLE", 2);
        Street s2 = new Street(16, "Sirena Beach", 180, 90, 3, "ORANGE",
                new Integer[]{70, 200, 550, 750, 950}, 100, "ORANGE", 14);
        Player p = new Player("Bob", null);
        Player p2 = new Player("Jan", s2);

        p.getProperties().add(s);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> p.collectDebt(s, p2, g));
    }

    @Test
    void collectDebtTooLateDiceRolledAlready(){
        MonopolyService service = new MonopolyService();
        Game g = service.createGame(2, "group17");
        Street s = new Street(1, "Peach's Garden", 60, 30, 2,
                "PURPLE", new Integer[]{10, 30, 90, 160, 250}, 50, "PURPLE", 2);
        Player p = new Player("Bob", null);
        Player p2 = new Player("Jan", s);

        p.getProperties().add(s);

        Turn turn = new Turn(p2);
        Turn turn2 = new Turn(p);
        turn2.addMove("Electric Company", "can buy this property in direct sale");
        turn.addMove(s.getName(), "should pay rent");
        g.getTurns().add(turn);
        g.getTurns().add(turn2);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> p.collectDebt(s, p2, g));
    }

    @Test
    void collectDebtNotEnoughMoney(){
        MonopolyService service = new MonopolyService();
        Game g = service.createGame(2, "group17");
        Street s = new Street(1, "Peach's Garden", 60, 30, 2,
                "PURPLE", new Integer[]{10, 30, 90, 160, 250}, 50, "PURPLE", 1505);
        Player p = new Player("Bob", null);
        Player p2 = new Player("Jan", s);

        p.getProperties().add(s);

        Turn turn = new Turn(p2);
        turn.addMove(s.getName(), "should pay rent");
        g.getTurns().add(turn);

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> p.collectDebt(s, p2, g));
        assertEquals(1505, p2.getDebt());
        assertEquals(1500, p2.getMoney());
        assertEquals(p, p2.getCreditor());
    }

    @Test
    void collectDebtSuccessful(){
        MonopolyService service = new MonopolyService();
        Game g = service.createGame(2, "group17");
        Street s = new Street(1, "Peach's Garden", 60, 30, 2,
                "PURPLE", new Integer[]{10, 30, 90, 160, 250}, 50, "PURPLE", 20);
        Player p = new Player("Bob", null);
        Player p2 = new Player("Jan", s);

        p.getProperties().add(s);

        Turn turn = new Turn(p2);
        turn.addMove(s.getName(), "should pay rent");
        g.getTurns().add(turn);

        p.collectDebt(s, p2, g);
        assertEquals(1500 - 20, p2.getMoney());
        assertEquals(1500 + 20, p.getMoney());
    }
}