package study.cp.datastoreanalisys;

import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;

import static study.cp.datastoreanalisys.Utils.bytesToHexString;

public class PlaceholderFragment extends Fragment implements View.OnClickListener {

    private EditText et;
    private ProviderInfo provider;

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_PROVIDER = "provider";

    private int section;

    public PlaceholderFragment() {
    }

    public static PlaceholderFragment newInstance(int sectionNumber, ProviderInfo provider) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putParcelable(ARG_PROVIDER, provider);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        provider = getArguments().getParcelable(ARG_PROVIDER);
        section = getArguments().getInt(ARG_SECTION_NUMBER);
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        TextView label = (TextView) rootView.findViewById(R.id.section_label);
        label.setText(getString(R.string.section_authority, provider.authority));
        TextView perms = (TextView) rootView.findViewById(R.id.section_permissions);
        perms.setText(getString(R.string.section_permissions, provider.readPermission, provider.writePermission));
        TextView other = (TextView) rootView.findViewById(R.id.section_other);
        other.setText(getString(R.string.section_other, provider.packageName, provider.processName, provider.exported, provider.enabled));
        TextView help = (TextView) rootView.findViewById(R.id.help);
        help.setText(getResources().getStringArray(R.array.help_info)[section]);
        Button button = (Button) rootView.findViewById(R.id.injection_button);
        button.setOnClickListener(this);
        provider = getArguments().getParcelable(ARG_PROVIDER);
        et = (EditText) rootView.findViewById(R.id.injection_et);
        if (section == 0) {
            et.setText(R.string.sql_injection);
        }else if(section == 1){

        }else{

        }
        return rootView;
    }

    @Override
    public void onClick(View view) {
        String result;
        if (isFile()){
            result = "This content provider are build on file";
        }else if (section==0){
            result = getSQLResult(String.valueOf(et.getText()));
        }else if (section==1){
            result = getJSONResult(String.valueOf(et.getText()));
        }else{
            result = getKVResult(String.valueOf(et.getText()));
        }
        Drawable icon = getResources().getDrawable(R.drawable.ic_warning, null);
        if (!(result.contains("Denial") || result.contains("Exception"))) {
            icon = getResources().getDrawable(R.drawable.ic_succes, null);
        }
        new AlertDialog.Builder(getContext())
                .setTitle("Result")
                .setIcon(icon)
                .setMessage(result)
                .setPositiveButton("Ok", (dialog, which) -> {
                }).create().show();

    }

    public boolean isFile(){
        try {
            return getActivity().getContentResolver().openInputStream(Uri.parse("content://" + provider.authority))!=null;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public String getJSONResult(String...query){
        Uri uri = Uri.parse("content://" + provider.authority);
        Cursor c = getActivity().getContentResolver().query(uri, query, null, null, null);
        if (c.getCount()==0){
            return "Documents not found";
        }else{
            StringBuilder res = new StringBuilder();
            String[] all = c.getColumnNames();
            for (int i =0; i< c.getCount();i++){
                res.append(all[i]).append(" ").append(c.getString(i)).append("\n");
            }
            return res.toString();
        }
    }

    public String getKVResult(String...query){
        Uri uri = Uri.parse("content://" + provider.authority);
        Cursor c = getActivity().getContentResolver().query(uri, query, null, null, null);
        if (c.getColumnCount()==0){
            return "Keys not found";
        }else{
            StringBuilder res = new StringBuilder();
            for (int i =0; i< c.getColumnCount();i++){
                res.append(c.getString(i)+"\n");
            }
            return res.toString();
        }
    }

    public String getSQLResult(String...query) {
        String s = "";
        try {
            Uri uri = Uri.parse("content://" + provider.authority);
            Cursor c = getActivity().getContentResolver().query(uri, query, null, null, null); //TODO result
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
}