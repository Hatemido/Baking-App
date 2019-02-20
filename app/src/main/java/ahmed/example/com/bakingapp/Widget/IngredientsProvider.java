package ahmed.example.com.bakingapp.Widget;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by root on 22/09/17.
 */

public class IngredientsProvider extends ContentProvider {

    public static final String AUTHORITY = "ahmed.example.com.bakingapp";
    public static final String INGREDIENTS_TABLE = "Ingredients_Table";
    public static final int INGREDIENTS = 100;
    private static final String BASE_URL = "content://" + AUTHORITY + "/" + INGREDIENTS_TABLE;
    public static final String INGREDIENTS_ID = "Id";
    public static final String INGREDIENTS_QUANTITY = "Quantity";
    public static final String INGREDIENTS_MEASURE = "Measure";
    public static final String INGREDIENT = "Ingredient";

    public static final String CREATE_TABLE = "CREATE TABLE " + INGREDIENTS_TABLE +
            "(" +
            INGREDIENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            INGREDIENTS_QUANTITY + " TEXT ," +
            INGREDIENTS_MEASURE + " TEXt ," +
            INGREDIENT + " TEXt )";
    public static  final Uri BASE_URI = Uri.parse(BASE_URL);

    UriMatcher sUriMatcher = buildMatcher();
    MyDataBase myDataBase;

    static UriMatcher buildMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, INGREDIENTS_TABLE, INGREDIENTS);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        myDataBase = new MyDataBase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] strings,
                        @Nullable String s,
                        @Nullable String[] strings1,
                        @Nullable String s1
    ) {
        SQLiteDatabase sqLiteDatabase = myDataBase.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor cursor = null;
        switch (match) {
            case INGREDIENTS:
                cursor = sqLiteDatabase.query(INGREDIENTS_TABLE,
                                              strings,
                                              s,
                                              strings1,
                                              null,
                                              null,
                                              s1
                );
                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues
    ) {
        SQLiteDatabase sqLiteDatabase=myDataBase.getWritableDatabase();

        int match=sUriMatcher.match(uri);
        Uri returnUri=null;

        switch (match){
            case INGREDIENTS:
                long id =sqLiteDatabase.insert(INGREDIENTS_TABLE,null,contentValues);
                if(id>0){
                    returnUri= ContentUris.withAppendedId(BASE_URI,id);
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                break;
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings
    ) {
        SQLiteDatabase sqLiteDatabase=myDataBase.getWritableDatabase();

        int match=sUriMatcher.match(uri);
        int  deleted=0;

        switch (match) {
            case INGREDIENTS:
                deleted=sqLiteDatabase.delete(INGREDIENTS_TABLE,s,strings);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues contentValues,
                      @Nullable String s,
                      @Nullable String[] strings
    ) {
        return 0;
    }

    class MyDataBase extends SQLiteOpenHelper {

        MyDataBase(Context context) {
            super(context, INGREDIENTS_TABLE, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + INGREDIENTS_TABLE);
            onCreate(sqLiteDatabase);
        }
    }
}
