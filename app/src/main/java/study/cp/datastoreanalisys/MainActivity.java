package study.cp.datastoreanalisys;

import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements DataAdapter.OnAdapterItemClickListener {
    private RecyclerView mRecyclerView;
    private CompositeDisposable mCompositeDisposable;
    private DataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCompositeDisposable = new CompositeDisposable();
        initRecyclerView();
        loadProviders();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void loadProviders() {
        mCompositeDisposable.add(getProviders()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private Observable<List<ProviderInfo>> getProviders(){
        return Observable.fromCallable(() -> {
            List<ProviderInfo> info = new ArrayList<>();
            getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS).stream().filter(pack -> pack.providers != null).forEach(pack -> {
                for (ProviderInfo provider : pack.providers) {
                    if (provider.authority != null) {
                        info.add(provider);
                    }
                }
            });
            return info;
        });
    }
    private void handleResponse(List<ProviderInfo> result) {
        mAdapter = new DataAdapter(result,this);
        mRecyclerView.setAdapter(mAdapter);
        getSupportActionBar().setSubtitle("found: "+result.size());
    }

    private void handleError(Throwable error) {
        Toast.makeText(this, "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {
                getSupportActionBar().setSubtitle("found: "+mAdapter.getItemCount());
                return true;
            }
            @Override public boolean onQueryTextChange(String newText) {
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.refresh){
            loadProviders();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(ProviderInfo info) {
        startActivity(DetailsActivity.newIntent(getApplicationContext(),info));
    }
}
