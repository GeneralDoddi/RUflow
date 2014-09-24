package is.ru.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

/* sound shit */

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by olafurn on 9.9.2014.
 */
public class PlayActivity extends Activity {

    //Sound
    private static SoundPool soundPool;
    private static int soundID;
    private static int soundID2;
    private static float volume;
    static boolean  loaded = false;
    static boolean playing = false;

    //Timer
    private static double i = 0;
    private static TextView time;
    final static Handler handler = new Handler();
    private static Timer myTimer;

    final static Runnable runner = new Runnable() {
        public void run(){
            int seconds = (int) i / 1000;
            int millis =  (int) i % 1000;
            int minutes = seconds / 60;
            seconds = seconds - minutes * 60;
            String shiz = String.format("%02d:%02d:%02d", minutes, seconds,millis);
            time.setText(shiz);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        SharedPreferences settings = getSharedPreferences("ColorPref", MODE_PRIVATE);

        int color = settings.getInt("pathColor", Color.CYAN);
        Board board = (Board) findViewById(R.id.board);
        board.setColor(color);

        time = (TextView) findViewById(R.id.gameTimer);

        //Load sounds
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });


        soundID = soundPool.load(this, R.raw.pisssss, 1);
        soundID2 = soundPool.load(this, R.raw.sturt, 1);

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actualVolume / maxVolume;
    }

    public static void moveSound() {

        if(loaded)
        {

            soundPool.play(soundID, volume,volume, 1 ,0 , 1f);

        }

    }
    public static void winSound() {

        if(loaded)
        {
            soundPool.play(soundID2, volume,volume, 1 ,0 , 1f);

        }

    }


    public static void startTimer()
    {
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() { UpdateGUI();}
        },0,1);
    }

    public static void stopTimer()
    {
        myTimer.cancel();
        myTimer.purge();
    }
    private static void UpdateGUI() {
        i += 1;
        handler.post(runner);
    }
}
