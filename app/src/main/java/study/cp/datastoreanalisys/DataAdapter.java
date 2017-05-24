package study.cp.datastoreanalisys;


import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static study.cp.datastoreanalisys.Utils.contains;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> implements Filterable {
    private List<ProviderInfo> mFullList;
    private List<ProviderInfo> mFilteredList;

    public DataAdapter(List<ProviderInfo> androidList) {
        mFullList = androidList;
        mFilteredList = androidList;
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
        holder.mTvReadPermissions.setText(provider.readPermission);
        holder.mTvWritePermissions.setText(provider.writePermission);
        if (!provider.exported){
            holder.itemView.setBackgroundColor(Color.GREEN);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
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

        public ViewHolder(View view) {
            super(view);
            mAuthorityView = (TextView)view.findViewById(R.id.tv_name);
            mTvReadPermissions = (TextView)view.findViewById(R.id.tv_version);
            mTvWritePermissions = (TextView)view.findViewById(R.id.tv_api_level);
        }
    }
}
