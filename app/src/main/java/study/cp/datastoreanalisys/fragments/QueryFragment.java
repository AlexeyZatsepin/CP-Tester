package study.cp.datastoreanalisys.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import java.io.FileNotFoundException;

import study.cp.datastoreanalisys.R;

import static study.cp.datastoreanalisys.ContentProviderHelper.getSQLResult;
import static study.cp.datastoreanalisys.ContentProviderHelper.getStatus;

public class QueryFragment extends Fragment implements View.OnClickListener {

    private EditText et;
    private CircularProgressButton button;
    private ProviderInfo provider;

    private static final String ARG_PROVIDER = "provider";

    public QueryFragment() {
    }

    public static QueryFragment newInstance(ProviderInfo provider) {
        QueryFragment fragment = new QueryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROVIDER, provider);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        provider = getArguments().getParcelable(ARG_PROVIDER);
        View rootView = inflater.inflate(R.layout.fragment_details_query, container, false);
        TextView help = (TextView) rootView.findViewById(R.id.help);
        help.setText(getResources().getString(R.string.help_info));
        button = (CircularProgressButton) rootView.findViewById(R.id.injection_button);
        button.setOnClickListener(this);
        et = (EditText) rootView.findViewById(R.id.injection_et);
        et.setText(R.string.sql_injection);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        try{
            String result;
            button.setProgress(50);
            if (provider.exported){
                if (isFile()){
                    result = "This content provider are build on file";
                }else {
                    result = getSQLResult(getContext(), provider ,String.valueOf(et.getText()));
                }
            }else {
                button.setProgress(-1);
                return;
            }
            Drawable icon = null;
            int status = getStatus(result);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                icon = getResources().getDrawable(R.drawable.ic_warning, null);
                if (status==-1) {
                    icon = getResources().getDrawable(R.drawable.ic_succes, null);
                }
            }
            button.setProgress(status);
            new AlertDialog.Builder(getContext())
                    .setTitle("Result")
                    .setIcon(icon)
                    .setMessage(result)
                    .setNegativeButton("Copy", (dialogInterface, i) -> {
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Result", result);
                        clipboard.setPrimaryClip(clip);
                    })
                    .setPositiveButton("Ok", (dialog, which) -> {})
                    .create().show();
        }catch (Throwable e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }finally {
            if(button.getProgress()==50){
                button.setProgress(-1);
            }
        }

    }

    public boolean isFile(){
        try {
            return getActivity().getContentResolver().openInputStream(Uri.parse("content://" + provider.authority))!=null;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

}