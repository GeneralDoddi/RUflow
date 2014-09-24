package is.ru.app;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




public class Board extends View {

    //timer
    private boolean timeStarted = false;
    private Global mGlobals = Global.getInstance();

    private final int NUM_CELLS = mGlobals.mSize;
    private int m_cellWidth;
    private int m_cellHeight;

    // Nytt shape fyrir hring
    private ShapeDrawable m_shape = new ShapeDrawable( new OvalShape());

    private Rect m_rect = new Rect();
    private Paint m_paintGrid  = new Paint();
    private Paint m_paintPath  = new Paint();
    private Path m_path = new Path();

    private Cellpath m_cellPath = new Cellpath();

    private ArrayList<Cellpath> allCellPaths = new ArrayList<Cellpath>();

    private ArrayList<Integer> colorList = new ArrayList<Integer>();

    private int colorMeTimbers = 0;

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

        for(int i = 0; i < mGlobals.flowCoord.size(); i++){
            Cellpath newPath = new Cellpath();
            allCellPaths.add(newPath);
        }


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

        if ( c >= NUM_CELLS || r >= NUM_CELLS ) {
            return true;
        }

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if(!timeStarted)
                {
                    PlayActivity.startTimer();
                    timeStarted = true;
                }

                // WORK IN PROGRESS, NEEDS FIXING!
                Coordinate tempCord = new Coordinate(c, r);
                //Finna hvort þetta sé upphafspunktur
                int found = mGlobals.flowCoord.indexOf(tempCord);

                if (found == -1) {

                    for (Cellpath i : allCellPaths) {
                        if (i.getCoordinates().contains(tempCord)) {
                            found = allCellPaths.indexOf(i);
                        }
                    }

                }


                    Cellpath tempPath = allCellPaths.get(found / 2);


                    if (tempPath.getCoordinates().contains(tempCord) || mGlobals.flowCoord.contains(tempCord)) {
                        if (mGlobals.flowCoord.contains(tempCord)) {
                            tempPath.reset();
                        } else {
                            tempPath.trim(tempCord);
                        }
                        m_paintPath.setColor(colorList.get(allCellPaths.indexOf(tempPath)));
                        colorMeTimbers = colorList.get(allCellPaths.indexOf(tempPath));
                        tempPath.append(tempCord);

                    }
                }


            else if (event.getAction() == MotionEvent.ACTION_MOVE) {

                Coordinate tempCord = new Coordinate(c , r);


                if(movementConflict(tempCord,colorMeTimbers) && !tooFar(colorMeTimbers)){

                    lineConflict(tempCord, colorMeTimbers);

                    if (!allCellPaths.get(colorList.indexOf(colorMeTimbers)).isEmpty()) {

                        List<Coordinate> coordinateList = allCellPaths.get(colorList.indexOf(colorMeTimbers)).getCoordinates();
                        Coordinate last = coordinateList.get(coordinateList.size() - 1);
                        if (areNeighbours(last.getCol(), last.getRow(), c, r)) {
                            allCellPaths.get(colorList.indexOf(colorMeTimbers)).append(tempCord);
                            invalidate();

                        }
                    }
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                PlayActivity.moveSound();
               // m_cellPath = new Cellpath();
                if(isWin()){
                    PlayActivity.stopTimer();
                    PlayActivity.winSound();

                    mGlobals.fa.openToWrite();
                    mGlobals.fa.updateFlowFinished(
                            Integer.parseInt(mGlobals.puzzlePack.get(mGlobals.selectedPuzzle).getName()),
                            mGlobals.puzzlePack.get(mGlobals.selectedPuzzle).getChallengeName(),
                            PlayActivity.getGameTime(),
                            true);

                    mGlobals.fa.close();

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
        for(Cellpath tempPath:allCellPaths){
            if(tempPath.getCoordinates().contains(co) && (allCellPaths.indexOf(tempPath) != colorList.indexOf(colorMeTimbers))){
                tempPath.conflict(co);
                break;
            }
        }
    }

    //Check what current color is and if the color code isn't the wrong one
    public boolean movementConflict(Coordinate co ,int colorMeTimbers)
    {
        //
        int index = colorList.indexOf(colorMeTimbers);

        if(mGlobals.flowCoord.indexOf(co) != index / 2){
            return true;
        }

        return false;
    }
    //Prevents us from going from a start point of a color,
    // and then to an endpoint and continuing through it.
    public boolean tooFar(int colorMeTimbers)
    {
        for(Cellpath cellpath: allCellPaths) {
            if(cellpath.getCoordinates().size() > 1) {
                Coordinate lastCord = cellpath.getCoordinates().get(cellpath.getCoordinates().size() - 1);
                if (colorMeTimbers == colorList.get(allCellPaths.indexOf(cellpath)) && cellpath.getCoordinates().size() > 1) {
                    if (mGlobals.flowCoord.contains(lastCord) && (mGlobals.flowCoord.indexOf(lastCord) / 2) == colorList.indexOf(colorMeTimbers)) {
                        return true;
                    }
                }
            }
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
        int totalSize = 0;
        for(Cellpath cellPath: allCellPaths) {
            totalSize = totalSize + cellPath.getCoordinates().size();
        }

        if(totalSize == (NUM_CELLS * NUM_CELLS)){
            return true;
        }

       return false;

    }
}
