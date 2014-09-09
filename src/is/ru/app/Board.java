package is.ru.app;

/**
 * Created by olafurn on 9.9.2014.
 */

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
public class Board extends View {

    //Want to change for varying sizes
    private final int NUM_CELLS = 5;
    private int m_cellWidth;
    private int m_cellHeight;

    private Rect m_rect = new Rect();
    private Paint m_paintGrid = new Paint();
    private Paint m_paintPath = new Paint();
    private Path m_path = new Path();

    private Cellpath m_cellPath = new Cellpath();

    private int xToCol(int x) {
        return (x - getPaddingLeft()) / m_cellWidth;
    }

    private int yToRow(int y)
    {
        return (y - getPaddingTop()) / m_cellHeight;
    }

    private int colToX(int col){
        return col * m_cellWidth + getPaddingLeft();
    }

    private int rowToY(int row){
        return row * m_cellHeight + getPaddingTop();
    }

    public Board(Context context, AttributeSet attrbs) {
        super(context, attrbs);

        //Set grid color
        m_paintGrid.setStyle(Paint.Style.STROKE);
        m_paintGrid.setColor(Color.GRAY);

        //Set line attributes
        m_paintPath.setStyle(Paint.Style.STROKE);
        m_paintPath.setColor(Color.GREEN);
        m_paintPath.setStrokeWidth(32);
        m_paintPath.setStrokeCap(Paint.Cap.ROUND);
        m_paintPath.setStrokeJoin(Paint.Join.ROUND);
        m_paintPath.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int size = Math.min(width,height);

        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Paint grid
        for (int r = 0; r < NUM_CELLS; ++r) {
            for (int c = 0; c < NUM_CELLS; ++c) {
                int x = colToX(c);
                int y = rowToY(r);

                m_rect.set(x, y, x + m_cellWidth, y + m_cellHeight);
                canvas.drawRect(m_rect, m_paintGrid);
            }
        }

        m_path.reset();
        if (!m_cellPath.isEmpty()) {
            List<Coordinate> colist = m_cellPath.getCoordinates();
            Coordinate co = colist.get(0);

            m_path.moveTo(colToX(co.getCol()) + m_cellWidth / 2,
                            rowToY(co.getRow()) + m_cellHeight / 2);
            for(int i = 1; i<colist.size(); ++i)
            {
                co = colist.get(i);
                m_path.lineTo(colToX(co.getCol()) + m_cellWidth / 2,
                        rowToY(co.getRow()) + m_cellHeight / 2);
            }
        }
        canvas.drawPath(m_path, m_paintPath);
    }

    private boolean areNeighbours(int c1, int r1, int c2, int r2){
        return Math.abs(c1-c2) + Math.abs(r1-r2) == 1;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int c = xToCol(x);
        int r = yToRow(y);

        if(c >= NUM_CELLS || r >= NUM_CELLS) {
            return true;
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            if(!m_cellPath.isEmpty()) {
                List<Coordinate> coordinateList = m_cellPath.getCoordinates();
                Coordinate last = coordinateList.get(coordinateList.size()-1);

                if(areNeighbours(last.getCol(), last.getRow(), c, r)) {
                    m_cellPath.append(new Coordinate(c, r));
                    invalidate();
                }
            }
        }
    }
}
