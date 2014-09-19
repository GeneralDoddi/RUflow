package is.ru.app;

import java.util.List;

/**
 * Created by Þórður on 15.9.2014.
 */
public class Global {

    public List<Pack> mPacks;
    public String packSelect;
    public List<Coordinate> flowCoord;

    ///
    private static Global mInstance = new Global();

    public static Global getInstance() {
        return mInstance;
    }

    private Global() {}

}
