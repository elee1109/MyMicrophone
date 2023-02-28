package net.fandm.lee.mymicrophone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class VoiceMemo extends AppCompatActivity {
    //need to use onSaveInstanceState and onRestoreInstanceState to save the state of the app
    private MediaRecorder recorder;
    private boolean isRecording = false;

    private ArrayAdapter<String> aa;
    private AudioArrayAdapter aaa;
    private File file;
    private ListView lv;

    private boolean hasPermission = false;
    public ArrayList<String> file_list = new ArrayList<String>();

    /**
     *
     * TODO:: delete the file when the user long clicks on the file
     * TODO:: add a boolean to onPermissionResult to check if the user has given permission to record audio
     * TODO:: add the boolean to the if statement in the record button
     * TODO:: make the arrayadapter always display files even when the app is closed.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_memo);


        ActivityCompat.requestPermissions(VoiceMemo.this, new String[]{
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);




        Button recordButton = (Button) findViewById(R.id.record_button);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                String fileName = file_list.get(position);
                File targetFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName);
                if(targetFile.exists()){
                    boolean isGone = targetFile.delete();
                    if(isGone){
                        Toast.makeText(getApplicationContext(), "File deleted", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "File not deleted", Toast.LENGTH_LONG).show();
                    }
                }
                playRecording(view, targetFile);


            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = file_list.get(position);

                File targetFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName);
                targetFile.delete();
                aaa.remove(aaa.getItem(position));
                aaa.notifyDataSetChanged();


                return false;

            }
        });

        recordButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(hasPermission) {
                    if (!isRecording) {
                        file = startRecording();
                        recordButton.setForeground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.baseline_stop_24));
                        recordButton.setBackgroundResource(R.drawable.red_stop);
                        isRecording = true;
                    } else {
                        stopRecording(file);
                        recordButton.setForeground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.baseline_mic_24));
                        recordButton.setBackgroundResource(R.drawable.record_bttn);
                        isRecording = false;
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please give permission to record audio", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(VoiceMemo.this, new String[]{
                            android.Manifest.permission.RECORD_AUDIO,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }

            }
        });
    }
            private File getPath() {
                String state = Environment.getExternalStorageState();
                //check if external storage is mounted
                if (!state.equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(getApplicationContext(), "external storage not mounted", Toast.LENGTH_LONG).show();
                    return null;
                }
                //create directory if it doesn't exist
                File audioDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
                File targetFile = new File(audioDir, "MyMicrophone");
                if (!targetFile.exists()) {
                    targetFile.mkdirs();
                }
                Date date = new Date();
                String temp = date.toString();
                String timeStamp = temp.replace(" ", "_");
                Log.d("TIMESTAMP", timeStamp);
                targetFile = new File(targetFile, timeStamp + ".m4a");
                return targetFile;
            }
            private File startRecording() {
                File targetFile = getPath();
                Log.d("StartRecording file name:", targetFile.getName());
                try {
                    //basic MediaRecorder configuration autofilled using Github Copilot (free trial is 60 days)
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
                    Log.d("ERROR", "prepare()/Configuration failed");
                }
                return targetFile;

            }


            private void stopRecording(File targetFile) {

                Log.d("StopRecording file name:", targetFile.getName());
                recorder.stop();
                recorder.release();
                recorder = null;

                file_list.add(targetFile.getName());
                Log.d("FILE PATH", targetFile.getAbsolutePath());
                aaa = new AudioArrayAdapter(getApplicationContext(), file_list);
                lv = (ListView) findViewById(R.id.list);
                lv.setAdapter(aaa);

            }

            public void playRecording(View view, File targetFile) {
                MediaPlayer player = new MediaPlayer();
                FileInputStream fis = null;
                if (this.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PERMISSION", "Permission is granted");
                    try {
                        /**
                         * //FOUND SOLUTION IN THIS VIDEO:
                         * https://www.youtube.com/watch?v=huFPqGmQRPA
                         */
                        Log.d("PLAYING", targetFile.getAbsolutePath());
                        File directory = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC) + ("/MyMicrophone/") +targetFile.getName());
                        fis = new FileInputStream(directory);
                        player.setDataSource(fis.getFD());
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.prepare();
                        player.start();
                        //this plays the animation, was previously in the onItemLongClickListener but had to be moved since it needs to stop in onCompletionListener for MediaPlayer
                        ImageView playArrow = view.findViewById(R.id.play_arrow);
                        view.setVisibility(View.VISIBLE);
                        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.blink);
                        playArrow.startAnimation(animation);
                        /**
                         * onCompletion autocompleted with co pilot
                         */
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                playArrow.clearAnimation();
                                view.setVisibility(View.VISIBLE);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fis != null) {
                            try{
                                fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
                else{
                    ActivityCompat.requestPermissions(VoiceMemo.this, new String[]{
                            android.Manifest.permission.RECORD_AUDIO,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


                }

            }

            public void onPermissionCallback(int requestCode, String[] permissions, int[] grantResults) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "permission granted", Toast.LENGTH_LONG).show();
                    hasPermission = true;
                } else {
                    Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_LONG).show();
                    hasPermission = false;


                }
            }
    @Override
    protected void onResume() {
        super.onResume();
        // Prepare the MediaRecorder when the activity is resumed
        if(recorder != null) {
            recorder.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Release the MediaRecorder when the activity is paused
        if(recorder != null) {
            recorder.release();
        }
    }

    @Override
        protected void onDestroy() {
            super.onDestroy();
            // Release the MediaRecorder when the activity is destroyed
            if(recorder != null) {
                recorder.release();
            }
        }
    //from slides on activity lifecycle
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList("file_list", file_list);
    }
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        file_list = savedInstanceState.getStringArrayList("file_list");
        aaa = new AudioArrayAdapter(getApplicationContext(), file_list);
        lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(aaa);

    }
}


