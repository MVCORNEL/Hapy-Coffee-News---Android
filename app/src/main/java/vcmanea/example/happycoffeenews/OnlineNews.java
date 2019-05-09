package vcmanea.example.happycoffeenews;

public class OnlineNews {

    private String mTitle;
    private String mDescription;
    private String mContent;
    private String mUrl;
    private String mImageURl;

    public OnlineNews(String title, String description, String content, String url, String imageURl) {
        mTitle = title;
        mDescription = description;
        mContent = content;
        mUrl = url;
        mImageURl = imageURl;
    }

    String getTitle() {
        return mTitle;
    }

    String getDescription() {
        return mDescription;
    }

    String getContent() {
        return mContent;
    }

    String getUrl() {
        return mUrl;
    }

    String getImageURl() {
        return mImageURl;
    }


    @Override
    public String toString() {
        return "OnlineNews{" +
                "mTitle='" + mTitle + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mContent='" + mContent + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mImageURl='" + mImageURl + '\'' +
                '}';
    }
}
