package vcmanea.example.happycoffeenews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OnlineNews_Fragment extends Fragment {
    View mHolderView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mHolderView = inflater.inflate(R.layout.fragment_online_news, container, false);

        return mHolderView;


    }
}
