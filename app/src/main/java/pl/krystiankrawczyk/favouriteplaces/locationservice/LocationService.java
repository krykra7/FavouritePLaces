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
import android.widget.EditText;
import android.widget.LinearLayout;
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

    @BindView(R.id.location_service_properties_layout)
    LinearLayout locationPropertiesLL;
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
    @BindView(R.id.location_service_place_description_et)
    EditText locationDescriptionET;
    @BindView(R.id.location_service_street_tv)
    TextView locationStreetTv;

    private FavouritePlaceData currentPositionDetails = new FavouritePlaceData();
    private String locationProvider;
    private LocationManager locationManager;

    @OnClick(R.id.location_service_save_current_location_btn)
    public void onSaveLocationClicked() {
        currentPositionDetails.setPlaceDescription(locationDescriptionET.getText().toString());
        UserData.getInstance(getBaseContext()).addNewFavouritePlace(currentPositionDetails);
        Toast.makeText(this, R.string.location_service_saved, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, getString(R.string.location_service_provider_enabled) + provider
                , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, getString(R.string.location_service_provider_disabled) + provider,
                Toast.LENGTH_SHORT).show();
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
        currentPositionDetails.setStreet(getStreet());
        setupFetchedInformations();
    }

    @Nullable
    private List<Address> getGeocoderAddress() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            return geocoder.getFromLocation(currentPositionDetails.getLatitude(), currentPositionDetails.getLongitude(),
                    GEOCODER_MAX_NUMBER_OF_RESULTS);
        } catch (IOException exception) {
            Log.e(Geocoder.class.getName(), getString(R.string.location_service_geocoder), exception);
        }
        return null;
    }

    private String getStreet() {
        List<Address> addressList = getGeocoderAddress();

        if (addressList != null && addressList.size() > 0) {
            return addressList.get(0).getThoroughfare();
        } else {
            return getString(R.string.location_service_resolve_street);
        }
    }

    private String getLocality() {
        List<Address> addressList = getGeocoderAddress();

        if (addressList != null && addressList.size() > 0) {
            return addressList.get(0).getLocality();
        } else {
            return getString(R.string.location_service_resolve_city);
        }
    }

    private String getPostalCode() {
        List<Address> addressList = getGeocoderAddress();

        if (addressList != null && addressList.size() > 0) {
            return addressList.get(0).getPostalCode();
        } else {
            return getString(R.string.location_service_resolve_postal);
        }
    }

    private String getCountryName() {
        List<Address> addressList = getGeocoderAddress();

        if (addressList != null && addressList.size() > 0) {
            return addressList.get(0).getCountryName();
        } else {
            return getString(R.string.location_service_resolve_country);
        }
    }

    private void setupFetchedInformations() {
        locationLatitudeTV.setText(String.valueOf(currentPositionDetails.getLatitude()));
        locationLongitudeTV.setText(String.valueOf(currentPositionDetails.getLongitude()));
        locationCityNameTV.setText(currentPositionDetails.getCity());
        locationCountryNameTV.setText(currentPositionDetails.getCountry());
        locationPostalCodeTV.setText(currentPositionDetails.getPostalCode());
        locationStreetTv.setText(currentPositionDetails.getStreet());
        locationPropertiesLL.setVisibility(View.VISIBLE);
        serviceProgressBarPB.setVisibility(View.GONE);
    }
}
