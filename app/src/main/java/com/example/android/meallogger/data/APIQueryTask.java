package com.example.android.meallogger.data;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.meallogger.CreateMealActivity;
import com.example.android.meallogger.utils.NetworkUtils;
import com.example.android.meallogger.utils.UsdaAPIUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;

public class APIQueryTask extends AsyncTask<String, Void, String> {
    private Callback mCallbackFC;
    private String mApiMode;
    private static final String TAG = APIQueryTask.class.getSimpleName();

    public APIQueryTask(Callback callback) {
        mCallbackFC = callback;
    }

    public interface Callback {
        void handleSearchResults(ArrayList<FoodId> ids);
        void handleDetailResults(MealItem food);
    }

    // Param[0]: Url
    // Param[1]: "search" or "detail"
    // "search": querying food ID of food String
    // "detail": getting nutritional information on foodID
    @Override
    protected String doInBackground(String... params) {
        String apiURL = params[0];
        mApiMode = params[1];
        String resultsJSON = null;
        try {
            resultsJSON = NetworkUtils.doHTTPGet(apiURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultsJSON;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s!=null){
            switch (mApiMode){
            case "search":
                ArrayList<FoodId> sResults = UsdaAPIUtils.parseSearchJSON(s);
                mCallbackFC.handleSearchResults(sResults);
                return;
            case "detail":
                MealItem dResults = UsdaAPIUtils.parseDetailJSON(s);
                mCallbackFC.handleDetailResults(dResults);
                return;
            default:
        }}
    }
}
