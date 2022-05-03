package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.ServiceAdapter;
import be.howest.ti.monopoly.logic.implementation.tile.*;

import java.util.List;


public class MonopolyService extends ServiceAdapter {

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public List<Tile> getTiles(){
        return List.of(
                new SimpleTile(0, "Go", "Go"),
                new Street(1, "Mediterranean", 60, 30, 2, "PURPLE", new Integer[]{10, 30, 90, 160, 250}, 50, "PURPLE", 2),
                new CardExecutingTile(2, "Community Chest I", "community chest"),
                new Street(3, "Baltic", 60, 30, 2, "PURPLE", new Integer[]{20,60,180,320,450}, 50, "PURPLE", 4),
                new CardExecutingTile(4, "Tax Income", "Tax Income"),
                new Railroad(5, "Reading RR", 200, 100, 4, "BLACK", 25),
                new Street(6, "Oriental", 100, 50, 3, "LIGHTBLUE", new Integer[]{30,90,270,400,550}, 50, "LIGHTBLUE", 6),
                new CardExecutingTile(7, "Chance I", "chance"),
                new Street(8, "Vermont", 100, 50, 3, "LIGHTBLUE", new Integer[]{30,90,270,400,550}, 50,"LIGHTBLUE",6),
                new Street(9, "Connecticut",120, 60, 3, "LIGHTBLUE", new Integer[]{40,100,300,450,600}, 50, "LIGHTBLUE", 8),
                new SimpleTile(10, "Jail", "Jail"),
                new Street(11, "Saint Charles Place", 140, 70, 3, "VIOLET", new Integer[]{50,150,450,625,750}, 100, "VIOLET", 10),
                new Utility(12, "Electric Company", 150, 75, 2, "WHITE","4 or 10 times the dice roll"),
                new Street(13, "States", 140, 70, 3, "VIOLET", new Integer[]{50,150,450,625,750}, 100, "VIOLET", 10),
                new Street(14, "Virginia", 160, 80, 3, "VIOLET", new Integer[]{60,180,500,700,900}, 100, "VIOLET", 12),
                new Railroad(15, "Pennsylvania RR", 200, 100, 4, "BLACK", 25),
                new Street(16, "Saint James", 180, 90, 3, "ORANGE", new Integer[]{70,200,550,750,950}, 100,"ORANGE", 14),
                new CardExecutingTile(17, "Community Chest II", "community chest"),
                new Street(18, "Tennessee", 180, 90, 3, "ORANGE", new Integer[]{70,200,550,750,950}, 100, "ORANGE", 14),
                new Street(19, "New York", 200, 100, 3, "ORANGE", new Integer[]{80,220,600,800,1000}, 100, "ORANGE", 16),
                new SimpleTile(20, "Free Parking", "Free Parking"),
                new Street(21, "Kentucky Avenue", 220, 110, 3, "RED", new Integer[]{90,250,700,875,1050}, 150, "RED", 18),
                new CardExecutingTile(22, "Chance II", "chance"),
                new Street(23, "Indiana Avenue", 220, 110, 3, "RED", new Integer[]{90,250,700,875,1050}, 150, "RED", 18),
                new Street(24, "Illinois Avenue", 240, 120, 3, "RED", new Integer[]{100,300,750,925,1100}, 150, "RED", 20),
                new Railroad(25, "Baltimore and Ohio RR", 200, 100, 4, "Black", 25),
                new Street(26, "Atlantic", 260, 130, 3, "YELLOW", new Integer[]{110,330,800,975,1150}, 150, "YELLOW", 22),
                new Street(27, "Ventnor", 260, 130, 3, "YELLOW", new Integer[]{110,330,800,975,1150,}, 150, "YELLOW", 22),
                new Utility(28, "Water Works", 150, 75, 2, "WHITE", "4 or 10 times the dice roll"),
                new Street(29, "Marvin Gardens", 280, 140, 3, "YELLOW", new Integer[]{120,360,850,1025,1200}, 150, "YELLOW", 24),
                new SimpleTile(30, "Go to Jail", "Go to Jail"),
                new Street(31, "Pacific", 300, 150, 3, "DARKGREEN", new Integer[]{130,390,900,1100,1275}, 200, "DARKGREEN", 26),
                new Street(32, "North Carolina", 300, 150, 3, "DARKGREEN", new Integer[]{130,390,900,1100,1275}, 200, "DARKGREEN", 26),
                new CardExecutingTile(33, "Community Chest III", "community chest"),
                new Street(34, "Pennsylvania", 320, 160, 3, "DARKGREEN", new Integer[]{150,450,1000,1200,1400}, 200, "DARKGREEN", 28),
                new Railroad(35, "Short Line RR", 200, 100, 4, "BLACK", 25),
                new CardExecutingTile(36, "Chance III", "chance"),
                new Street(37, "Park Place", 350, 175, 2, "DARKBLUE", new Integer[]{175,500,1100,1300,1500}, 200, "DARKGREEN", 35),
                new CardExecutingTile(38, "Luxury Tax", "Luxury Tax"),
                new Street(39, "Boardwalk", 400, 200, 2, "DARKBLUE", new Integer[]{200,600,1400,1700,2000}, 200, "DARKBLUE", 50)

        );
    }
}
