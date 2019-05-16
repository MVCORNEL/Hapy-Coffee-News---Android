package vcmanea.example.happycoffeenews;

import android.provider.BaseColumns;

public class StoreNewsContract {
    //****************MAKE SURE NO-ONE CAN CREATE A INSTANCE OF THIS CLASS>>> CAUSE WE DON'T NEED IT*****************//
    private StoreNewsContract() {

    }

    public static final class StoreNewsEntry implements BaseColumns {
        static final String TABLE_NAME = "newsList";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_DESCRIPTION = "description";
        static final String COLUMN_CONTENT = "content";
        static final String COLUMN_IMAGE = "imageURL";
    }


}


