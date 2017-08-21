package pl.krystiankrawczyk.favouriteplaces.places.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.krystiankrawczyk.favouriteplaces.R;
import pl.krystiankrawczyk.favouriteplaces.places.adapters.FavouritePlacesListAdapter;
import pl.krystiankrawczyk.favouriteplaces.preferences.UserData;

/**
 * Created by Krystian Krawczyk on 18.08.2017.
 */

public class PlacesActivity extends Activity {

    @BindView(R.id.places_activity_places_list_RV)
    RecyclerView placesListLV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_list_activity);
        ButterKnife.bind(this);
        setupAdapter();
    }

    private void setupAdapter() {
        if (isPlacesDataEmpty()) {
            Toast.makeText(this, "First add at least one place to your list", Toast.LENGTH_SHORT).show();
        } else {
            placesListLV.setLayoutManager(new LinearLayoutManager(this));
            placesListLV.setAdapter(new FavouritePlacesListAdapter(UserData.getInstance(getBaseContext()).getFavouritePlaces()));
        }
    }

    private boolean isPlacesDataEmpty() {
        return UserData.getInstance(getBaseContext()).getFavouritePlaces().isEmpty();
    }
}
