package ahmed.example.com.bakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import ahmed.example.com.bakingapp.R;

/**
 * Created by root on 22/09/17.
 */

public class WidgetServiceIntent extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {


        return new WidgetRemoteFactory(getApplicationContext(), intent);
    }

    class WidgetRemoteFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private Cursor mCursor;

        public WidgetRemoteFactory(Context mContext, Intent intent) {

            this.mContext = mContext;
        }

        @Override
        public void onCreate() {
            getData();
            Log.e("Cursor", mCursor.getCount() + "");
        }

        @Override
        public void onDataSetChanged() {
            getData();
            Log.e("Cursor", mCursor.getCount() + "");

        }

        void getData() {
            mCursor = mContext.getContentResolver().query(IngredientsProvider.BASE_URI,
                                                          null,
                                                          null,
                                                          null,
                                                          null
            );
        }

        @Override
        public void onDestroy() {

            mCursor.close();

        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            if (mCursor.getCount() == 0) return null;
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                                                      R.layout.widget_list_item
            );
            mCursor.moveToPosition(i);
            String measure = mCursor.getString(mCursor.getColumnIndex(IngredientsProvider.INGREDIENTS_MEASURE));
            String ingredient = mCursor.getString(mCursor.getColumnIndex(IngredientsProvider.INGREDIENT));
            String quantity = mCursor.getString(mCursor.getColumnIndex(IngredientsProvider.INGREDIENTS_QUANTITY));

            remoteViews.setTextViewText(R.id.ingredient,
                                        "-" + ingredient + "(" + quantity + measure + ")"
            );


            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}