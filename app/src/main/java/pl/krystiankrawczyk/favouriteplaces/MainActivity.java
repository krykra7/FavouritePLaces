package pl.krystiankrawczyk.favouriteplaces;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.krystiankrawczyk.favouriteplaces.locationservice.LocationService;
import pl.krystiankrawczyk.favouriteplaces.places.activities.PlacesActivity;
import pl.krystiankrawczyk.favouriteplaces.preferences.UserData;

public class MainActivity extends AppCompatActivity {

    private final static int ACCESS_FINE_CODE = 1;
    private final static int ACCESS_COARSE_CODE = 2;
    private final static int ACCESS_INTERNET_CODE = 3;

    @OnClick(R.id.main_activity_save_actual_location_btn)
    public void onClickSaveLocation() {
        Intent intent = new Intent(this, LocationService.class);
        startActivity(intent);
    }

    @OnClick(R.id.main_activity_view_saved_places_btn)
    public void onClickViewSaved() {
        Intent intent = new Intent(this, PlacesActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        promptUserEnableGPS();
        UserData.getInstance(getBaseContext());
    }

    private void promptUserEnableGPS() {
        setPermissions();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    private void setPermissions() {
        int accessFineLocationCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int accessCoarseCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessInternetCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (isPermissionDenied(accessFineLocationCheck)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_CODE);
        }
        if (isPermissionDenied(accessCoarseCheck)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_COARSE_CODE);
        }
        if (isPermissionDenied(accessInternetCheck)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, ACCESS_INTERNET_CODE);
        }
    }

    private boolean isPermissionDenied(int permissionCode) {
        return permissionCode != PackageManager.PERMISSION_GRANTED;
    }
}
