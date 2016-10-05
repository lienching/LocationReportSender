package coding.lien.charles.locationreportsender.util;

import android.content.Context;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


import io.realm.RealmResults;

/**
 * Created by lienching on 9/20/16.
 */



public class BookMarkAdapter extends BaseAdapter implements Filterable {

    private String target;
    private final RealmResults<BookMarkHolder> mRealmObjectList;
    private ArrayList<BookMarkHolder> mResults;
    private LayoutInflater layoutInflater;


    public BookMarkAdapter(Context context, RealmResults<BookMarkHolder> realmObjectList, String target) {
        this.mRealmObjectList = realmObjectList;
        this.target = target;
        this.layoutInflater = LayoutInflater.from(context);
        mResults = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public Object getItem(int i) {
        return mResults.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if ( view == null ) {
            view = layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
            holder = new ViewHolder();
            holder.selections = (TextView) view.findViewById( android.R.id.text1 );
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        BookMarkHolder info = mResults.get(i);

        switch(target) {
            case "IP":
                holder.selections.setText(info.serverip);
                break;
            case "GROUP":
                holder.selections.setText(info.partyid);
                break;
            case "USER":
                holder.selections.setText(info.memberid);
                break;
            default:
                break;
        }

        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            private boolean mHasResults = true;
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                // do nothing here because it's executed in another thread and Realm really
                // doesn't like treating data from another thread.
                final FilterResults results = new FilterResults();
                results.count = mHasResults ? 1 : 0;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                // back on the main thread, we can do the query and notify
                if (constraint != null) {
                    mResults = performRealmFiltering(constraint, mRealmObjectList);
                    mHasResults = mResults.size() > 0;
                    notifyDataSetChanged();
                }
            }

            protected ArrayList<BookMarkHolder> performRealmFiltering(@NonNull CharSequence constraint, RealmResults<BookMarkHolder> results){
                HashSet<String> set = new HashSet<>();
                ArrayList<BookMarkHolder> list = new ArrayList<>();
                for( BookMarkHolder holder : mRealmObjectList ) {
                    switch(target) {
                        case "IP":
                            if ( set.add(holder.serverip) ) {
                                list.add(holder);
                            }
                            break;
                        case "GROUP":
                            if ( set.add(holder.partyid) ) {
                                list.add(holder);
                            }
                            break;
                        case "USER":
                            if ( set.add(holder.memberid) ) {
                                list.add(holder);
                            }
                            break;
                        default:
                            break;
                    }
                }

                Log.d("TAG", list.size()+"");
                return list;
            }
        };
    }

    private static class ViewHolder {

        TextView selections;
    }
}
