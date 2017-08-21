package pl.krystiankrawczyk.favouriteplaces.locationservice;

/**
 * Created by Krystian Krawczyk on 20.08.2017.
 */

public class FavouritePlaceData {

    private double Latitude;
    private double Longitude;
    private String City;
    private String Country;
    private String PostalCode;

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }
}
