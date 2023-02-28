package net.fandm.lee.mymicrophone;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * https://medium.com/mindorks/custom-array-adapters-made-easy-b6c4930560dd
 * Basical tutorial for creating a custom array adapter.
 */

public class AudioArrayAdapter extends ArrayAdapter<String> {
    private Context mContext;

    public AudioArrayAdapter(Context context, List<String> objects) {
        super(context, 0, objects);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (convertView == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        String item = getItem(position); //found on ArrayAdapter Doc:    https://developer.android.com/reference/android/widget/ArrayAdapter#getItem(int)
        TextView textView = listItem.findViewById(R.id.time_stamp);
        textView.setText(item);
        return listItem;
    }
}

