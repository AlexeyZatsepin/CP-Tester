package study.cp.datastoreanalisys;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
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
