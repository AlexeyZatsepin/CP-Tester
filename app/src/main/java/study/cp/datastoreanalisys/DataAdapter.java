package study.cp.datastoreanalisys;


import android.content.pm.ProviderInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dd.CircularProgressButton;

import java.util.ArrayList;
import java.util.List;

import static study.cp.datastoreanalisys.Utils.contains;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> implements Filterable {
    private List<ProviderInfo> mFullList;
    private List<ProviderInfo> mFilteredList;
    private OnAdapterItemClickListener mListener;

    interface OnAdapterItemClickListener{
        void onItemClick(ProviderInfo info);
        int onButtonClick(ProviderInfo info);
    }


    public DataAdapter(List<ProviderInfo> androidList,OnAdapterItemClickListener listener) {
        mFullList = androidList;
        mFilteredList = androidList;
        mListener = listener;
    }
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        ProviderInfo provider = mFilteredList.get(position);
        holder.mAuthorityView.setText(provider.authority);
        if (provider.readPermission!=null) holder.mTvReadPermissions.setText(provider.readPermission); else holder.mTvReadPermissions.setHeight(0);
        if (provider.writePermission!=null)holder.mTvWritePermissions.setText(provider.writePermission); else holder.mTvWritePermissions.setHeight(0);
        holder.itemView.setOnClickListener(view -> mListener.onItemClick(provider));
        holder.button.setIndeterminateProgressMode(true);
        holder.button.setOnClickListener(view -> {
                holder.button.setProgress(50);
                holder.button.setProgress(mListener.onButtonClick(provider));
        });
    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mFullList;
                } else {
                    List<ProviderInfo> filteredList = new ArrayList<>();
                    for (ProviderInfo info : mFullList) {
                        if (contains(info,charString)) {
                            filteredList.add(info);
                        }
                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (List<ProviderInfo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mAuthorityView, mTvReadPermissions, mTvWritePermissions;
        CircularProgressButton button;

        public ViewHolder(View view) {
            super(view);
            mAuthorityView = (TextView)view.findViewById(R.id.tv_name);
            mTvReadPermissions = (TextView)view.findViewById(R.id.tv_version);
            mTvWritePermissions = (TextView)view.findViewById(R.id.tv_api_level);
            button = (CircularProgressButton) view.findViewById(R.id.progress);
        }
    }
}
