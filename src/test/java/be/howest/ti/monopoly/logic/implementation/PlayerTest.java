package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.implementation.tile.Railroad;
import be.howest.ti.monopoly.logic.implementation.tile.Street;
import be.howest.ti.monopoly.web.views.PropertyView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void buyPropertySuccesful(){
        Player pl = new Player("Bob", null);
        Railroad pr = new Railroad(1, "name", 5, 3, 2, "PURPLE", 20);
        PropertyView prView = new PropertyView(pr);

        pl.buyProperty(pr);

        assertEquals(1500 - 5, pl.getMoney());
        assertTrue(pl.getProperties().contains(prView));
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
        PropertyView prView = new PropertyView(s);
        PropertyView prView1 = new PropertyView(s2);
        PropertyView prView2 = new PropertyView(s3);

        p2.buyProperty(s);
        p2.buyProperty(s2);
        p.buyProperty(s3);

        p2.turnOverAssetsTo(p);

        assertEquals(0, p2.getMoney());
        assertTrue(p2.getProperties().isEmpty());
        assertEquals(1500 - 280 + (1500 - 240 - 260), p.getMoney());
        assertTrue(p.getProperties().contains(prView));
        assertTrue(p.getProperties().contains(prView1));
        assertTrue(p.getProperties().contains(prView2));
    }
}