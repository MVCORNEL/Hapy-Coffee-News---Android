package vcmanea.example.happycoffeenews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StoredNews_Fragment extends Fragment {

    View holderView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        holderView = inflater.inflate(R.layout.fragment_stores_news, container, false);

        return holderView;
    }
}
