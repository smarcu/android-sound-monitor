package teakdata.com.soundmonitor;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

/**
 * Created by silviu on 30/01/16.
 */
public class SoundMonitor {

    private AudioRecord audioRecord;
    private int minSize;

    private int sampleRateInHz = 8000;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

    public SoundMonitor() {
    }

    public SoundMonitor(AudioRecord audioRecord, int minSize, int sampleRateInHz, int channelConfig, int audioFormat) {
        this.audioRecord = audioRecord;
        this.minSize = minSize;
        this.sampleRateInHz = sampleRateInHz;
        this.channelConfig = channelConfig;
        this.audioFormat = audioFormat;
    }

    public synchronized void start() {
        if (audioRecord == null) {
            minSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRateInHz, channelConfig, audioFormat, minSize);
            audioRecord.getState();
            audioRecord.startRecording();
        }
    }

    public synchronized void stop() {
        if (audioRecord != null) {
            audioRecord.stop();
        }
    }

    public double getAmplitude() {
        short[] buffer = new short[minSize];
        audioRecord.read(buffer, 0, minSize);
        int max = 0;
        for (short s : buffer) {
            if (Math.abs(s) > max) {
                max = Math.abs(s);
            }
        }
        return max;
    }


}
