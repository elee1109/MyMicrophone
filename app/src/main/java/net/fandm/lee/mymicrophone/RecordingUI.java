package net.fandm.lee.mymicrophone;

import android.media.MediaRecorder;

import java.io.IOException;

public class RecordingUI {


    public void startRecording() {
        MediaRecorder recording = new MediaRecorder();
        recording.setAudioSource(MediaRecorder.AudioSource.MIC);
        recording.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recording.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recording.setAudioSamplingRate(44100);
        recording.setAudioEncodingBitRate(192000);
        try {
            recording.prepare();
            recording.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
