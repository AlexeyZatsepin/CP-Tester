package study.cp.datastoreanalisys;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import static study.cp.datastoreanalisys.ViewHelper.getFilledTextView;
import static study.cp.datastoreanalisys.ViewHelper.getTableRow;
import static study.cp.datastoreanalisys.ViewHelper.getTableView;

public final class ContentProviderHelper {

    public final static int NUMBER_INFO = 0;
    public final static int NUMBER_SCHEMA = 1;
    public final static int NUMBER_QUERY = 2;

    private final static String TAG = ContentProviderHelper.class.getSimpleName();

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

    public static String getSQLResult(Context context, ProviderInfo provider, String...query) {
        String s = "";
        try {
            Uri uri = Uri.parse("content://" + provider.authority);
            Cursor c = context.getContentResolver().query(uri, query, null, null, null);
            int col_c = c != null ? c.getColumnCount() : 0;
            String[] Columns = new String[col_c];
            for (int i = 0; i < col_c; i++) {
                s += c.getColumnName(i);
                s += ":";
                Columns[i] = c.getColumnName(i);
            }
            s += "\n";
            if (c != null && c.moveToFirst()) {
                do {
                    for (int i = 0; i < col_c; i++) {
                        if (Columns[i].toLowerCase().contains("image")) {
                            byte[] blob = c.getBlob(i);
                            s += bytesToHexString(blob);
                        } else {
                            s += c.getString(i);
                        }
                        s += ";";
                    }
                    s += "\n";
                } while (c.moveToNext());
            }
            if (c != null) {
                c.close();
            }
        } catch (Throwable e) {
            s += e.getMessage();
        }
        return s;
    }

    public static View parseResult(Context context, String result){
        String stmt = "CREATE TABLE";
        TableLayout table = getTableView(context);
        while (result.contains(stmt)){
            int index = result.indexOf(stmt);
            result=result.substring(index);
            int next = result.indexOf("(");
            String table_name = result.substring(stmt.length()+1,next);
            Log.v(TAG, "Table name "+table_name);
            TextView t_name = getFilledTextView(context,table_name);
            TableRow titleRow = getTableRow(context, t_name);
            table.addView(titleRow);
            String[] toParse = result.substring(next+1,result.indexOf(")")).split(",");
            for (String item:toParse){
                String[] keywords = item.trim().split(" ");
                Log.v(TAG, "Row keywords "+Arrays.toString(keywords));
                TextView row_name = getFilledTextView(context,keywords[0]);
                TableRow tableRow;
                if (keywords.length>1){
                    TextView row_type = getFilledTextView(context,keywords[1]);
                    TextView row_additional = getFilledTextView(context,Arrays.asList(keywords)
                            .subList(1,keywords.length).toString().replace("[","").replace("]","").replace(keywords[1],"-"));
                    tableRow = getTableRow(context, row_name, row_type, row_additional);
                }else{
                    tableRow = getTableRow(context, row_name);
                }
                table.addView(tableRow);
            }
            result = result.substring(result.indexOf(")"));
        }
        return table;
    }
}
