package net.fandm.lee.mymicrophone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class VoiceMemo extends AppCompatActivity {

    private ArrayAdapter<String> aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_memo);

        ArrayList<String> voice_recordings = new ArrayList<String>();

        voice_recordings.add("hello");
        voice_recordings.add("hello world");
        ListView lv=(ListView)findViewById(R.id.list);
        this.aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, voice_recordings);
        lv.setAdapter(aa);

    }
}