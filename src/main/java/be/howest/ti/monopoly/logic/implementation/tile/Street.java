package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.MonopolyService;
import be.howest.ti.monopoly.logic.implementation.Player;
import be.howest.ti.monopoly.logic.implementation.enums.StreetColor;
import be.howest.ti.monopoly.logic.implementation.enums.TileType;

import java.util.Arrays;
import java.util.Objects;

public class Street extends Property {
    public static final int NUMBER_OF_HOUSES_IN_ONE_HOTEL = 4;

    private final int rent;
    private final int housePrice;
    private final StreetColor streetColor;
    private final Integer[] rentOfHouses;

    //Sonarlint error ignored as false positive: approved by Mr.Casier
    public Street(int position, String name, int cost, int mortgage, int groupSize, StreetColor color,
                  Integer[] rentOfHouses, int housePrice, int rent) {
        super(position, name, cost, mortgage, groupSize, color, TileType.STREET);

        this.rent = rent;
        this.housePrice = housePrice;

        this.streetColor = color;
        this.rentOfHouses = rentOfHouses;
    }

    public Integer[] getRentOfHouses() {
        return rentOfHouses;
    }

    public int getHousePrice() {
        return housePrice;
    }

    public StreetColor getStreetColor() {
        return streetColor;
    }

    public int getRent() {
        return rent;
    }

    public boolean checkStreetHouseDifference(MonopolyService service, Player player, boolean buy) {
        int decideBuyOrSell = 1;
        if (!buy) {
            decideBuyOrSell *= -1;
        }

        for (Tile t : service.getTiles()) {
            if (t.getType() == TileType.STREET) {
                Street s = (Street) t;
                if (checkStreetColor(s.getStreetColor(), streetColor) && isBigDifference(player, decideBuyOrSell, s)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkStreetColor(StreetColor streetColor1, StreetColor streetColor2) {
        return streetColor1.equals(streetColor2);
    }

    private boolean isBigDifference(Player player, int decideBuyOrSell, Street street) {
        int housesStreet1 = player.getPropertyView(street).getHouseCount() +
                (player.getPropertyView(street).getHotelCount() * NUMBER_OF_HOUSES_IN_ONE_HOTEL);
        int housesStreet2 = player.getPropertyView(this).getHouseCount() +
                (player.getPropertyView(this).getHotelCount() * NUMBER_OF_HOUSES_IN_ONE_HOTEL);
        int difference = Math.abs(housesStreet1 - (housesStreet2 + decideBuyOrSell));

        return difference > 1;
    }

    @Override
    public int calculateRent(Player player, Game game) {
        if (player.getPropertyView(this).getHouseCount() > 0) {
            return rentOfHouses[player.getPropertyView(this).getHouseCount() - 1];
        } else if (player.getPropertyView(this).getHotelCount() > 0) {
            return rentOfHouses[rentOfHouses.length - 1];
        } else {
            return rent;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Street street = (Street) o;
        return rent == street.rent && housePrice == street.housePrice && checkStreetColor(streetColor, street.streetColor) && Arrays.equals(rentOfHouses, street.rentOfHouses);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), rent, housePrice, streetColor);
        result = 31 * result + Arrays.hashCode(rentOfHouses);
        return result;
    }
}
