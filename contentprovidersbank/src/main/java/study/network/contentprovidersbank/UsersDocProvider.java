package study.network.contentprovidersbank;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseOptions;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class UsersDocProvider extends ContentProvider{

    Database database;

    @Override
    public boolean onCreate() {
        try {
            Manager manager = new Manager(new AndroidContext(getContext()), Manager.DEFAULT_OPTIONS);
            DatabaseOptions options = new DatabaseOptions();
            options.setCreate(true);
            database = manager.openDatabase("users",options);
        } catch (IOException | CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String [] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Query query;
        if ((projection==null)||(selection==null)){
            query = database.createAllDocumentsQuery();
        }else{
            query = database.getView(projection[0]).createQuery();
        }
        if (selection!=null){
        }
        final List<Document> docs = new ArrayList<>();
        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext();) {
                QueryRow row = it.next();
                docs.add(row.getDocument());
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return new AbstractCursor() {
            @Override public int getCount() {
                return docs.size();
            }

            @Override public String[] getColumnNames() {
                String[] str = new String[docs.get(0).getProperties().keySet().size()];
                int cur = 0;
                for (Object obj: docs.get(0).getProperties().keySet()){
                    str[cur++] = (String) obj;
                }
                return str;
            }

            @Override public String getString(int column) {
                return docs.get(column).getProperties().toString();
            }

            @Override public short getShort(int column) {
                return 0;
            }
            @Override public int getInt(int column) {
                return 0;
            }
            @Override public long getLong(int column) {
                return 0;
            }
            @Override public float getFloat(int column) {
                return 0;
            }
            @Override public double getDouble(int column) {
                return 0;
            }
            @Override public boolean isNull(int column) {
                return false;
            }
        };
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "NoSQL";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Map<String, Object> properties = new HashMap<>();
        for (String key: values.keySet()){
            properties.put(key,values.get(key));
        }
        Document document = database.createDocument();
        try {
            document.putProperties(properties);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Document task = database.getDocument(selection);
        try {
            task.delete();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }


}
