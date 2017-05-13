package study.network.datastoreanalisys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.cp_list)
    Spinner cs;
    @ViewById(R.id.run)
    Button btn;
    @ViewById(R.id.authority)
    EditText auth;
    @ViewById(R.id.projection)
    EditText proj;
    @ViewById(R.id.selection)
    EditText sel;
    @ViewById(R.id.selection_arg)
    EditText sel_arg;
    @ViewById(R.id.filename_arg)
    EditText file_arg;

    private List<ItemInfo> cps;
    private ProviderInfo[] providers;

    @AfterViews
    void init(){
        getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS).size();
        cps = new ArrayList<>();
        for (PackageInfo pack : getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS)) {
            providers = pack.providers;
            if (providers != null) {
                for (ProviderInfo provider : providers) {
                    if (provider.authority != null)
                    {
                        ItemInfo mcpi = new ItemInfo("content://" + provider.authority.split(";")[0]);
                        mcpi.SetPermission(provider.readPermission,provider.writePermission);
                        cps.add(mcpi);
                    }
                }
            }
        }
        cs.setAdapter(new MyAdapter(MainActivity.this, R.layout.spinner, cps));
        cs.setSelection(0);
        cs.setPrompt("Choose content provider");
        cs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                changeAuthority();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Click(R.id.run)
    public void showResult() {
        Intent my_intent = new Intent(this, ResultActivity.class);
        ItemInfo mcpi = cps.get(cs.getSelectedItemPosition());
        mcpi.setAuthority(auth.getText().toString());
        mcpi.setProjection(proj.getText().toString());
        mcpi.setSelection(sel.getText().toString());
        mcpi.setSelectionArgs(sel_arg.getText().toString());
        mcpi.setFilenameArgs(file_arg.getText().toString());
        my_intent.putExtra("cpi",mcpi);
        startActivity(my_intent);
    }

    public void changeAuthority() {
        auth.setText(cps.get(cs.getSelectedItemPosition()).getAuthority());
    }

    public class MyAdapter extends ArrayAdapter<String> {

        ItemInfo[] cpinfo;

        public MyAdapter(Context context, int textViewResourceId, List<ItemInfo> cpi) {
            super(context, textViewResourceId, new String[cpi.size()]);
            cpinfo = new ItemInfo[cpi.size()];
            int i = 0;
            for (ItemInfo cp : cpi)
            {
                cpinfo[i] = cp;
                i++;
            }
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.spinner, parent, false);
            TextView label = (TextView)row.findViewById(R.id.content_provider_authority);
            label.setText(cpinfo[position].getAuthority());
            TextView read_perm = (TextView)row.findViewById(R.id.read_permission);
            String read_permission_string = cpinfo[position].getPermissions()[ItemInfo.PERMISSIONS_READ];
            read_perm.setTextColor(Color.GREEN);
            read_perm.setText("Read permissions:" + read_permission_string);
            if (read_permission_string == null)
            {
                read_perm.setTextColor(Color.RED);
                read_perm.setText("Read permissions: -");
            }
            TextView write_perm = (TextView)row.findViewById(R.id.write_permission);
            String write_permission_string = cpinfo[position].getPermissions()[ItemInfo.PERMISSIONS_WRITE];
            write_perm.setTextColor(Color.GREEN);
            write_perm.setText("Write permissions:" + write_permission_string);
            if (write_permission_string == null)
            {
                write_perm.setTextColor(Color.RED);
                write_perm.setText("Write permissions: -");
            }
            return row;
        }
    }

}
