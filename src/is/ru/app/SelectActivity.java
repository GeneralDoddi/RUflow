package is.ru.app;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
 * Created by Þórður on 15.9.2014.
 */
public class SelectActivity extends ListActivity {

    private Global mGlobals = Global.getInstance();

    public class MapPacks{

        private String name;
        private String description;
        private String file;


        MapPacks(String mName, String mDescription, String mFile){
            name = mName;
            description = mDescription;
            file = mFile;
        }

        String getName(){return name;}
        String getDescription(){return description;}
        String getFile(){return file;}

        public String toString(){
            return "" + getName() + " : " + getDescription();
        }
    }

    ArrayList<MapPacks> challengeList = new ArrayList<MapPacks>();

    @Override
    public void onCreate(Bundle savedInstanceState){

        List<Pack> mPacks = new ArrayList<Pack>();

        super.onCreate(savedInstanceState);

        try {
            List<Pack> packs = new ArrayList<Pack>();
            readPack(getAssets().open("packs/packs.xml"), packs);
            mPacks = packs;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        for(Pack pack: mPacks) {
            challengeList.add(new MapPacks(pack.getName(),pack.getDescription(),pack.getFile()));
        }

        ArrayAdapter<MapPacks> adapter = new ArrayAdapter<MapPacks>(
                this, android.R.layout.simple_expandable_list_item_1, challengeList);

        setListAdapter (adapter);
    }

    @Override
    protected void onListItemClick( ListView l, View v, int position, long id){
        MapPacks mapElement = (MapPacks) l.getAdapter().getItem(position);

        mGlobals.packSelect = mapElement.file;


        startActivity(new Intent( this, PuzzleSelectActivity.class));
        /*
        SharedPreferences settings = getSharedPreferences( "ColorPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        //editor.putInt ("pathColor", colorElement.getColor());
        editor.commit();*/
    }



    private void readPack( InputStream is, List<Pack> packs) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse( is );
            NodeList nList = doc.getElementsByTagName( "pack" );
            for ( int c=0; c<nList.getLength(); ++c ) {
                Node nNode = nList.item(c);
                if ( nNode.getNodeType() == Node.ELEMENT_NODE ) {
                    Element eNode = (Element) nNode;
                    String name = eNode.getElementsByTagName( "name" ).item(0).getFirstChild().getNodeValue();
                    String description = eNode.getElementsByTagName( "description" ).item(0).getFirstChild().getNodeValue();
                    String file = eNode.getElementsByTagName( "file" ).item(0).getFirstChild().getNodeValue();
                    packs.add( new Pack( name, description, file ) );
                }
            }
        }
        catch ( Exception e){
            e.printStackTrace();
        }
    }


}
