package vcmanea.example.happycoffeenews;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetImage extends AsyncTask<String, Void, byte[]> {
    private static final String TAG = "GetImage";

    interface OnDonwloadImgComplete {
        void onPictureComplete(byte[] image,DownloadStatus mDownloadStatus);
    }

    private OnDonwloadImgComplete mOnDonwloadImgComplete;
    private DownloadStatus mDownloadStatus;

    GetImage(OnDonwloadImgComplete onDonwloadImgComplete) {
        mOnDonwloadImgComplete = onDonwloadImgComplete;

    }

    //**************** INFORM THE CALLER THE DOWNLOADING IS DONE - POSSIBLY RETURN NULL IF THERE WAS AN ERROR******************//
    @Override
    protected void onPostExecute(byte[] bytes) {
        if (mOnDonwloadImgComplete != null) {
            mOnDonwloadImgComplete.onPictureComplete(bytes,mDownloadStatus);
        }
    }

    public byte[] doInBackground(String... urls) {
        Log.d(TAG, "doInBackground: starts");
        //****************  CHECKING TO SEE IF THE URL PASSED IS NULL******************//
        if (urls == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }

        HttpURLConnection connection = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream buffer = null;
        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(urls[0]);


            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was" + response);


            InputStream inputStream = connection.getInputStream();
            bis = new BufferedInputStream(inputStream, 128);
            buffer = new ByteArrayOutputStream();
            //We create an array of bytes
            byte[] data = new byte[50];
            int current;

            while ((current = bis.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, current);
            }

            mDownloadStatus = DownloadStatus.OK;
            return buffer.toByteArray();


        } catch (MalformedURLException e) {

            Log.e(TAG, "doInBackground:Invalid URL" + e.getMessage());
        } catch (IOException e) {

            Log.e(TAG, "doInBackground:IO Exception reading data:" + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground:Error  Security Exception: Needs permisksion ?\"" + e.getMessage());
            //****************FINALLY BLOCK IS EXECUTED JUST BEFORE THE METHOD RETURNS******************//
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream" + e.getMessage());
                }
            }
            if (buffer != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream" + e.getMessage());
                }
            }

        }

        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;


    }
}