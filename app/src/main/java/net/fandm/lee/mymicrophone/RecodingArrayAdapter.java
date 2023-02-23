package net.fandm.lee.mymicrophone;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

public class RecodingArrayAdapter extends ArrayAdapter<RecordingUI> {
    public RecodingArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
