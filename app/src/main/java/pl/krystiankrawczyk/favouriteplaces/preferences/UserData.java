package pl.krystiankrawczyk.favouriteplaces.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import pl.krystiankrawczyk.favouriteplaces.locationservice.FavouritePlaceData;

/**
 * Created by Krystian Krawczyk on 16.08.2017.
 */

public class UserData {

    private static final String PREFERENCES_NAME = "PREFERENCES";
    private static final String PLACES_COUNT = "PLACES_COUNT";
    private static final String FAVOURITE_PLACES_KEY = "PLACES";
    private static final String DEFAULT_EMPTY_STRING = "";
    private static final int DEFAULT_INT_VALUE = 0;

    private static UserData userData;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private Map<Integer, FavouritePlaceData> testFavouritePlacesMap;


    private UserData(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        testFavouritePlacesMap = getFavouritePlacesMap();
    }

    public static UserData getInstance(Context context) {
        if (userData == null) {
            userData = new UserData(context);
        }
        return userData;
    }

    public void saveFavouritePlacesCount() {
        editor.putInt(PLACES_COUNT, testFavouritePlacesMap.size()).apply();
    }

    public Integer getFavouritePlacesCount() {
        return sharedPreferences.getInt(PLACES_COUNT, DEFAULT_INT_VALUE);
    }

    public void addNewFavouritePlace(Integer key, FavouritePlaceData value) {
        testFavouritePlacesMap.put(key, value);
        saveFavouritePlacesCount();
    }

    public Map<Integer, FavouritePlaceData> getFavouritePlaces() {
        return this.testFavouritePlacesMap;
    }

    public void saveFavouritePlacesMap() {
        Gson gson = new Gson();
        String jsonPlacesMapString = gson.toJson(testFavouritePlacesMap);
        editor.putString(FAVOURITE_PLACES_KEY, jsonPlacesMapString).apply();
    }

    private Map<Integer, FavouritePlaceData> getFavouritePlacesMap() {
        String jsonPlacesMapString = sharedPreferences.getString(FAVOURITE_PLACES_KEY, DEFAULT_EMPTY_STRING);
        if (jsonPlacesMapString.isEmpty()) {
            return new HashMap<>();
        } else {
            Gson gson = new Gson();
            Type customType = new TypeToken<HashMap<Integer, FavouritePlaceData>>() {
            }.getType();
            return gson.fromJson(jsonPlacesMapString, customType);
        }
    }

}
