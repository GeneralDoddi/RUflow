package is.ru.app;

import java.util.List;

/**
 * Created by Þórður on 15.9.2014.
 */
public class MapPack{

    private String name;
    private String description;
    private String file;


    MapPack(String mName, String mDescription, String mFile){
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
