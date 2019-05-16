package vcmanea.example.happycoffeenews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class StoredNews_Fragment extends Fragment {
    private static final String TAG = "StoredNews_Fragment";
    private View mHolderView;
    private RecyclerView mRecyclerView;
    private StoredRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starts");
        mHolderView = inflater.inflate(R.layout.fragment_stores_news, container, false);
        initUI();
        Log.d(TAG, "onCreateView: ended");
        return mHolderView;
    }

    private void initUI(){
        mRecyclerView= mHolderView.findViewById(R.id.storage_recycle_view);
        adapter=new StoredRecyclerViewAdapter(getContext(),StoredNewsDBHelper.getAllData(),(MainActivity)getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL,false));
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }




}
