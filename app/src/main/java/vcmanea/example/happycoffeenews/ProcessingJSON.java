package vcmanea.example.happycoffeenews;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProcessingJSON extends AsyncTask<String, Void, List<OnlineNews>> {
    private static final String TAG = "ProcessingJSON";

    interface OnDataAvailable {
        void onDataAvailable(List<OnlineNews> processedNews, DownloadStatus status);
    }

    private OnDataAvailable mOnDataAvailable;
    private List<OnlineNews> mNewsList;
    private DownloadStatus status;


    ProcessingJSON(OnDataAvailable onDataAvailable) {
        mOnDataAvailable = onDataAvailable;
    }

    //**************** INFORM THE CALLER THE PROCESSING IS DONE - POSSIBLY RETURN NULL IF THERE WAS AN ERROR******************//
    @Override
    protected void onPostExecute(List<OnlineNews> onlineNews) {
        Log.d(TAG, "onPostExecute: data processed");
        if (mOnDataAvailable != null) {
            mOnDataAvailable.onDataAvailable(onlineNews, status);
        }
    }

    @Override
    protected List<OnlineNews> doInBackground(String... data) {
        Log.d(TAG, "doInBackground: starts" + Thread.currentThread().getId());

        mNewsList = new ArrayList<>();
        try {
            JSONObject jsonData = new JSONObject(data[0]);
            JSONArray itemsArray = jsonData.getJSONArray("articles");

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject jsonNews = itemsArray.getJSONObject(i);

                String title = jsonNews.getString("title");
                String descriptiom = jsonNews.getString("description");
                String url = jsonNews.getString("url");
                String urlToImage = jsonNews.getString("urlToImage");

                OnlineNews onlineNewsObject = new OnlineNews(title, descriptiom, url, urlToImage);
                mNewsList.add(onlineNewsObject);
            }
            status = DownloadStatus.OK;
            Log.d(TAG, "doInBackground: ends");
            return mNewsList;
        } catch (JSONException e) {
            e.getStackTrace();
            Log.e(TAG, "onDownloadComplete: error processing Json data" + e.getMessage());

        }
        status = DownloadStatus.FAILED_OR_EMPTY;
        Log.d(TAG, "doInBackground: ends" + status);
        return null;
    }

}
