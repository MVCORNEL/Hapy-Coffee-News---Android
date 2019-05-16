package vcmanea.example.happycoffeenews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;


public class Content_Fragment extends Fragment {
    View myViewHolder;
    WebView webView;
    TextView mTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myViewHolder = inflater.inflate(R.layout.fragment_content, container, false);


        webView = myViewHolder.findViewById(R.id.web_view_id);
        Bundle bundle = this.getArguments();
        int i = bundle.getInt("URL");

        boolean online = bundle.getBoolean("ONLINE");
        if (online) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(OnlineNews.getNewsList().get(i).getUrl());
        } else {
            webView.loadData(StoredNewsDBHelper.getInstance(getActivity()).getContent(i), "text/html", "UTF-8");
        }

        return myViewHolder;
    }



}


