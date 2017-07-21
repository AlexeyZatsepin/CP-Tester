package study.cp.datastoreanalisys.fragments;

import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;


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
}
