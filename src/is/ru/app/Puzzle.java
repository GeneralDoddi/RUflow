package is.ru.app;

import java.util.List;

/**
 * Created by Þórður on 15.9.2014.
 */
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
/*
<puzzle id="1">
<size>5</size>
<flows>(0 0 1 4), (2 0 1 3), (2 1 2 4), (4 0 3 3), (4 1 3 4)</flows>
</puzzle>*/