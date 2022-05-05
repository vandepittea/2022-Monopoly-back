package be.howest.ti.monopoly.web.views;

import be.howest.ti.monopoly.logic.implementation.tile.Property;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PropertyView {
    private final Property property;

    public PropertyView(Property property){
        this.property = property;
    }

    public String getProperty(){
        return property.getName();
    }
    public boolean isMortgage() {
        return property.isMortgaged();
    }
    public int getHouseCount() {
        return property.getHouseCount();
    }
    public int getHotelCount() {
        return property.getHotelCount();
    }
}
