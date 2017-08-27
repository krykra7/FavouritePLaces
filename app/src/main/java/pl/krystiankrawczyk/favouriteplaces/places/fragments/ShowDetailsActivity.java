package pl.krystiankrawczyk.favouriteplaces.places.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.krystiankrawczyk.favouriteplaces.R;
import pl.krystiankrawczyk.favouriteplaces.locationservice.FavouritePlaceData;

/**
 * Created by Krystian Krawczyk on 23.08.2017.
 */

public class ShowDetailsActivity extends Activity {

    @BindView(R.id.details_activity_city_name_tv)
    TextView detailsCityNameTV;
    @BindView(R.id.details_activity_country_name_tv)
    TextView detailsCountryNameTV;
    @BindView(R.id.details_activity_latitude_tv)
    TextView detailsLatitudeTV;
    @BindView(R.id.details_activity_longitude_tv)
    TextView detailsLongitudeTV;
    @BindView(R.id.details_activity_postal_code_tv)
    TextView detailsPostalCodeTV;
    @BindView(R.id.details_activity_place_description_et)
    TextView detailsDescriptionTV;
    @BindView(R.id.details_activity_street_tv)
    TextView detailsStreetTV;

    private FavouritePlaceData data;

    @OnClick(R.id.details_activity_open_in_maps)
    public void onOpenInMapsClicked() {
        String uri = String.format("geo:%f,%f", data.getLatitude(), data.getLongitude());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_details_activity);
        ButterKnife.bind(this);
        setupView(getFavouritePlaceDataFromBundle(getIntent().getExtras()));
    }

    private void setupView(FavouritePlaceData data) {
        this.data = data;
        detailsCityNameTV.setText(data.getCity());
        detailsCountryNameTV.setText(data.getCountry());
        detailsLatitudeTV.setText(String.valueOf(data.getLatitude()));
        detailsLongitudeTV.setText(String.valueOf(data.getLongitude()));
        detailsPostalCodeTV.setText(data.getPostalCode());
        detailsDescriptionTV.setText(data.getPlaceDescription());
        detailsStreetTV.setText(data.getStreet());
    }

    private FavouritePlaceData getFavouritePlaceDataFromBundle(Bundle bundle) {
        return (new Gson()).fromJson(bundle.getString(FavouritePlaceData.class.getName()), FavouritePlaceData.class);
    }
}
