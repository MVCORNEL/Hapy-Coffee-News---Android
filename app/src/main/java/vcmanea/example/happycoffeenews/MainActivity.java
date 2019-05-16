package vcmanea.example.happycoffeenews;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GetRawData.OnDownloadComplete,
        ProcessingJSON.OnDataAvailable, OnlineRecyclerViewAdapter.OnlineNewsListener, GetImage.OnDonwloadImgComplete
        , StoredRecyclerViewAdapter.StoreNewsListener, GetURLConent.OnDownloadURLComplete {
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    private GetRawData mGetRawData;
    private ProcessingJSON mProcessingJSON;
    private GetImage mGetImage;
    private GetURLConent mGetURLConent;

    private Fragment mFragmentOnline;
    private Fragment mFragmentStore;
    private Fragment mContentFramgnet;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private StoredNewsDBHelper mStoredNewsDBHelper;
    private int elementToStorePositiom;
    private String mCurentContent;

    private UriBuilder mUriBuilder;
    private AlertDialog mAlertDialog = null;

    private Bundle bundle;
    private boolean online;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //****************  URI ******************//
        mUriBuilder = new UriBuilder(this);

        //****************  DATABASE ******************//
        mStoredNewsDBHelper = StoredNewsDBHelper.getInstance(this);


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
                        if (haveNetwork()) {
                            addFragmentOnline();
                            if (PreferencesContract.STATUS_CHANGED) {
                                recreateActivity();
                            }
                        }
                        break;
                    case R.id.offline_news:
                        addFragmentStorage();
                        break;
                    case R.id.about:
                        showAboutDialog();
                        break;
                    case R.id.settings_news:
                        addSettingsFragment();
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //**************** FIRST TIME ******************//

        if (savedInstanceState == null && haveNetwork()) {
            mGetRawData = new GetRawData(this);
            mGetRawData.execute(createUrl());
            mProcessingJSON = new ProcessingJSON(this);
        }
    }


    //****************DATA DOWNLOADING AND PROCESSING-->******************//

    public void onDownloadComplete(String data, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: data is" + data);
            mProcessingJSON.execute(data);
        } else {
            Log.e(TAG, "onDownloadComplete: failed with status" + status);
        }
    }

    //****************ONLINE FRAGMENT ADDED HERE FOR NOW******************//
    @Override
    public void onDataAvailable(List<OnlineNews> processedNews, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDataAvailable: list size  " + processedNews.size());
            OnlineNews.getNewsList().addAll(processedNews);
            //****************ONLINE FRAGMENT TRANSAZCTION******************//
            addFragmentOnline();
        } else {
            Log.e(TAG, "onDataAvailable: failed with status" + status);
        }
    }

    @Override
    public void onDownloadURLComplete(String data, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            mCurentContent = data;
            //****************DOWNLOADING THE IMAGE --> ******************//
            OnlineNews currentNews = OnlineNews.getNewsList().get(elementToStorePositiom);
            Toast.makeText(MainActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
            String imgUrl = currentNews.getImageURl();
            mGetImage = new GetImage(MainActivity.this);
            mGetImage.execute(imgUrl);
        } else {
            Log.e(TAG, "onUrlComplete: failed with status" + status);
        }

    }

    @Override
    public void onPictureComplete(byte[] image, DownloadStatus downloadStatus) {
        if (downloadStatus == DownloadStatus.OK) {
            String title = OnlineNews.getNewsList().get(elementToStorePositiom).getTitle();
            String description = OnlineNews.getNewsList().get(elementToStorePositiom).getDescription();
            String content = mCurentContent;

            if (mStoredNewsDBHelper.addData(title, description, content, image)) {
                Toast.makeText(MainActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e(TAG, "onPictureComplete: failed with status" + downloadStatus);
        }
    }


    //****************ONLINE ONCLICK LISTENERS*****************//

    @Override
    public void onClick(int i) {
        Log.d(TAG, "onClick: clicked  " + i);
        online = true;
        addContentFragment(i);
    }

    @Override
    public void onLongClick(int i) {
        elementToStorePositiom = i;
        OnlineNews currentNews = OnlineNews.getNewsList().get(elementToStorePositiom);
        //****************DOWNLOADING THE CONTENT--> ******************//
        Toast.makeText(MainActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
        String contentUrl = currentNews.getUrl();
        mGetURLConent = new GetURLConent(MainActivity.this);
        mGetURLConent.execute(contentUrl);


    }

    //****************STORE ONCLICK LISTENERS*****************//
    @Override
    public void onClickStorage(int i) {
        online = false;
        Log.d(TAG, "onClickStorage: clicked  " + i);
        addContentFragment(i);
    }

    @Override
    public void onLongClickStorage(int i) {
        StoredNewsDBHelper.getInstance(this).delete(i);
        addFragmentStorage();

    }

    //****************FRAGMENT TRANSACTIONS******************//
    //****************STORAGE FRAGMENT******************//
    private void addFragmentOnline() {
        mFragmentOnline = new OnlineNews_Fragment();
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.first_container_online, mFragmentOnline);
        mTransaction.commit();
    }

    //****************STORAGE FRAGMENT******************//
    private void addFragmentStorage() {
        mFragmentStore = new StoredNews_Fragment();
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.replace(R.id.first_container_online, mFragmentStore);
        mTransaction.commit();
        mTransaction.addToBackStack(null);
    }

    //****************IMPLEMENTATION OF ABOUT_DIALOG******************//
    public void showAboutDialog() {

        View messageView = getLayoutInflater().inflate(R.layout.dialog_about, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(messageView);
        builder.setTitle("Happy Coffee News");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("Ok,", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mAlertDialog != null && mAlertDialog.isShowing()) {
                    mAlertDialog.dismiss();
                }
            }
        });

        mAlertDialog = builder.create();
        mAlertDialog.setCanceledOnTouchOutside(true);
        TextView tv = messageView.findViewById(R.id.about_version);
        tv.setText("v" + BuildConfig.VERSION_NAME);
        mAlertDialog.show();
    }


    //****************MANAGING SETTINGS FRAGMENT******************//
    public void addSettingsFragment() {
        mFragmentStore = new SettingsPreference_Fragment();
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.replace(R.id.first_container_online, mFragmentStore);
        mTransaction.commit();
        mTransaction.addToBackStack(null);
    }

    //****************MANAGING CONTENT FRAGMENT*****************//
    public void addContentFragment(int i) {
        bundle = new Bundle();
        if (online) {
            Log.d(TAG, "addContentFragment ONLINE: " + OnlineNews.getNewsList().get(i).getUrl());
            bundle.putInt("URL", i);
            online = true;
            bundle.putBoolean("ONLINE", online);
        } else {
            bundle.putInt("URL", i);
            online = false;
            bundle.putBoolean("ONLINE", online);
        }

        mContentFramgnet = new Content_Fragment();
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.replace(R.id.first_container_online, mContentFramgnet);
        mContentFramgnet.setArguments(bundle);
        mTransaction.commit();
        mTransaction.addToBackStack(null);

    }

    //****************GENERATE URL******************//
    public String createUrl() {
        Log.d(TAG, "createUrl url is : " + mUriBuilder.createUri());
        return mUriBuilder.createUri();
    }
    //****************SIMPLE METHODS******************//

    //****************Recreate Activity after the Settings are changed******************//
    public void recreateActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        PreferencesContract.STATUS_CHANGED = false;

    }

    public boolean haveNetwork() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());

    }

    //****************CALLBACKS*****************//
    @Override
    protected void onStop() {
        super.onStop();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        if (PreferencesContract.STATUS_CHANGED) {
            recreateActivity();
        }
        super.onBackPressed();
    }

}