package vcmanea.example.happycoffeenews;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import vcmanea.example.happycoffeenews.StoreNewsContract.StoreNewsEntry;

public class StoredRecyclerViewAdapter extends RecyclerView.Adapter<StoredRecyclerViewAdapter.StoredRecyclerViewHolder> {
    private static final String TAG = "StoredRecyclerViewAdapt";
    private Context mContext;
    private Cursor mCursor;
    private Bitmap bitmap;

     StoredRecyclerViewAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    @NonNull
    @Override
    public StoredRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: new view requested ");
        View viewHolder;
        viewHolder = LayoutInflater.from(mContext).inflate(R.layout.cardview_storage, viewGroup, false);
        return new StoredRecyclerViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull StoredRecyclerViewHolder storedRecyclerViewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called by the layout manager when it want new data in an existing row");
        mCursor.moveToPosition(i);

        int indexTitle = mCursor.getColumnIndex(StoreNewsEntry.COLUMN_TITLE);
        int indexDescription = mCursor.getColumnIndex(StoreNewsEntry.COLUMN_DESCRIPTION);
        int indexImg = mCursor.getColumnIndex(StoreNewsEntry.COLUMN_IMAGE);

        storedRecyclerViewHolder.titleCardViewStorage.setText(mCursor.getString(indexTitle));
        storedRecyclerViewHolder.descirptionCardViewStorage.setText(mCursor.getString(indexDescription));


        byte[] image = (mCursor.getBlob(indexImg));
        if (image != null) {
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        }
        storedRecyclerViewHolder.imageCardViewStorage.setImageBitmap(bitmap);


    }

    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0 : mCursor.getCount();
    }

    public static class StoredRecyclerViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "StoredRecyclerViewHolde";
        CardView cardView_storage;
        ImageView imageCardViewStorage;
        TextView titleCardViewStorage;
        TextView descirptionCardViewStorage;


         StoredRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
             Log.d(TAG, "StoredRecyclerViewHolder: starts");
            cardView_storage = itemView.findViewById(R.id.cardView);
            imageCardViewStorage = itemView.findViewById(R.id.imageView_storage);
            titleCardViewStorage = itemView.findViewById(R.id.storage_title_id);
            descirptionCardViewStorage = itemView.findViewById(R.id.storage_description_id);


        }
    }



}

