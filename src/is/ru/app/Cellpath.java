package is.ru.app;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olafurn on 9.9.2014.
 */


public class Cellpath {

    private ArrayList<Coordinate> m_path = new ArrayList<Coordinate>();
    private int color;
    public void append( Coordinate co ) {
        int idx = m_path.indexOf(  co );
        if ( idx >= 0 ) {
            for ( int i=m_path.size()-1; i > idx; --i ) {
                m_path.remove(i);
            }
        }
        else {
            m_path.add(co);
        }
    }

    public void trim(Coordinate co) {
        int idx = m_path.indexOf(co);

        if(idx >= 0)
        {
            for(int i = m_path.size()-1; i > idx; --i)
            {
                m_path.remove(i);
            }

        }
    }

    public void conflict(Coordinate co)
    {
        int idx = m_path.indexOf(co);

        if(idx >= 0)
        {
            for(int i = m_path.size()-1; i > idx; --i)
            {
                m_path.remove(i);

            }
        }
        if(m_path.size() > 1)
        {
            m_path.remove(m_path.size()-1);
        }
        else
        {
            reset();
        }

    }

    public List<Coordinate> getCoordinates() {
        return m_path;
    }

    public void reset() {
        m_path.clear();
    }

    public boolean isEmpty() {
        return m_path.isEmpty();
    }

    //Bætti þessu við hérna en ég held að það þurfi að útfæra þetta annarstaðar.
    public void setColor(int color){this.color = color;}
    public int getColor(){ return color; }

}
