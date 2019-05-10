package vcmanea.example.happycoffeenews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OnlineNews_Fragment extends Fragment {
    private static final String TAG = "OnlineNews_Fragment";
    private View mHolderView;
    private RecyclerView mRecyclerViev;
    private OnlineRecyclerViewAdapter mOnlineRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starts");
        mHolderView = inflater.inflate(R.layout.fragment_online_news, container, false);

        initUI();

        Log.d(TAG, "onCreateView: ends");

        return mHolderView;
    }


    private void initUI() {
        mRecyclerViev = mHolderView.findViewById(R.id.online_recycler_view);
        mRecyclerViev.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));

        mOnlineRecyclerViewAdapter = new OnlineRecyclerViewAdapter(getContext(), OnlineNews.getCountryList());
        mRecyclerViev.setAdapter(mOnlineRecyclerViewAdapter);
        mOnlineRecyclerViewAdapter.notifyDataSetChanged();

    }


}
