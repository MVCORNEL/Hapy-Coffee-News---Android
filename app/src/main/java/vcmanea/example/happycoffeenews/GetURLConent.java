package vcmanea.example.happycoffeenews;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetURLConent extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetURLConent";

    interface OnDownloadURLComplete {
        void onDownloadURLComplete(String data, DownloadStatus status);
    }

    private DownloadStatus mDownloadStatus;
    private GetURLConent.OnDownloadURLComplete mCallBack;

    GetURLConent(GetURLConent.OnDownloadURLComplete downloadComplete) {
        mDownloadStatus = DownloadStatus.IDLE;
        mCallBack = downloadComplete;
    }

    //**************** INFORM THE CALLER THE DOWNLOAD IS DONE - POSSIBLY RETURN NULL IF THERE WAS AN ERROR******************//
    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: argument " + s);
        if (mCallBack != null) {
            mCallBack.onDownloadURLComplete(s, mDownloadStatus);
        }
    }

    @Override
    protected String doInBackground(String... urls) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        //****************  CHECKING TO SEE IF THE URL PASSED IS NULL******************//
        if (urls == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }
        //****************IF IT'S NOT NULL WE GO FURTHER******************//
        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(urls[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was" + response);

            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));


            StringBuilder htmlContent = new StringBuilder();

            String line = reader.readLine();

            while (line != null) {

                htmlContent.append(line);
                line = reader.readLine();

            }
            //////////////////////
            mDownloadStatus = DownloadStatus.OK;
            return htmlContent.toString();
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IO Exception reading data: " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground: Security Exception: Needs permisksion ?" + e.getMessage());
            //****************FINALLY BLOCK IS EXECUTED JUST BEFORE THE METHOD RETURNS******************//
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream" + e.getMessage());
                }
            }
        }

        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
