package vcmanea.example.happycoffeenews;

import android.provider.BaseColumns;

public class StoreNewsContract {
    //****************MAKE SURE NO-ONE CAN CREATE A INSTANCE OF THIS CLASS>>> CAUSE WE DON:T NEED IT*****************//
    private StoreNewsContract() {

    }

    public static final class StoreNewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "newsList";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_IMAGE = "imageURL";
    }

}


