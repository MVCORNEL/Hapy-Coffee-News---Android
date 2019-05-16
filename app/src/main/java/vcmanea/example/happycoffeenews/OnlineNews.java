package vcmanea.example.happycoffeenews;

import java.util.ArrayList;
import java.util.List;

public class OnlineNews {
    private static List<OnlineNews> myListNews;
    private String mTitle;
    private String mDescription;
    private String mUrl;
    private String mImageURl;

    public OnlineNews(String title, String description, String url, String imageURl) {
        mTitle = title;
        mDescription = description;
        mUrl = url;
        mImageURl = imageURl;
    }

    String getTitle() {
        return mTitle;
    }

    String getDescription() {
        return mDescription;
    }

    String getUrl() {
        return mUrl;
    }

    String getImageURl() {
        return mImageURl;
    }


    public static List<OnlineNews> getNewsList() {
        if (myListNews == null) {
            myListNews = new ArrayList<>();
        }
        return myListNews;

    }

}
