package be.howest.ti.monopoly.web.views;

import be.howest.ti.monopoly.logic.implementation.tile.Property;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyView that = (PropertyView) o;
        return property.equals(that.property);
    }

    @Override
    public int hashCode() {
        return Objects.hash(property);
    }
}
