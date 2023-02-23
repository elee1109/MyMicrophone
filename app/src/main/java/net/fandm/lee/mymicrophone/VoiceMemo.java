package net.fandm.lee.mymicrophone;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class VoiceMemo extends AppCompatActivity {

    private ArrayAdapter<String> aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_memo);
        Button recordButton = (Button)findViewById(R.id.record_button);
        recordButton.setOnClickListener(new View.OnClickListener(){


            private ArrayAdapter<String> aa;

            @Override
            public void onClick(View v) {

                RecordingUI recording = new RecordingUI();
                recording.startRecording();
                //ArrayList<RecordingUI> voice_recordings = new ArrayList<RecordingUI>();
                ListView lv=(ListView)findViewById(R.id.list);
                ArrayList<String> voice_recordings = new ArrayList<String>();
                voice_recordings.add("hello world");
                this.aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, voice_recordings);
                //aa = new ArrayAdapter<RecordingUI>(getApplicationContext(), android.R.layout.simple_list_item_1, voice_recordings);
                lv.setAdapter(aa);
            }
        });





    }
}