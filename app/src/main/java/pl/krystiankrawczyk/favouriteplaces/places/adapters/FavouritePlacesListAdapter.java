package pl.krystiankrawczyk.favouriteplaces.places.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import pl.krystiankrawczyk.favouriteplaces.R;
import pl.krystiankrawczyk.favouriteplaces.locationservice.FavouritePlaceData;
import pl.krystiankrawczyk.favouriteplaces.places.viewholder.FavouritePlaceViewHolder;

/**
 * Created by Krystian Krawczyk on 16.08.2017.
 */

public class FavouritePlacesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Map<Integer, FavouritePlaceData> favouritePlacesDataMap;

    public FavouritePlacesListAdapter(Map<Integer, FavouritePlaceData> favouritePlacesData) {
        this.favouritePlacesDataMap = favouritePlacesData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_list_item, parent, false);
        return new FavouritePlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FavouritePlaceViewHolder) {
            FavouritePlaceData placeData = favouritePlacesDataMap.get(position);
            ((FavouritePlaceViewHolder) holder).getItemIdTV().setText(String.valueOf(position));
            ((FavouritePlaceViewHolder) holder).getItemLatitudeValueTV().
                    setText(String.valueOf(placeData.getLatitude()));
            ((FavouritePlaceViewHolder) holder).getItemLongitudeValueTV().
                    setText(String.valueOf(placeData.getLongitude()));
        }
    }

    @Override
    public int getItemCount() {
        return favouritePlacesDataMap.size();
    }
}
