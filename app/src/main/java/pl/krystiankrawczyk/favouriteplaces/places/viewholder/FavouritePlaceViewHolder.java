package pl.krystiankrawczyk.favouriteplaces.places.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.krystiankrawczyk.favouriteplaces.R;
import pl.krystiankrawczyk.favouriteplaces.locationservice.FavouritePlaceData;
import pl.krystiankrawczyk.favouriteplaces.places.fragments.ShowDetailsActivity;

/**
 * Created by Krystian Krawczyk on 18.08.2017.
 */

public class FavouritePlaceViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.favourite_place_item_id_tv)
    TextView itemIdTV;
    @BindView(R.id.place_list_item_street_value_tv)
    TextView itemLatitudeValueTV;
    @BindView(R.id.place_list_item_city_value_tv)
    TextView itemLongitudeValueTV;

    private FavouritePlaceData placeData;
    private Context context;

    public FavouritePlaceViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.place_list_details_arrow)
    public void onDetailsClick() {
        Intent intent = new Intent(context, ShowDetailsActivity.class);
        intent.putExtra(FavouritePlaceData.class.getName(), new Gson().toJson(placeData));
        context.startActivity(intent);
    }

    public TextView getItemIdTV() {
        return this.itemIdTV;
    }

    public TextView getItemStreetValueTV() {
        return this.itemLatitudeValueTV;
    }

    public TextView getItemCityValueTV() {
        return this.itemLongitudeValueTV;
    }

    public void setPlaceData(FavouritePlaceData placeData) {
        this.placeData = placeData;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
