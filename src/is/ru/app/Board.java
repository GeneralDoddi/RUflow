package is.ru.app;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board extends View {

    private Global mGlobals = Global.getInstance();

    private final int NUM_CELLS = 5;
    private int m_cellWidth;
    private int m_cellHeight;

    // Tilraun ad bordi med 2 raudum punktum
    private String m_board = "R.G.Y..B.W......G.Y..RBW.";
    // "R.G.Y..B.O......G.Y..R.B.O.;


    // Nytt shape fyrir hring
    private ShapeDrawable m_shape = new ShapeDrawable( new OvalShape());

    private Rect m_rect = new Rect();
    private Paint m_paintGrid  = new Paint();
    private Paint m_paintPath  = new Paint();
    private Path m_path = new Path();

    private Cellpath m_cellPath = new Cellpath();

    private ArrayList<Cellpath> allCellPaths = new ArrayList<Cellpath>();

    private ArrayList<Integer> colorList = new ArrayList<Integer>();

    // Cellpath array to keep all paths
    private Cellpath m_redPathList = new Cellpath();
    private Cellpath m_greenPathList = new Cellpath();
    private Cellpath m_bluePathList = new Cellpath();
    private Cellpath m_yellowPathList = new Cellpath();
    private Cellpath m_whitePathList = new Cellpath();

    private int colorMeTimbers = 0;

    //Predefined 4 litir


    private int xToCol( int x ) {
        return (x - getPaddingLeft()) / m_cellWidth;
    }

    private int yToRow( int y ) {
        return (y - getPaddingTop()) / m_cellHeight;
    }

    private int colToX( int col ) {
        return col * m_cellWidth + getPaddingLeft() ;
    }

    private int rowToY( int row ) {
        return row * m_cellHeight + getPaddingTop() ;
    }

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_paintGrid.setStyle( Paint.Style.STROKE );
        m_paintGrid.setColor( Color.GRAY );

        m_paintPath = createPainter(Color.RED);

        allCellPaths.add(m_redPathList);
        allCellPaths.add(m_greenPathList);
        allCellPaths.add(m_bluePathList);
        allCellPaths.add(m_yellowPathList);
        allCellPaths.add(m_whitePathList);


        colorList.add(Color.RED);
        colorList.add(Color.GREEN);
        colorList.add(Color.BLUE);
        colorList.add(Color.YELLOW);
        colorList.add(Color.WHITE);
        colorList.add(Color.CYAN);
        colorList.add(Color.GRAY);
        colorList.add(Color.MAGENTA);
        colorList.add(Color.BLACK);
        colorList.add(Color.DKGRAY);

    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width  = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int size = Math.min(width, height);
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(),
                size + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onSizeChanged( int xNew, int yNew, int xOld, int yOld ) {
        int sw = Math.max(1, (int) m_paintGrid.getStrokeWidth());
        m_cellWidth  = (xNew - getPaddingLeft() - getPaddingRight() - sw) / NUM_CELLS;
        m_cellHeight = (yNew - getPaddingTop() - getPaddingBottom() - sw) / NUM_CELLS;
    }

    //Stadsetning fyrir punkta a mappinu
    public char getBoard( int col, int row ) {
        return m_board.charAt(col + row * NUM_CELLS);
    }



    @Override
    protected void onDraw( Canvas canvas ) {


        for ( int r=0; r<NUM_CELLS; ++r ) {
            for (int c = 0; c<NUM_CELLS; ++c) {
                int x = colToX( c );
                int y = rowToY( r );
                m_rect.set(x, y, x + m_cellWidth, y + m_cellHeight);
                canvas.drawRect( m_rect, m_paintGrid );

                // tilraun til ad teikna hringi a bordid

                m_shape.setBounds(m_rect);
                //char dot = getBoard(c, r);
                Coordinate flowCord = new Coordinate(c,r);
                if(mGlobals.flowCoord.contains(flowCord)){
                    m_shape.getPaint().setColor(colorList.get(mGlobals.flowCoord.indexOf(flowCord)/2));
                    m_shape.draw(canvas);
                }
            }
        }

        // Teikni path til ad teikna linuna i real time
        m_path.reset();
        if ( !m_cellPath.isEmpty() ) {
            m_path = new Path();
            List<Coordinate> colist = m_cellPath.getCoordinates();
            //Coordinate co;
            Coordinate co = colist.get( 0 );
            m_path.moveTo( colToX(co.getCol()) + m_cellWidth / 2,
                           rowToY(co.getRow()) + m_cellHeight / 2 );
            for ( int i=0; i<colist.size(); ++i ) {
                co = colist.get(i);
                m_path.lineTo( colToX(co.getCol()) + m_cellWidth / 2,
                        rowToY(co.getRow()) + m_cellHeight / 2 );
            }
            canvas.drawPath( m_path, createPainter(colorMeTimbers));
        }
        // cellPathList er dreginn upp til ad teikna upp alla adra saveada cellpaths

        for(Cellpath tempPath:allCellPaths) {
            if (!tempPath.isEmpty()) {
                m_path = new Path();

                List<Coordinate> colist = tempPath.getCoordinates();

                Coordinate co = colist.get(0);
                m_path.moveTo(colToX(co.getCol()) + m_cellWidth / 2,
                        rowToY(co.getRow()) + m_cellHeight / 2);
                for (int i = 0; i < colist.size(); ++i)
                {
                    co = colist.get(i);
                    m_path.lineTo(colToX(co.getCol()) + m_cellWidth / 2,
                            rowToY(co.getRow()) + m_cellHeight / 2);
                }
                //System.out.println(allCellPaths.indexOf(tempPath));
                canvas.drawPath( m_path, createPainter(colorList.get(allCellPaths.indexOf(tempPath))));
            }
        }

    }

    private boolean areNeighbours( int c1, int r1, int c2, int r2 ) {
        return Math.abs(c1-c2) + Math.abs(r1-r2) == 1;
    }

    @Override
    public boolean onTouchEvent( MotionEvent event ) {

        int x = (int) event.getX();         // NOTE: event.getHistorical... might be needed.
        int y = (int) event.getY();
        int c = xToCol( x );
        int r = yToRow( y );
        int arraySlot = 0;

        if ( c >= NUM_CELLS || r >= NUM_CELLS ) {
            return true;
        }

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                // WORK IN PROGRESS, NEEDS FIXING!
                Coordinate tempCord = new Coordinate(c , r);
                for(Cellpath tempPath:allCellPaths){
                        if (tempPath.getCoordinates().contains(tempCord) || mGlobals.flowCoord.contains(tempCord)) {
                            if (mGlobals.flowCoord.contains(tempCord)) {
                                tempPath.reset();
                            } else {
                                tempPath.trim(tempCord);
                            }
                            m_paintPath.setColor(colorList.get(allCellPaths.indexOf(tempPath)));
                            colorMeTimbers = colorList.get(allCellPaths.indexOf(tempPath));
                            tempPath.append(tempCord);
                            break;
                        }
                }
                /*char color = getBoard(c, r);
                Coordinate tempCord = new Coordinate(c , r);
                if ((color == 'R' || m_redPathList.getCoordinates().contains(tempCord)) )
                {
                    //If we're on a starting dot, we want to reset the whole path.
                    if(color == 'R')
                    {
                        m_redPathList.reset();
                    }
                    else
                    {
                        m_redPathList.trim(tempCord);
                    }

                    m_paintPath.setColor(Color.RED);
                    colorMeTimbers = Color.RED;
                    m_redPathList.append(tempCord);


                }
                if (color == 'G' ||  m_greenPathList.getCoordinates().contains(tempCord)) {
                    //If we're on a starting dot, we want to reset the whole path.
                    if(color == 'G')
                    {
                        m_greenPathList.reset();
                    }
                    else
                    {
                        m_greenPathList.trim(tempCord);
                    }

                    m_paintPath.setColor(Color.GREEN);
                    colorMeTimbers = Color.GREEN;
                    m_greenPathList.append(tempCord);
                }
                if (color == 'B' ||  m_bluePathList.getCoordinates().contains(tempCord)) {
                    //If we're on a starting dot, we want to reset the whole path.
                    if(color == 'B')
                    {
                        m_bluePathList.reset();
                    }
                    else
                    {
                        m_greenPathList.trim(tempCord);
                    }

                    m_paintPath.setColor(Color.BLUE);
                    colorMeTimbers = Color.BLUE;
                    m_bluePathList.append(tempCord);
                }
                if (color == 'Y' ||  m_yellowPathList.getCoordinates().contains(tempCord)) {
                    //If we're on a starting dot, we want to reset the whole path.
                    if(color == 'Y')
                    {
                        m_yellowPathList.reset();
                    }
                    else
                    {
                        m_greenPathList.trim(tempCord);
                    }

                    m_paintPath.setColor(Color.YELLOW);
                    colorMeTimbers = Color.YELLOW;
                    m_yellowPathList.append(tempCord);
                }
                if (color == 'W' ||  m_whitePathList.getCoordinates().contains(tempCord)) {
                    //If we're on a starting dot, we want to reset the whole path.
                    if(color == 'W')
                    {
                        m_whitePathList.reset();
                    }
                    else
                    {
                        m_greenPathList.trim(tempCord);
                    }

                    m_paintPath.setColor(Color.WHITE);
                    colorMeTimbers = Color.WHITE;
                    m_whitePathList.append(tempCord);
                }*/


            }
            else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                Coordinate tempCord = new Coordinate(c , r);
                char color = getBoard(c, r);
                //m_path.lineTo( colToX(c) + m_cellWidth / 2, rowToY(r) + m_cellHeight / 2 );
                if(movementConflict(colorMeTimbers, color) && !tooFar(colorMeTimbers)) {

                    lineConflict(tempCord, colorMeTimbers);
                    if (!m_redPathList.isEmpty()) {

                        List<Coordinate> coordinateList = m_redPathList.getCoordinates();
                        Coordinate last = coordinateList.get(coordinateList.size() - 1);
                        if (areNeighbours(last.getCol(), last.getRow(), c, r)) {
                            m_redPathList.append(new Coordinate(c, r));
                            invalidate();
                        }
                    }
                }

                if(movementConflict(colorMeTimbers, color) && !tooFar(colorMeTimbers)) {
                    lineConflict(tempCord, colorMeTimbers);
                    if (!m_greenPathList.isEmpty()) {

                        List<Coordinate> coordinateList = m_greenPathList.getCoordinates();
                        Coordinate last = coordinateList.get(coordinateList.size() - 1);
                        if (areNeighbours(last.getCol(), last.getRow(), c, r)) {
                            m_greenPathList.append(new Coordinate(c, r));
                            invalidate();
                        }
                    }
                }
                if(movementConflict(colorMeTimbers, color) && !tooFar(colorMeTimbers)) {
                    lineConflict(tempCord, colorMeTimbers);
                    if (!m_bluePathList.isEmpty()) {

                        List<Coordinate> coordinateList = m_bluePathList.getCoordinates();
                        Coordinate last = coordinateList.get(coordinateList.size() - 1);
                        if (areNeighbours(last.getCol(), last.getRow(), c, r)) {
                            m_bluePathList.append(new Coordinate(c, r));
                            invalidate();
                        }
                    }
                }
                if(movementConflict(colorMeTimbers, color) && !tooFar(colorMeTimbers)) {
                    lineConflict(tempCord, colorMeTimbers);
                    if (!m_yellowPathList.isEmpty()) {
                        List<Coordinate> coordinateList = m_yellowPathList.getCoordinates();
                        Coordinate last = coordinateList.get(coordinateList.size() - 1);
                        if (areNeighbours(last.getCol(), last.getRow(), c, r)) {
                            m_yellowPathList.append(new Coordinate(c, r));
                            invalidate();
                        }
                    }
                }
                if(movementConflict(colorMeTimbers, color) && !tooFar(colorMeTimbers)) {
                    lineConflict(tempCord, colorMeTimbers);
                    if (!m_whitePathList.isEmpty()) {
                        List<Coordinate> coordinateList = m_whitePathList.getCoordinates();
                        Coordinate last = coordinateList.get(coordinateList.size() - 1);
                        if (areNeighbours(last.getCol(), last.getRow(), c, r)) {
                            m_whitePathList.append(new Coordinate(c, r));
                            invalidate();
                        }
                    }
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
               // m_cellPath = new Cellpath();
                if(isWin()){
                    Toast.makeText(getContext(),"You Win!", Toast.LENGTH_SHORT).show();
                }
            }

        return true;
    }

    public void setColor( int color ) {
        m_paintPath.setColor( color );
        invalidate();
    }
    //Cuts all paths that our line is going over
    public void lineConflict(Coordinate co, int color)
    {
        if(m_greenPathList.getCoordinates().contains(co) && color != Color.GREEN)
        {
            m_greenPathList.conflict(co);
        }
        if(m_redPathList.getCoordinates().contains(co) && color != Color.RED)
        {
            m_redPathList.conflict(co);
        }
        if(m_yellowPathList.getCoordinates().contains(co) && color != Color.YELLOW)
        {
            m_yellowPathList.conflict(co);
        }
        if(m_whitePathList.getCoordinates().contains(co) && color != Color.WHITE)
        {
            m_whitePathList.conflict(co);
        }
        if(m_bluePathList.getCoordinates().contains(co) && color != Color.BLUE)
        {
            m_bluePathList.conflict(co);
        }
    }

    //Check what current color is and if the color code isn't the wrong one
    public boolean movementConflict(int colorMeTimbers, char colorCode)
    {

        if(colorMeTimbers == Color.RED && (colorCode != 'G'  && colorCode != 'Y' && colorCode != 'B' && colorCode != 'W'))
        {

            return true;
        }
        if(colorMeTimbers == Color.GREEN && (colorCode != 'R'  && colorCode != 'Y' && colorCode != 'B' && colorCode != 'W'))
        {
            return true;
        }
        if(colorMeTimbers == Color.BLUE && (colorCode != 'R'  && colorCode != 'Y' && colorCode != 'G' && colorCode != 'W'))
        {
            return true;
        }
        if(colorMeTimbers == Color.YELLOW && (colorCode != 'R'  && colorCode != 'G' && colorCode != 'B' && colorCode != 'W'))
        {
            return true;
        }
        if(colorMeTimbers == Color.WHITE && (colorCode != 'R'  && colorCode != 'Y' && colorCode != 'B' && colorCode != 'G'))
        {
            return true;
        }

        return false;
    }
    //Prevents us from going from a start point of a color,
    // and then to an endpoint and continuing through it.
    public boolean tooFar(int colorMeTimbers)
    {
        int redSize = m_redPathList.getCoordinates().size();
        int greenSize = m_greenPathList.getCoordinates().size();
        int blueSize = m_bluePathList.getCoordinates().size();
        int yellowSize = m_yellowPathList.getCoordinates().size();
        int whiteSize = m_whitePathList.getCoordinates().size();

        if(redSize > 1) {
            Coordinate lastRed = m_redPathList.getCoordinates().get(redSize - 1);
            if(colorMeTimbers == Color.RED && getBoard(lastRed.getCol(), lastRed.getRow()) == 'R' && redSize > 1 )
                return true;
        }
       if(greenSize > 1)
       {
           Coordinate lastGreen = m_greenPathList.getCoordinates().get(greenSize-1);
           if(colorMeTimbers == Color.GREEN && getBoard(lastGreen.getCol(), lastGreen.getRow()) == 'G' && greenSize > 1 )
               return true;
       }
        if(blueSize > 1)
        {
            Coordinate lastBlue = m_bluePathList.getCoordinates().get(blueSize-1);
            if(colorMeTimbers == Color.BLUE && getBoard(lastBlue.getCol(), lastBlue.getRow()) == 'B' && blueSize > 1 )
                return true;

        }
        if(yellowSize > 1)
        {
            Coordinate lastYellow = m_yellowPathList.getCoordinates().get(yellowSize-1);
            if(colorMeTimbers == Color.YELLOW && getBoard(lastYellow.getCol(), lastYellow.getRow()) == 'Y' && yellowSize > 1 )
                return true;
        }
        if(whiteSize > 1)
        {
            Coordinate lastWhite = m_whitePathList.getCoordinates().get(whiteSize-1);
            if(colorMeTimbers == Color.WHITE && getBoard(lastWhite.getCol(), lastWhite.getRow()) == 'W' && whiteSize > 1 )
                return true;
        }
        return false;
    }
    public Paint createPainter(int color)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(80);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setAntiAlias(true);
        return paint;
    }

    public boolean isWin()
    {
        int totalSize = m_redPathList.getCoordinates().size() + m_greenPathList.getCoordinates().size() + m_bluePathList.getCoordinates().size()
                + m_whitePathList.getCoordinates().size() + m_yellowPathList.getCoordinates().size();

        int dotSize = 0;

        for(Cellpath cellPath: allCellPaths) {
            for (Coordinate tempCord : m_redPathList.getCoordinates()) {

                if ((getBoard(tempCord.getCol(), tempCord.getRow()) == 'R') ||
                        (getBoard(tempCord.getCol(),tempCord.getCol()) == 'G') ||
                        (getBoard(tempCord.getCol(),tempCord.getCol()) == 'B') ||
                        (getBoard(tempCord.getCol(),tempCord.getCol()) == 'Y') ||
                        (getBoard(tempCord.getCol(),tempCord.getCol()) == 'W')
                        ) {
                    dotSize++;
                }
            }
        }

        if((dotSize == allCellPaths.size()*2) && (totalSize == (NUM_CELLS * NUM_CELLS))){
            return true;
        }

       return false;

    }
}
