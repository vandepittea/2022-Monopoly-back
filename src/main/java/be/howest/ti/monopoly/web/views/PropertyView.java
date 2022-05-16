package be.howest.ti.monopoly.web.views;

import be.howest.ti.monopoly.logic.implementation.tile.Property;
import be.howest.ti.monopoly.logic.implementation.tile.Street;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class PropertyView {
    private Property property;
    private int houseCount;
    private int hotelCount;
    private boolean mortgage;

    public PropertyView(Property property) {
        this.property = property;
        this.houseCount = 0;
        this.hotelCount = 0;
        this.mortgage = false;
    }

    public String getProperty() {
        return property.getName();
    }

    @JsonIgnore
    public Property getPropertyObject() {
        return property;
    }

    public int getHouseCount() {
        return houseCount;
    }

    public int getHotelCount() {
        return hotelCount;
    }

    public boolean isMortgage() {
        return mortgage;
    }

    public void buyHouse() {
        houseCount++;
    }

    public void sellHouse() {
        houseCount--;
    }

    public void buyHotel() {
        houseCount = 0;
        hotelCount = 1;
    }

    public void sellHotel() {
        hotelCount = 0;
        houseCount = 4;
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
