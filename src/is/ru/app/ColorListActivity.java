package is.ru.app;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Þórður on 9.9.2014.
 */
public class ColorListActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);
    }

    public void buttonClick(View v)
    {
        Switch b = (Switch) findViewById(R.id.sound);

        System.out.println(b.isChecked());
        if(b.isChecked())
        {
            AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
            //
            //
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            amanager.setStreamMute(AudioManager.STREAM_ALARM, false);
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            amanager.setStreamMute(AudioManager.STREAM_RING, false);
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        }
        else
        {
            AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            amanager.setStreamMute(AudioManager.STREAM_RING, true);
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        }

    }


}
