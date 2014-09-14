package is.ru.app;

import android.graphics.Path;
import android.graphics.Region;

/**
 * Created by olafurn on 14.9.2014.
 */
public class SuperPath  {
    //Color variable
    private int color;
    private Path m_Path;

    public SuperPath(Path src, int color)
    {
        this.m_Path = src;
        this.color = color;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public int getColor()
    {
        return this.color;
    }

}
