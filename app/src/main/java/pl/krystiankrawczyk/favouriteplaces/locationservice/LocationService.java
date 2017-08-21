package pl.krystiankrawczyk.favouriteplaces.locationservice;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.krystiankrawczyk.favouriteplaces.R;
import pl.krystiankrawczyk.favouriteplaces.preferences.UserData;

/**
 * Created by Krystian Krawczyk on 16.08.2017.
 */

public class LocationService extends Activity implements LocationListener {

    private static final int GEOCODER_MAX_NUMBER_OF_RESULTS = 1;
    private static final int DEFAULT_FIRST_PLACE_KEY = 0;

    @BindView(R.id.location_service_location_fetching_info_tv)
    TextView fetchingInfoTV;
    @BindView(R.id.location_service_longitude_tv)
    TextView locationLongitudeTV;
    @BindView(R.id.location_service_latitude_tv)
    TextView locationLatitudeTV;
    @BindView(R.id.location_service_city_name_tv)
    TextView locationCityNameTV;
    @BindView(R.id.location_service_country_name_tv)
    TextView locationCountryNameTV;
    @BindView(R.id.location_service_postal_code_tv)
    TextView locationPostalCodeTV;
    @BindView(R.id.location_service_progress_bar_pb)
    ProgressBar serviceProgressBarPB;
    @BindView(R.id.location_service_save_current_location_btn)
    Button saveCurrentLocationBtn;

    private FavouritePlaceData currentPositionDetails = new FavouritePlaceData();
    private String locationProvider;
    private LocationManager locationManager;

    @OnClick(R.id.location_service_save_current_location_btn)
    public void onSaveLocationClicked() {
        int favouritePlacesCount = UserData.getInstance(getBaseContext()).getFavouritePlacesCount();
        if (isPlacesCollectionEmpty()) {
            UserData.getInstance(getBaseContext()).addNewFavouritePlace(DEFAULT_FIRST_PLACE_KEY,
                    currentPositionDetails);
        } else {
            UserData.getInstance(getBaseContext()).addNewFavouritePlace(favouritePlacesCount, currentPositionDetails);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_service_activity);
        ButterKnife.bind(this);
        setupView();
    }

    @Override
    protected void onResume() throws SecurityException {
        super.onResume();
        locationManager.requestLocationUpdates(locationProvider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        setUpCurrentLocationProperties(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserData.getInstance(getBaseContext()).saveFavouritePlacesMap();
    }

    private void setupView() throws SecurityException {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationProvider = locationManager.getBestProvider(new Criteria(), false);
        Location location = locationManager.getLastKnownLocation(locationProvider);

        if (location != null) {
            System.out.println("Provider " + locationProvider + "has been selected");
            onLocationChanged(location);
        } else {
            serviceProgressBarPB.setVisibility(View.VISIBLE);
        }
    }

    private void setUpCurrentLocationProperties(Location location) {
        currentPositionDetails.setLongitude(location.getLongitude());
        currentPositionDetails.setLatitude(location.getLatitude());
        currentPositionDetails.setCity(getLocality());
        currentPositionDetails.setCountry(getCountryName());
        currentPositionDetails.setPostalCode(getPostalCode());
        setupCorrespondingEditTexts();
    }

    @Nullable
    private List<Address> getGeocoderAddress() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            return geocoder.getFromLocation(currentPositionDetails.getLatitude(), currentPositionDetails.getLongitude(),
                    GEOCODER_MAX_NUMBER_OF_RESULTS);
        } catch (IOException exception) {
            Log.e(Geocoder.class.getName(), "Could not connect to geocoder", exception);
        }
        return null;
    }

    public String getLocality() {
        List<Address> addressList = getGeocoderAddress();

        if (addressList != null && addressList.size() > 0) {
            return addressList.get(0).getLocality();
        } else {
            return "Can't resolve city";
        }
    }

    private String getPostalCode() {
        List<Address> addressList = getGeocoderAddress();

        if (addressList != null && addressList.size() > 0) {
            return addressList.get(0).getPostalCode();
        } else {
            return "Can't resolve postal code";
        }
    }

    private String getCountryName() {
        List<Address> addressList = getGeocoderAddress();

        if (addressList != null && addressList.size() > 0) {
            return addressList.get(0).getCountryName();
        } else {
            return "Can't resolve country name";
        }
    }

    private void setupCorrespondingEditTexts() {
        locationLatitudeTV.setText(String.valueOf(currentPositionDetails.getLatitude()));
        locationLongitudeTV.setText(String.valueOf(currentPositionDetails.getLongitude()));
        locationCityNameTV.setText(currentPositionDetails.getCity());
        locationCountryNameTV.setText(currentPositionDetails.getCountry());
        locationPostalCodeTV.setText(currentPositionDetails.getPostalCode());
        locationLatitudeTV.setVisibility(View.VISIBLE);
        locationLongitudeTV.setVisibility(View.VISIBLE);
        locationCityNameTV.setVisibility(View.VISIBLE);
        locationCountryNameTV.setVisibility(View.VISIBLE);
        locationPostalCodeTV.setVisibility(View.VISIBLE);
        saveCurrentLocationBtn.setVisibility(View.VISIBLE);
        serviceProgressBarPB.setVisibility(View.GONE);
    }

    private boolean isPlacesCollectionEmpty() {
        return UserData.getInstance(getBaseContext()).getFavouritePlaces().isEmpty();
    }
}
