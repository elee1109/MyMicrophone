package net.fandm.lee.mymicrophone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;


import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class VoiceMemo extends AppCompatActivity {

    private MediaRecorder recorder;
    private boolean isRecording = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_memo);

        ActivityCompat.requestPermissions(VoiceMemo.this, new String[]{
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 1);



        Button recordButton = (Button) findViewById(R.id.record_button);
        ListView lv = (ListView) findViewById(R.id.list);
        recordButton.setOnClickListener(new View.OnClickListener() {


            //private ArrayAdapter<String> aa;

            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    onPermissionCallback(1, new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, new int[]{PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_GRANTED});
                    isRecording = true;
                    recordButton.setForeground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.baseline_crop_square_24));
                    recordButton.setBackgroundResource(R.drawable.red_stop);
                    isRecording = true;
                } else {
                    stopRecording();
                    isRecording = false;
                    recordButton.setForeground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.baseline_mic_24));
                    recordButton.setBackgroundResource(R.drawable.record_bttn);
                    isRecording = false;
                }

            }
        });
    }

            private void startRecording() {

                String state = Environment.getExternalStorageState();

                //check if external storage is mounted
                if (!state.equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(getApplicationContext(), "external storage not mounted", Toast.LENGTH_LONG).show();
                    return;
                }
                //create directory if it doesn't exist
                File audioDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

                File targetFile = new File(audioDir, "recorded_audio.mp4"); //this will be a time stamp?




                try { //basic MediaRecorder configuration found on ChatGPT3
                    recorder = new MediaRecorder();

                    //prompt user for permissions
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

                    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

                    recorder.setOutputFile(targetFile.getAbsolutePath());

                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    recorder.setAudioEncodingBitRate(1600);
                    recorder.setAudioSamplingRate(44100);
                    Log.d("5", "AudioEncoder found");
                    recorder.prepare();
                    recorder.start();

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("ERROR", "prepare() failed");
                }

            }

                //ArrayList<RecordingUI> voice_recordings = new ArrayList<RecordingUI>();
            private void stopRecording() {

                recorder.stop();
                recorder.release();
                recorder = null;
                Toast.makeText(getApplicationContext(), "recording stopped", Toast.LENGTH_LONG).show();
            }

            public void onPermissionCallback(int requestCode, String[] permissions, int[] grantResults) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    startRecording();
                } else {
                    Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_LONG).show();
                    String packageName = getApplicationContext().getPackageName(); //found on chatGPT3, how to kill app
                    ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
                    if(activityManager != null) {
                        activityManager.killBackgroundProcesses(packageName);
                    }

                }
            }






    }

