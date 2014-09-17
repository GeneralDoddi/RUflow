package is.ru.app;

import android.app.ListActivity;
import android.content.Intent;
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
public class PuzzleSelectActivity extends ListActivity{

    private Global mGlobals = Global.getInstance();

    public class Puzzle {

        private int size;
        private String flows;
        private int id;

        Puzzle(int mSize, String mFlows, int mId){
            size = mSize;
            flows = mFlows;
            id = mId;
        }

        int getSize(){ return size;}
        String getFlows() {return flows;}
        int getId() {return id;}

        public String toString(){
            return "Puzzle no. " + getId();
        }
    }

    ArrayList<Puzzle> puzzleList = new ArrayList<Puzzle>();

    @Override
    public void onCreate(Bundle savedInstanceState){

        List<Puzzle> mPuzzles = new ArrayList<Puzzle>();

        super.onCreate(savedInstanceState);

        try {
            List<Puzzle> packs = new ArrayList<Puzzle>();
            readLevels(getAssets().open(mGlobals.packSelect), packs);
            mPuzzles = packs;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        for(Puzzle puzzle: mPuzzles) {
            puzzleList.add(new Puzzle(puzzle.getSize(),puzzle.getFlows(),puzzle.getId()));
        }

        ArrayAdapter<Puzzle> adapter = new ArrayAdapter<Puzzle>(
                this, android.R.layout.simple_expandable_list_item_1, puzzleList);

        setListAdapter (adapter);
    }

    @Override
    protected void onListItemClick( ListView l, View v, int position, long id){
        Puzzle colorElement = (Puzzle) l.getAdapter().getItem(position);



        startActivity(new Intent( this, PlayActivity.class));
        /*
        SharedPreferences settings = getSharedPreferences( "ColorPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        //editor.putInt ("pathColor", colorElement.getColor());
        editor.commit();*/
    }

    private void readLevels( InputStream is, List<Puzzle> puzzles) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse( is );
            NodeList nList = doc.getElementsByTagName( "puzzle" );
            for ( int c=0; c<nList.getLength(); ++c ) {
                Node nNode = nList.item(c);
                 if ( nNode.getNodeType() == Node.ELEMENT_NODE ) {
                    NodeList nList2 =  nNode.getChildNodes();
                   // for(int i = 0; i < nList2.getLength(); ++i) {
                        Element eNode = (Element) nNode;
                        String size = eNode.getElementsByTagName("size").item(0).getFirstChild().getNodeValue();
                        String flows = eNode.getElementsByTagName("flows").item(0).getFirstChild().getNodeValue();
                        int id = c+1;

                        puzzles.add(new Puzzle(Integer.parseInt(size), flows, id));
                    //}
                }
            }
        }
        catch ( Exception e){
            e.printStackTrace();
        }
    }
}
