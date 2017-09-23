package study.cp.datastoreanalisys.fragments;

import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import study.cp.datastoreanalisys.R;
import study.cp.datastoreanalisys.adapter.GridAdapter;

import static study.cp.datastoreanalisys.ContentProviderHelper.getSQLResult;
import static study.cp.datastoreanalisys.ContentProviderHelper.getStatus;
import static study.cp.datastoreanalisys.ContentProviderHelper.parseResultToMap;
import static study.cp.datastoreanalisys.ViewHelper.getFilledTextView;

public class SchemaFragment extends Fragment {

    private ProviderInfo provider;

    private static final String ARG_PROVIDER = "provider";

    public SchemaFragment() {
    }

    public static SchemaFragment newInstance(ProviderInfo provider) {
        SchemaFragment fragment = new SchemaFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROVIDER, provider);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        provider = getArguments().getParcelable(ARG_PROVIDER);
        String result = getSQLResult(getContext(), provider, getString(R.string.sql_injection));
        View rootView = inflater.inflate(R.layout.fragment_details_schema, container, false);
        if (getStatus(result)==100){
            //TODO add image view
            rootView.findViewById(R.id.tv_schema).setVisibility(View.VISIBLE);
        } else {
            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.ll);
            Map<String, List<List<String>>> resultToMap = parseResultToMap(result);
            for (String name: resultToMap.keySet()){
                TextView tv = getFilledTextView(getContext(),name);
                linearLayout.addView(tv);
                GridView grid = new GridView(getContext());
                grid.setNumColumns(3);
                grid.setHorizontalSpacing(10);
                grid.setVerticalSpacing(10);
                grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                grid.setGravity(View.TEXT_ALIGNMENT_CENTER);
                List<List<String>> list = resultToMap.get(name);
                grid.setAdapter(new GridAdapter(getContext(),list));
                grid.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, list.size()*30)); //TODO: remove int value
                linearLayout.addView(grid);
            }
        }
        return rootView;
    }
}
