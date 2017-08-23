package pl.krystiankrawczyk.favouriteplaces.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.krystiankrawczyk.favouriteplaces.locationservice.FavouritePlaceData;

/**
 * Created by Krystian Krawczyk on 16.08.2017.
 */

public class UserData {

    private static final String PREFERENCES_NAME = "PREFERENCES";
    private static final String FAVOURITE_PLACES_KEY = "PLACES";
    private static final String DEFAULT_EMPTY_STRING = "";

    private static UserData userData;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private List<FavouritePlaceData> favouritePlacesDataList;


    private UserData(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        favouritePlacesDataList = getFavouritePlacesMap();
    }

    public static UserData getInstance(Context context) {
        if (userData == null) {
            userData = new UserData(context);
        }
        return userData;
    }

    public void addNewFavouritePlace(FavouritePlaceData value) {
        favouritePlacesDataList.add(value);
    }

    public void removeFavouritePlace(int position) {
        favouritePlacesDataList.remove(position);
    }

    public void swapFavouritePlaceElements(int position, int targetPosition) {
        Collections.swap(favouritePlacesDataList, position, targetPosition);
    }

    public List<FavouritePlaceData> getFavouritePlaces() {
        return this.favouritePlacesDataList;
    }

    public void saveFavouritePlacesMap() {
        Gson gson = new Gson();
        String jsonPlacesMapString = gson.toJson(favouritePlacesDataList);
        editor.putString(FAVOURITE_PLACES_KEY, jsonPlacesMapString).apply();
    }

    private List<FavouritePlaceData> getFavouritePlacesMap() {
        String jsonPlacesMapString = sharedPreferences.getString(FAVOURITE_PLACES_KEY, DEFAULT_EMPTY_STRING);
        if (jsonPlacesMapString.isEmpty()) {
            return new ArrayList<>();
        } else {
            Gson gson = new Gson();
            Type customType = new TypeToken<ArrayList<FavouritePlaceData>>() {
            }.getType();
            return gson.fromJson(jsonPlacesMapString, customType);
        }
    }
}
