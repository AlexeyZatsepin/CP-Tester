package study.cp.datastoreanalisys;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static study.cp.datastoreanalisys.ViewHelper.getFilledTextView;
import static study.cp.datastoreanalisys.ViewHelper.getTableRow;
import static study.cp.datastoreanalisys.ViewHelper.getTableView;

public final class ContentProviderHelper {

    public final static int NUMBER_INFO = 0;
    public final static int NUMBER_SCHEMA = 1;
    public final static int NUMBER_QUERY = 2;

    private final static String TAG = ContentProviderHelper.class.getSimpleName();
    private final static String STMT = "CREATE TABLE";

    public static Map<ProviderInfo,Integer> cache = new HashMap<>();

    public static int getStatus(String str) {
        int status;
        if (str.contains("CREATE TABLE")) status = -1;
        else status = 100;
        return status;
    }

    public static boolean contains(ProviderInfo info, String charString){
        if ((info.readPermission==null)||(info.writePermission==null))
            return info.authority.toLowerCase().contains(charString);
        return info.authority.toLowerCase().contains(charString) || info.readPermission.toLowerCase().contains(charString) || info.writePermission.toLowerCase().contains(charString);
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        formatter.close();
        return sb.toString();
    }

    public static String getSQLResult(Context context, ProviderInfo provider, String query) {
        Bundle bundle = new Bundle();
        bundle.putString("provider_name", provider.packageName);
        bundle.putString("query", query);
        FirebaseAnalytics.getInstance(context).logEvent("sql_injection", bundle);
        StringBuilder s = new StringBuilder();
        try {
            Uri uri = Uri.parse("content://" + provider.authority);
            Cursor c = context.getContentResolver().query(uri, new String[]{ query }, null, null, null);
            int col_c = c != null ? c.getColumnCount() : 0;
            String[] Columns = new String[col_c];
            for (int i = 0; i < col_c; i++) {
                s.append(c.getColumnName(i));
                s.append(":");
                Columns[i] = c.getColumnName(i);
            }
            s.append("\n");
            if (c != null && c.moveToFirst()) {
                do {
                    for (int i = 0; i < col_c; i++) {
                        if (Columns[i].toLowerCase().contains("image")) {
                            byte[] blob = c.getBlob(i);
                            s.append(bytesToHexString(blob));
                        } else {
                            s.append(c.getString(i));
                        }
                        s.append(";");
                    }
                    s.append("\n");
                } while (c.moveToNext());
            }
            if (c != null) {
                c.close();
            }
        } catch (Throwable e) {
            s.append(e.getMessage());
        }
        return s.toString();
    }

    public static Map<String,List<List<String>>> parseResultToMap(String result){
        Map<String,List<List<String>>> tables = new HashMap<>();
        while (result.contains(STMT)){
            int index = result.indexOf(STMT);
            result=result.substring(index);
            int next = result.indexOf("(");
            String table_name = result.substring(STMT.length()+1,next);
            Log.v(TAG, "Table name "+table_name);
            String[] toParse = result.substring(next+1,result.indexOf(")")).split(",");
            List<List<String>> table = new ArrayList<>();
            for (String item:toParse){
                String[] keywords = item.trim().split(" ");
                Log.v(TAG, "Row keywords "+Arrays.toString(keywords));
                List<String> row = new ArrayList<>();
                if (keywords.length>1){
                    row.add(keywords[0]);
                    row.add(keywords[1]);
                    row.add(Arrays.asList(keywords)
                            .subList(1,keywords.length).toString().replace("[","").replace("]","").replace(keywords[1],"").replace(","," "));
                }
                table.add(row);
            }
            result = result.substring(result.indexOf(")"));
            tables.put(table_name,table);
        }
        return tables;
    }
}
