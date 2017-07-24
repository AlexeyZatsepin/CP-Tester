package study.cp.datastoreanalisys.fragments;

import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import study.cp.datastoreanalisys.R;

import static study.cp.datastoreanalisys.ContentProviderHelper.getSQLResult;
import static study.cp.datastoreanalisys.ContentProviderHelper.getStatus;
import static study.cp.datastoreanalisys.ContentProviderHelper.parseResult;


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
            TextView textView = (TextView) rootView.findViewById(R.id.tv_schema);
            textView.setVisibility(View.VISIBLE);
        }else {
            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.ll);
            linearLayout.addView(parseResult(getContext(),result));
        }
        return rootView;
    }
}
