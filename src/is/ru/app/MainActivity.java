package is.ru.app;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by olafurn on 9.9.2014.
 */
public class MainActivity extends Activity {



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


    }

    public void buttonClick(View view){
        Button button = (Button) view;
        int id = button.getId();

        if(id == R.id.button_play) {
            //startActivity(new Intent( this, PlayActivity.class));
            startActivity(new Intent( this, SelectActivity.class));
        }
        else if (id == R.id.button_color){
            startActivity(new Intent(this, ColorListActivity.class));
        }

    }


}
