package vcmanea.example.happycoffeenews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class OnlineRecyclerViewAdapter extends RecyclerView.Adapter<OnlineRecyclerViewAdapter.ViewHolderOnline> {
    private static final String TAG = "OnlineRecyclerViewAdapt";
    List<OnlineNews> mNewsList;
    Context mContext;


    public OnlineRecyclerViewAdapter(Context context, List<OnlineNews> onlineNewsList) {
        this.mNewsList = onlineNewsList;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolderOnline onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: new view requested ");
        View viewHolder = LayoutInflater.from(mContext).inflate(R.layout.cardview, viewGroup, false);
        return new ViewHolderOnline(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderOnline viewHolderOnline, int i) {
        Log.d(TAG, "onBindViewHolder: called by the layout manager when it want new data in an existing row");

        Picasso.get()
                .load(mNewsList.get(i).getImageURl())
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder)
                .into(viewHolderOnline.imageCardViewOnline);
        viewHolderOnline.titleCardViewOnline.setText(mNewsList.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");
        return ((mNewsList != null) && (mNewsList.size() != 0) ? mNewsList.size() : 0);

    }


    public static class ViewHolderOnline extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolderOnline";
        ImageView imageCardViewOnline;
        TextView titleCardViewOnline;

        public ViewHolderOnline(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolderOnline: starts");
            imageCardViewOnline = itemView.findViewById(R.id.news_img_id);
            titleCardViewOnline = itemView.findViewById(R.id.news_title_id);

        }
    }


}
