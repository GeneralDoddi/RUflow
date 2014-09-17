package is.ru.app;

import java.util.List;

/**
 * Created by Þórður on 15.9.2014.
 */
public class Puzzle {

    private int size;
    private List<String> flows;

    Puzzle(int mSize, List<String> mFlows){
        size = mSize;
        flows = mFlows;
    }

    int getSize(){ return size;}
    List<String> getFlows() {return flows;}

}
/*
<puzzle id="1">
<size>5</size>
<flows>(0 0 1 4), (2 0 1 3), (2 1 2 4), (4 0 3 3), (4 1 3 4)</flows>
</puzzle>*/