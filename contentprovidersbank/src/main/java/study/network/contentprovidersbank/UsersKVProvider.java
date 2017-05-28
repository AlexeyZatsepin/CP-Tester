package study.network.contentprovidersbank;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

public class UsersKVProvider extends ContentProvider{
    DB snappydb;

    @Override
    public boolean onCreate() {
        try {
            snappydb = DBFactory.open(getContext());
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        try {
            final String[] keys = snappydb.findKeys(s);
            return new AbstractCursor() {
                @Override public int getCount() {
                    return keys.length;
                }
                @Override public String[] getColumnNames() {
                    return keys;
                }
                @Override public String getString(int i) {
                    try {
                        return snappydb.getObject(keys[i],User.class).toString();
                    } catch (SnappydbException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                @Override public short getShort(int i) {return 0;}
                @Override public int getInt(int i) {return 0;}
                @Override public long getLong(int i) {return 0;}
                @Override public float getFloat(int i) {return 0;}
                @Override public double getDouble(int i) {return 0;}
                @Override public boolean isNull(int i) {return false;}
            };
        } catch (SnappydbException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        User user = new User();
        for (String key: contentValues.keySet()){
            if (key.equals("name")) user.setName((String) contentValues.get(key));
            if (key.equals("password")) user.setPassword((String) contentValues.get(key));
        }
        try {
            snappydb.put(user.getName(),user);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return Uri.parse("content://"+uri.getAuthority()+user.getName());
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        try {
            snappydb.del(s);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
