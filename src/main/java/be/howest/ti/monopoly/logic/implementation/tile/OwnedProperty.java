package be.howest.ti.monopoly.logic.implementation.tile;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

import java.util.Objects;

public class OwnedProperty {
    private final boolean mortgage;
    private final Property property;

    private int houseCount;
    private int hotelCount;

    public OwnedProperty(Property property) {
        this.mortgage = false;

        this.houseCount = 0;
        this.hotelCount = 0;

        this.property = property;
    }

    @JsonIdentityReference(alwaysAsId = true)
    public Property getProperty() {
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
        OwnedProperty that = (OwnedProperty) o;
        return property.equals(that.property);
    }

    @Override
    public int hashCode() {
        return Objects.hash(property);
    }
}
