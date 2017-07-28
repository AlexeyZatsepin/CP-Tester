package study.cp.datastoreanalisys.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import study.cp.datastoreanalisys.adapter.ProviderAdapter;
import study.cp.datastoreanalisys.R;
import study.cp.datastoreanalisys.ContentProviderHelper;

import static study.cp.datastoreanalisys.ContentProviderHelper.getSQLResult;
import static study.cp.datastoreanalisys.ContentProviderHelper.getStatus;

public class MainActivity extends AppCompatActivity implements ProviderAdapter.OnAdapterItemClickListener {
    private RecyclerView mRecyclerView;
    private CompositeDisposable mCompositeDisposable;
    private ProviderAdapter mAdapter;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCompositeDisposable = new CompositeDisposable();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
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

    private void testProviders() {
        mCompositeDisposable.add(getProviders()
                .observeOn(AndroidSchedulers.mainThread())//Schedulers.single()
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleTestResponse,this::handleError));
    }


    private Observable<List<ProviderInfo>> getProviders(){
        return Observable.fromCallable(() -> {
            List<ProviderInfo> info = new ArrayList<>();
            for (PackageInfo providerInfo :getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS)){
                if (providerInfo.providers != null){
                    for (ProviderInfo provider : providerInfo.providers) {
                        if (provider.authority != null) {
                            if (!sp.getBoolean("need_filter",false)){
                                info.add(provider);
                            }
                        }
                    }
                }
            }
            return info;
        });
    }

    private void handleResponse(List<ProviderInfo> result) {
        mAdapter = new ProviderAdapter(result,this);
        mRecyclerView.setAdapter(mAdapter);
        getSupportActionBar().setSubtitle("found: "+result.size());
        runLayoutAnimation();
    }

    private void handleTestResponse(List<ProviderInfo> result) {
        for (int i = 0; i < result.size(); i++) {
            ProviderInfo info = result.get(i);
            ProviderAdapter.ViewHolder holder = (ProviderAdapter.ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);
            holder.getButton().setProgress(50);
            String str = getSQLResult(getApplicationContext(), info, getString(R.string.sql_injection));
            int status = getStatus(str);
            ContentProviderHelper.cache.put(info,status);
            holder.getButton().setProgress(status);
        }
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
        menu.findItem(R.id.action_settings).setIntent(new Intent(this,SettingsActivity.class));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.refresh){
            loadProviders();
            runLayoutAnimation();
        }
//        else if(id == R.id.play){
//            testProviders();
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(ProviderInfo info) {
        startActivity(DetailsActivity.newIntent(getApplicationContext(),info));
    }

    @Override
    public int onButtonClick(ProviderInfo info) {
        String result = getSQLResult(getApplicationContext(),info, getString(R.string.sql_injection));
        int status = getStatus(result);
        ContentProviderHelper.cache.put(info,status);
        return status;
    }


    private void runLayoutAnimation() {
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation_fall_down);

        mRecyclerView.setLayoutAnimation(controller);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mRecyclerView.scheduleLayoutAnimation();
    }
}
