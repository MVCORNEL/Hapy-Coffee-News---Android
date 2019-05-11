package vcmanea.example.happycoffeenews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GetRawData.OnDownloadComplete, ProcessingJSON.OnDataAvailable, OnlineRecyclerViewAdapter.OnlineNewsListener, GetImage.OnDonwloadImgComplete {
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    private GetRawData getRawData;
    private ProcessingJSON mProcessingJSON;
    private GetImage getImage;

    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private StoredNewsDBHelper mStoredNewsDBHelper;
    private int elementToStorePositiom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //****************  DATABASE ******************//
        mStoredNewsDBHelper = StoredNewsDBHelper.getInstance(this);
        StoredNewsDBHelper.getAllData();


        //****************  TOOLBAR ******************//
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //****************  TOOGLE ******************//
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_toggle, R.string.close_toggle);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        //****************  OnClickListener for NAVIGATION VIEW******************//
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.online_news:
                        addFragmentOnline();
                        break;
                    case R.id.offline_news:
                        addFragmentStorage();
                        break;
                }
                return true;
            }
        });

        //****************  DOWNLOAD RAW DATA FOR THE FIRST TIME-- > ADDING FRAGMENT FOR THE FORST TIME  ******************//
        if (savedInstanceState == null) {
            getRawData = new GetRawData(this);
            getRawData.execute("https://newsapi.org/v2/top-headlines?country=gb&category=technology&apiKey=09dbc3fd2b4e43e4900b114f60eaa190");
            //**********************************//
            mProcessingJSON = new ProcessingJSON(this);
        }
    }
    //****************SECTION 2-->******************//


    //****************CALLBACKS-->******************//
    //****************CODE WHICH WILL NE EXECUTED AFTER THE NOTIFY WHEN DATA HAS BEEN DOWNLOADED-THIS HAPPENS IN POST EXECUTE-CALLBACK PATTERN-->******************//
    public void onDownloadComplete(String data, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: data is" + data);
            mProcessingJSON.execute(data);
        } else {
            Log.e(TAG, "onDownloadComplete: failed with status" + status);
        }
    }

    //****************PASSING THE DATA TO THE LIST--> IN ORDER TO BE ACCESSED FOR THE RECYCLER VIEW ADAPTER******************//
    //****************PONLINE FRAGMENT ADDED HERE FOR NOW******************//
    @Override
    public void onDataAvailable(List<OnlineNews> processedNews, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDataAvailable: list size  " + processedNews.size());
            OnlineNews.getNewsList().addAll(processedNews);
            //****************DEALING WITH THE FRAGMENTS******************//
            addFragmentOnline();
        } else {
            Log.e(TAG, "onDataAvailable: failed with status" + status);
        }
    }

    //****************ADDING RECORD TO THE DATABASE******************//
    //****************WHAT CODE WILL BE EXECUTED AFTER THE IMAGE IS COMPLET DOWNLOADED******************//
    @Override
    public void onPictureComplete(byte[] image, DownloadStatus downloadStatus) {
        if (downloadStatus == DownloadStatus.OK) {
            String title = OnlineNews.getNewsList().get(elementToStorePositiom).getTitle();
            String description = OnlineNews.getNewsList().get(elementToStorePositiom).getDescription();
            String content = OnlineNews.getNewsList().get(elementToStorePositiom).getContent();

            if (mStoredNewsDBHelper.addData(title, description, content, image)) {
                Toast.makeText(MainActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e(TAG, "onPictureComplete: failed with status" + downloadStatus);
        }
    }
    //****************CLICK LISTENERS*****************//
    //****************CALBACKS FOR THE FIRST RECYCLERVIEW - ONLINE ONE -RecyclerViewOnline *****************//
    @Override
    public void onClick(int i) {
        Log.d(TAG, "onClick: clicked  " + i);
    }

    @Override
    public void onLongClick(int i) {
        OnlineNews currentNews = OnlineNews.getNewsList().get(i);
        //****************DOWNLOADING THE IMAGE --> ******************//
        String imgUrl = currentNews.getImageURl();
        getImage = new GetImage(this);
        getImage.execute(imgUrl);
        elementToStorePositiom = i;
    }


    //****************DEALING WITH THE FRAGMENTS******************//
    //****************ADDING ONLINE NEWS FRAGMENT TO THE ACTIVITY******************//
    private void addFragmentOnline() {
        mFragment = new OnlineNews_Fragment();
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.first_container_online, mFragment);
        mTransaction.commit();
    }


    //****************ADDING ONLINE NEWS FRAGMENT TO THE ACTIVITY******************//
    private void addFragmentStorage() {
        mFragment = new StoredNews_Fragment();
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.replace(R.id.first_container_online, mFragment);
        mTransaction.commit();


    }

}
