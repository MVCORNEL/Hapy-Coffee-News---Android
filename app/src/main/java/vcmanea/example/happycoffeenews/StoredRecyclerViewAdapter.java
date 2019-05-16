package vcmanea.example.happycoffeenews;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import vcmanea.example.happycoffeenews.StoreNewsContract.StoreNewsEntry;

public class StoredRecyclerViewAdapter extends RecyclerView.Adapter<StoredRecyclerViewAdapter.StoredRecyclerViewHolder> {
    private static final String TAG = "StoredRecyclerViewAdapt";
    private Context mContext;
    private Cursor mCursor;
    private Bitmap bitmap;
    private StoreNewsListener mStoreNewsListener;
    private static List<Integer> positionOnId;

    interface StoreNewsListener {
        void onClickStorage(int i);

        void onLongClickStorage(int i);
    }

    StoredRecyclerViewAdapter(Context context, Cursor cursor, StoreNewsListener listener) {
        mContext = context;
        mCursor = cursor;
        mStoreNewsListener = listener;
        positionOnId = new ArrayList<>();
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
    public void onBindViewHolder(@NonNull final StoredRecyclerViewHolder storedRecyclerViewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called by the layout manager when it want new data in an existing row");
        mCursor.moveToPosition(i);
        int indexId = mCursor.getColumnIndex(StoreNewsEntry._ID);

        int indexTitle = mCursor.getColumnIndex(StoreNewsEntry.COLUMN_TITLE);
        int indexDescription = mCursor.getColumnIndex(StoreNewsEntry.COLUMN_DESCRIPTION);
        int indexImg = mCursor.getColumnIndex(StoreNewsEntry.COLUMN_IMAGE);

        positionOnId.add(storedRecyclerViewHolder.getAdapterPosition(), indexId);


        storedRecyclerViewHolder.titleCardViewStorage.setText(mCursor.getString(indexTitle));
        storedRecyclerViewHolder.titleCardViewStorage.setTag(mCursor.getInt(indexId));
        storedRecyclerViewHolder.descirptionCardViewStorage.setText(mCursor.getString(indexDescription));


        byte[] image = (mCursor.getBlob(indexImg));
        if (image != null) {
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        }
        storedRecyclerViewHolder.imageCardViewStorage.setImageBitmap(bitmap);

        storedRecyclerViewHolder.imageCardViewStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStoreNewsListener != null)
                    mStoreNewsListener.onClickStorage((int) storedRecyclerViewHolder.titleCardViewStorage.getTag());
            }
        });

        storedRecyclerViewHolder.deleteImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mStoreNewsListener != null) {
                    Snackbar.make(v, "You are sure you want delete this?", Snackbar.LENGTH_LONG).setAction("Yes", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mStoreNewsListener.onLongClickStorage((int) storedRecyclerViewHolder.titleCardViewStorage.getTag());

                        }
                    }).show();

                }
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0 : mCursor.getCount();
    }

    static class StoredRecyclerViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "StoredRecyclerViewHolde";
        CardView cardView_storage;
        ImageView imageCardViewStorage;
        TextView titleCardViewStorage;
        TextView descirptionCardViewStorage;
        ImageView deleteImage;


        StoredRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "StoredRecyclerViewHolder: starts");
            cardView_storage = itemView.findViewById(R.id.cardView);
            imageCardViewStorage = itemView.findViewById(R.id.imageView_storage);
            titleCardViewStorage = itemView.findViewById(R.id.storage_title_id);
            descirptionCardViewStorage = itemView.findViewById(R.id.storage_description_id);
            deleteImage = itemView.findViewById(R.id.delete_store);


        }
    }


}

