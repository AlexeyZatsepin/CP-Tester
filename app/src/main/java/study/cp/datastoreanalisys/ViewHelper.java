package study.cp.datastoreanalisys;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public final class ViewHelper {

    public static TableLayout getTableView(Context context){
        TableLayout table = new TableLayout(context);
        table.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT));
        return table;
    }

    public static TextView getFilledTextView(Context context,String text){
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setTextColor(Color.parseColor("#dd2c00"));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(20);
        tv.setPadding(0,0,0,10);
        return tv;
    }

    public static TableRow getTableRow(Context context, View... views){
        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tableRow.setBackgroundColor(Color.GRAY);
        for (View item: views) {
            tableRow.addView(item);
        }
        return tableRow;
    }
}
