package study.cp.datastoreanalisys;

import android.content.pm.ProviderInfo;

import java.util.Formatter;

public final class Utils {

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
}
