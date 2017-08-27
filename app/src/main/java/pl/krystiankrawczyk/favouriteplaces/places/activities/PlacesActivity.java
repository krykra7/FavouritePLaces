package pl.krystiankrawczyk.favouriteplaces.places.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
    RecyclerView placesListRV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_list_activity);
        ButterKnife.bind(this);
        setupAdapter();
    }

    private void setupAdapter() {
        if (isPlacesDataEmpty()) {
            Toast.makeText(this, R.string.places_activity_message_add_one, Toast.LENGTH_SHORT).show();
        } else {
            placesListRV.setLayoutManager(new LinearLayoutManager(this));
            placesListRV.setAdapter(new FavouritePlacesListAdapter(UserData.getInstance(getBaseContext())
                    .getFavouritePlaces(), this));
            placesListRV.addItemDecoration(new DividerItemDecoration(placesListRV.getContext()
                    , DividerItemDecoration.VERTICAL));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (placesListRV.getAdapter() instanceof FavouritePlacesListAdapter) {
            setRecyclerViewItemTouchListener((FavouritePlacesListAdapter) placesListRV.getAdapter());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserData.getInstance(getBaseContext()).saveFavouritePlacesMap();
    }

    private boolean isPlacesDataEmpty() {
        return UserData.getInstance(getBaseContext()).getFavouritePlaces().isEmpty();
    }

    private void setRecyclerViewItemTouchListener(final FavouritePlacesListAdapter adapter) {
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.
                SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN
                , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView
                    .ViewHolder target) {
                int position = viewHolder.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();
                UserData.getInstance(getBaseContext()).swapFavouritePlaceElements(position, targetPosition);
                adapter.notifyItemMoved(position, targetPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                UserData.getInstance(getBaseContext()).removeFavouritePlace(position);
                adapter.notifyItemRemoved(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(placesListRV);
    }
}
