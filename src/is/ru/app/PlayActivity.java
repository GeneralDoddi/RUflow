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
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by olafurn on 9.9.2014.
 */
public class PlayActivity extends Activity {



    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        SharedPreferences settings = getSharedPreferences("ColorPref", MODE_PRIVATE);

        int color = settings.getInt("pathColor", Color.CYAN);
        Board board = (Board) findViewById(R.id.board);
        board.setColor(color);

    }
}
