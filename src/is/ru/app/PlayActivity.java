package is.ru.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/* sound shit */

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.widget.Toast;

/**
 * Created by olafurn on 9.9.2014.
 */
public class PlayActivity extends Activity {

      /* sound shit */

    private SoundPool soundPool;
    private int soundID;
    boolean plays = false, loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        SharedPreferences settings = getSharedPreferences("ColorPref", MODE_PRIVATE);

        int color = settings.getInt("pathColor", Color.CYAN);
        Board board = (Board) findViewById(R.id.board);
        board.setColor(color);


        /* Sound shit*/

        /* Shit til að stilla volume */
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        /* Hardware buttons settting til að adjusta shit sound */
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //Counter til að recogniza stream id af sound shitti
        counter = 0;

        //Loada sound og shit
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        soundID = soundPool.load(this, R.raw.sturt, 1);
    }


    public void buttonClick(View view){
        Button button = (Button) view;
        int id = button.getId();

        if(id == R.id.play_again) {
            startActivity(new Intent( this, PlayActivity.class));
        }
    }

    public void playSound(View v) {
        // Is the sound loaded does it already play?
        if (loaded && !plays) {
            soundPool.play(soundID, volume, volume, 1, 0, 1f);
            counter = counter++;
            Toast.makeText(this, "Played sound", Toast.LENGTH_SHORT).show();
            plays = true;
        }
    }

    public void playLoop(View v) {
        // Is the sound loaded does it already play?
        if (loaded && !plays) {

            // the sound will play for ever if we put the loop parameter -1
            soundPool.play(soundID, volume, volume, 1, -1, 1f);
            counter = counter++;
            Toast.makeText(this, "Plays loop", Toast.LENGTH_SHORT).show();
            plays = true;
        }
    }

    public void pauseSound(View v) {
        if (plays) {
            soundPool.pause(soundID);
            soundID = soundPool.load(this, R.raw.sturt, counter);
            Toast.makeText(this, "Pause sound", Toast.LENGTH_SHORT).show();
            plays = false;
        }
    }

    public void stopSound(View v) {
        if (plays) {
            soundPool.stop(soundID);
            soundID = soundPool.load(this, R.raw.sturt, counter);
            Toast.makeText(this, "Stop sound", Toast.LENGTH_SHORT).show();
            plays = false;
        }
    }
}
