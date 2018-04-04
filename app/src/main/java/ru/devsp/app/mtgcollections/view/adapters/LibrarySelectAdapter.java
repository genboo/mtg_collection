package ru.devsp.app.mtgcollections.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.devsp.app.mtgcollections.R;
import ru.devsp.app.mtgcollections.model.objects.Library;

/**
 *
 * Created by gen on 05.10.2017.
 */

public class LibrarySelectAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Library> mItems;

    public LibrarySelectAdapter(@NonNull Context context,  List<Library> objects) {
        mInflater = LayoutInflater.from(context);
        mItems = objects;
    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mItems.get(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = mInflater.inflate(R.layout.list_item_simple_text, null);
        }
        TextView name = view.findViewById(R.id.tv_text);
        name.setText(mItems.get(i).name);
        return view;
    }
}
