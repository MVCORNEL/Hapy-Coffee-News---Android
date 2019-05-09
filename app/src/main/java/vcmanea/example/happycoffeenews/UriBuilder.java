package vcmanea.example.happycoffeenews;

import android.net.Uri;
import android.util.Log;

public class UriBuilder {
    private static final String TAG = "Uri";
    private String mBaseURL;
    private String mCountry;
    private String mCategory;


    //****************LINK - BUILDING OUR URL THAT WE WILL FETCH DATA FROM******************//
    private String createUri(String country, String category) {
        Log.d(TAG, "createUri: start");
        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("country", country)
                .appendQueryParameter("category", category)
                .appendQueryParameter("apiKey", "09dbc3fd2b4e43e4900b114f60eaa190")
                .build().toString();

    }



}
