package study.cp.datastoreanalisys.fragments;

import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import study.cp.datastoreanalisys.R;

public class InfoFragment extends Fragment {
    private ProviderInfo provider;

    private static final String ARG_PROVIDER = "provider";

    public InfoFragment() {
    }

    public static InfoFragment newInstance(ProviderInfo provider) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROVIDER, provider);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        provider = getArguments().getParcelable(ARG_PROVIDER);
        View rootView = inflater.inflate(R.layout.fragment_details_info, container, false);
        TextView label = (TextView) rootView.findViewById(R.id.section_label);
        label.setText(getString(R.string.section_authority, provider.authority));
        TextView perms = (TextView) rootView.findViewById(R.id.section_permissions);
        perms.setText(getString(R.string.section_permissions, provider.readPermission, provider.writePermission));
        TextView other = (TextView) rootView.findViewById(R.id.section_other);
        other.setText(getString(R.string.section_other, provider.packageName, provider.processName, provider.exported, provider.enabled));
        TextView help = (TextView) rootView.findViewById(R.id.help);
        return rootView;
    }
}
