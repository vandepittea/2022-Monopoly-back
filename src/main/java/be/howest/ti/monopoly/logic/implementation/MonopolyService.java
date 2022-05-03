package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.ServiceAdapter;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
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
                new CardExecutingTile(4, "Tax Income", "Tex Income"),
                new Railroad(5, "Reading RR", 200, 100, 4, "BLACK", 25),
                new Street(6, "Oriental", 100, 50, 3, "LIGHTBLUE", new Integer[]{30,90,270,400,550}, 50, "LIGHTBLUE", 6)
        );
    }

    @Override
    public Tile getTile(int position){
        for(Tile tile: getTiles()){
            if(tile.getPosition() == position){
                return tile;
            }
        }
        throw new MonopolyResourceNotFoundException("No such tile, index out of bounds.");
    }
}
