package vcmanea.example.happycoffeenews;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import static vcmanea.example.happycoffeenews.PreferencesContract.*;

public class UriBuilder {
    private static final String TAG = "Uri";
    private String mBaseURL="https://newsapi.org/v2/top-headlines?";
    private String mCountry;
    private String mCategory;
    private  SharedPreferences mSharedPreferences;
    private Context mContext;


     UriBuilder(Context context){
        mContext=context;
        mSharedPreferences=PreferenceManager.getDefaultSharedPreferences(mContext);
        mCountry = mSharedPreferences.getString(LANGUAGE_LIST, "ro");
        mCategory=mSharedPreferences.getString(CATEGORY_LIST,"science");
    }

    //****************LINK - BUILDING OUR URL WHERE WE WILL FETCH DATA FROM******************//
     String createUri() {

        Log.d(TAG, "createUri: start");
        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("country", mCountry)
                .appendQueryParameter("category", mCategory)
                .appendQueryParameter("apiKey", "09dbc3fd2b4e43e4900b114f60eaa190")
                .build().toString();

    }





}
