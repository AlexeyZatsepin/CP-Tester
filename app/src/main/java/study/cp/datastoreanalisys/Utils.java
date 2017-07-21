package study.cp.datastoreanalisys;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;

import java.util.Formatter;

public final class Utils {

    public final static int NUMBER_INFO = 0;
    public final static int NUMBER_SCHEMA = 1;
    public final static int NUMBER_QUERY = 2;

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
        return sb.toString();
    }

    public static String getSQLResult(Context context, ProviderInfo provider, String...query) {
        String s = "";
        try {
            Uri uri = Uri.parse("content://" + provider.authority);
            Cursor c = context.getContentResolver().query(uri, query, null, null, null); //TODO result
            int col_c = c.getColumnCount();
            String[] Columns = new String[col_c];
            for (int i = 0; i < col_c; i++) {
                s += c.getColumnName(i);
                s += ":";
                Columns[i] = c.getColumnName(i);
            }
            s += "\n";
            if (c.moveToFirst()) {
                do {
                    for (int i = 0; i < col_c; i++) {
                        if (Columns[i].toLowerCase().contains("image")) {
                            byte[] blob = c.getBlob(i);
                            //iv.setImageBitmap(BitmapFactory.decodeByteArray(Image,0,Image.length));
                            //s += "<IMAGE?>";
                            s += bytesToHexString(blob);
                        } else {
                            try {
                                s += c.getString(i);
                            } catch (Exception e) {
                                byte[] blob = c.getBlob(i);
                                s += bytesToHexString(blob);
                            }
                        }
                        s += ";";
                    }
                    s += "\n";
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            s += e.getMessage();
        }
        return s;
    }

    public static int getStatus(String str) {
        int status;
        if (str.contains("CREATE TABLE")) status = -1;
        else status = 100;
        return status;
    }
}
