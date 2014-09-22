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
        private String name;
        private String challengeName;

        Puzzle(int mSize, String mFlows, int mId, String mName, String mChallengeName){
            size = mSize;
            flows = mFlows;
            id = mId;
            name = mName;
            challengeName = mChallengeName;
        }

        int getSize(){ return size;}
        String getFlows() {return flows;}
        int getId() {return id;}
        String getName() {return name;}
        String getChallengeName() {return challengeName;}

        public String toString(){
            return getChallengeName() + " - " + getName();
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
            puzzleList.add(new Puzzle(puzzle.getSize(),puzzle.getFlows(),puzzle.getId(), puzzle.getName(), puzzle.getChallengeName()));
        }

        ArrayAdapter<Puzzle> adapter = new ArrayAdapter<Puzzle>(
                this, android.R.layout.simple_expandable_list_item_1, puzzleList);

        setListAdapter (adapter);
    }

    @Override
    protected void onListItemClick( ListView l, View v, int position, long id){
        Puzzle selectedPuzzle = (Puzzle) l.getAdapter().getItem(position);

        mGlobals.flowCoord = flowList(selectedPuzzle);
        mGlobals.mSize = selectedPuzzle.getSize();

        startActivity(new Intent(this, PlayActivity.class));
    }

    private ArrayList<Coordinate> flowList(Puzzle selectedPuzzle){

        ArrayList<Coordinate> flowList = new ArrayList<Coordinate>();

        String[] test = selectedPuzzle.getFlows().split(", ");
        ArrayList<String[]> split = new ArrayList<String[]>();
        for(String temp:test) {
            split.add(temp.substring(1).split("[ () ]"));
        }

        for(String[] tempString: split){
            for(int i = 0; i < tempString.length; i = i + 2){
                Coordinate flowCoord = new Coordinate(Integer.parseInt(tempString[i]), Integer.parseInt(tempString[i+1]));
                flowList.add(flowCoord);
            }
        }

        return flowList;
    }

    private void readLevels( InputStream is, List<Puzzle> puzzles) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse( is );
            NodeList nList = doc.getElementsByTagName( "challenge" );
            for ( int c=0; c<nList.getLength(); ++c ) {
                Node nNode = nList.item(c);
                 if ( nNode.getNodeType() == Node.ELEMENT_NODE ) {


                        Element eNode = (Element) nNode;
                        NodeList nList2 =  eNode.getElementsByTagName("puzzle");
                        String challengeName = ((Element) nNode).getAttribute("name");
                        for(int i = 0; i<nList2.getLength();++i) {
                            Node tempNode = nList2.item(i);

                            if ( nNode.getNodeType() == Node.ELEMENT_NODE ) {
                                Element eNodetmp = (Element) tempNode;
                                String size = eNodetmp.getElementsByTagName("size").item(0).getFirstChild().getNodeValue();
                                String flows = eNodetmp.getElementsByTagName("flows").item(0).getFirstChild().getNodeValue();
                                String name = eNodetmp.getAttribute("id");
                                int id = c + 1;
                                puzzles.add(new Puzzle(Integer.parseInt(size), flows, id, name, challengeName));
                            }
                        }
                }
            }
        }
        catch ( Exception e){
            e.printStackTrace();
        }
    }
}
