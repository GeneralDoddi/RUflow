package is.ru.app;

import java.util.List;

/**
 * Created by Þórður on 15.9.2014.
 */
public class MapPack {

    private List<Puzzle> puzzleList;



    MapPack( List<Puzzle> puzzlelist )  {
        puzzleList = puzzlelist;
    }

    List<Puzzle> getPuzzleList(){
        return puzzleList;
    }
}
