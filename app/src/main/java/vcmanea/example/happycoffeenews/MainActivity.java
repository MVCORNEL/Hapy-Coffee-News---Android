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

import java.util.List;

public class MainActivity extends AppCompatActivity implements GetRawData.OnDownloadComplete, ProcessingJSON.OnDataAvailable {
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private ProcessingJSON mProcessingJSON;

    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private boolean addFragmentFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                }

                return true;
            }
        });

        //****************DEALING WITH THE FRAGMENTS******************//
        if (savedInstanceState == null) {
            addFragmentFirstTime = true;
        }

        //****************  DOWNLOAD RAW DATA--PASSING THE LISTENER IN THE CONSTRUCTOR-- FOR THAT PARTICULAT CASE IS BETTER TO PASS THE CALL BACK INTERFACE IN CONSTRUCOR THOUGH WE CAN SET IT LIKE THE BUTTONDS CALLBACS ******************//
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute("https://newsapi.org/v2/top-headlines?country=gb&category=technology&apiKey=09dbc3fd2b4e43e4900b114f60eaa190");
        //**********************************//
        mProcessingJSON = new ProcessingJSON(this);


    }

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
    @Override
    public void onDataAvailable(List<OnlineNews> processedNews, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDataAvailable: list size  " + processedNews.size());
            OnlineNews.getCountryList().addAll(processedNews);

            addFragmentOnline();
        } else {
            Log.e(TAG, "onDataAvailable: failed with status" + status);
        }
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


}
