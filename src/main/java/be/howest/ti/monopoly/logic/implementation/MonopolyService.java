package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.ServiceAdapter;
import be.howest.ti.monopoly.logic.implementation.tile.SimpleTile;
import be.howest.ti.monopoly.logic.implementation.tile.Tile;

import java.util.List;


public class MonopolyService extends ServiceAdapter {

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public List<Tile> getTiles(){
        return List.of(
                new SimpleTile(0, "Go", "Go")
        );
    }
}
