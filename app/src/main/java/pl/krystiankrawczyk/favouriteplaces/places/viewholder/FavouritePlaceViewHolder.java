package pl.krystiankrawczyk.favouriteplaces.places.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.krystiankrawczyk.favouriteplaces.R;

/**
 * Created by Krystian Krawczyk on 18.08.2017.
 */

public class FavouritePlaceViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.favourite_place_item_id_tv)
    TextView itemIdTV;
    @BindView(R.id.place_list_item_latitude_value_tv)
    TextView itemLatitudeValueTV;
    @BindView(R.id.place_list_item_longitude_value_tv)
    TextView itemLongitudeValueTV;

    public FavouritePlaceViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getItemIdTV() {
        return this.itemIdTV;
    }

    public TextView getItemLatitudeValueTV() {
        return this.itemLatitudeValueTV;
    }

    public TextView getItemLongitudeValueTV() {
        return this.itemLongitudeValueTV;
    }
}
