package coding.lien.charles.locationreportsender.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmConfiguration;

/**
 * Created by lienching on 9/20/16.
 */



public class BookMarkAdapter extends RealmBaseAdapter<InformationHolder> implements ListAdapter, Filterable {

    private Context my_context;

    public BookMarkAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<InformationHolder> data) {
        super(context, data);
        my_context = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if ( view == null ) {
            view = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            holder = new ViewHolder();
            holder.ip_address = (TextView) view.findViewById( android.R.id.text1 );
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }


        InformationHolder info = adapterData.get(i);
        holder.ip_address.setText(info.getServerip());
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                HashSet<String> set = new HashSet<>();
                ArrayList<InformationHolder> result = new ArrayList<>();

                for(InformationHolder info : adapterData) {
                    if ( set.add(info.getServerip()) ) {
                        result.add(info);
                    }
                }
                results.values = result;
                results.count = result.size();
                Log.d("Debug", "Result Size:"+result.size());
                Log.d("Debug", "Set Size:" + set.size());
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notifyDataSetChanged();
            }
        };
    }

    private static class ViewHolder {

        TextView ip_address;
    }
}
