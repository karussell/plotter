/*
 * CoordinateSystem.java
 *
 * Created on 11. September 2005, 18:18
 *
 */
package de.genvlin.gui.plot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * This class make the handling with the coordinate system easier.
 *
 * Imagine you have a special coordinate in mind(the math),
 * but the program wants to display it.(the win)
 * So we calculate scale factors etc.
 */
public class CoordinateSystem implements Serializable {
    
    /**
     * scaling between imagined and win coordinates
     */
    private double xScale, yScale;
    
    /**
     * now we need a math-offset, the origin will plotted relative to the
     * right-bottom window point. (Warning: Axis.x/yOffset is the window offset!)
     */
    private double xOff, yOff;
    
    
    private Color colorBackground;
    private Color color;
    
    private Axis xAxis, yAxis;
    
    private Rectangle winBounds;
    
    /**
     * normal Precision for scaler numbers. <br>
     * superPrec. for mouseposition
     */
    private NumberFormat numberFormatPosition;
    private NumberFormat numberFormatAxes;
    
    /**
     * Imagine you have a special coordinate in mind(the math), and the
     * program wants to display it.(the win)
     * So we calculate scale factors etc.
     *
     * @param xOrigin xdistance from left down windowpoint
     * @param yOrigin ydistance from left down windowpoint
     * @param xScale x-scaling scaling between math and win coordinates, xWin/xScale = xMath
     * @param yScale y-scaling scaling between math and win coordinates, yWin/yScale = yMath
     */
    public CoordinateSystem(int xOrigin, int yOrigin, double xScale, double yScale) {
        if(xScale==0) throw new InvalidParameterException("xScale should not be zero");
        if(yScale==0) throw new InvalidParameterException("yScale should not be zero");
        
        this.xOff = xOrigin;
        this.yOff = yOrigin;
        this.xScale = xScale;
        this.yScale = yScale;
        
        xAxis = new Axis(Axis.X, 10, this);
        yAxis = new Axis(Axis.Y, 5, this);
        setAxesNumberFormat(Locale.US, 3);
        setPositionNumberFormat(Locale.US, 7);
    }
    
    /**
     * This method sets the NumberFormat for the coordinate
     * system. Important for displaying numbers etc.
     */
    public void setAxesNumberFormat(Locale locale, int maximumFractionDigits) {
        numberFormatAxes = NumberFormat.getInstance(locale);
        numberFormatAxes.setMaximumFractionDigits(maximumFractionDigits);
    }
    
    /**
     * This method sets the NumberFormat for the coordinate
     * system. Important for displaying numbers etc.
     */
    public void setPositionNumberFormat(Locale locale, int maximumFractionDigits) {
        numberFormatPosition = NumberFormat.getInstance(locale);
        numberFormatPosition.setMaximumFractionDigits(maximumFractionDigits);
    }
    
    /**
     * sets the actualized window bounds (from plotpanel)
     */
    public void setWinBounds(Rectangle wBounds) {
        winBounds = wBounds;
    }
    
    public Axis getYAxis() {
        return yAxis;
    }
    
    public Axis getXAxis() {
        return xAxis;
    }
    
    public void setXAxis(Axis xAxis) {
        this.xAxis = xAxis;
    }
    
    public void setYAxis(Axis yAxis) {
        this.yAxis = yAxis;
    }
    
    public final double getXScale() {
        return xScale;
    }
    
    public final double getYScale() {
        return yScale;
    }
    
    /**
     * @return winBounds.width
     */
    public final int winWidth() {
        return winBounds.width;
    }
    
    /**
     * @return winBounds.height
     */
    public final int winHeight() {
        return winBounds.height;
    }
    
    /**
     * transform xMath value to xWin value
     */
    public final int xToWin(double x) {
        return (int)Math.round(xOff+x*getXScale());
    }
    
    /**
     * transform yMath value to yWin value
     */
    public final int yToWin(double y) {
        return (int)Math.round(winHeight()-(yOff+y*getYScale()));
    }
    
    /**
     * This method transforms a xWin-value to a xMath-value
     */
    public final double xToMath(int x) {
        return (double)((x-xOff)) / getXScale();
    }
    
    /**
     * This method transforms a yWin-value to a yMath-value
     */
    public final double yToMath(int y) {
        return (double)((winHeight()-y-yOff))/getYScale();
    }
    
    /**
     * This method checkes wether the point in math coord is visible on screen
     */
    public final boolean contains(double x, double y) {
        double x1,y1,x2,y2,tmp;
        
        x1 = xToMath(0 + Axis.xOffset);
        x2 = xToMath(winWidth() - Axis.xOffset);
        y1 = yToMath(0 + Axis.yOffset);
        y2 = yToMath(winHeight() - Axis.yOffset);
        
        //if the bounds wont be in the correct order
        if(x2<x1) { tmp=x1; x1=x2; x2=tmp;}
        if(y2<y1) { tmp=y1; y1=y2; y2=tmp;}
        
        return x <= x2 && x >= x1 && y <=y2 && y >= y1;
    }
    
    /**
     * This method returns the mathmetical bounds of this coordinate system.
     * @see contains
     */
    public final Rectangle2D.Double getMathBounds() {
        double x1,y1,x2,y2,tmp;
        
        x1 = xToMath(0 + Axis.xOffset);
        x2 = xToMath(winWidth() - Axis.xOffset);
        y1 = yToMath(0 + Axis.yOffset);
        y2 = yToMath(winHeight() - Axis.yOffset);
        
        //if the bounds wont be in the correct order
        if(x2<x1) { tmp=x1; x1=x2; x2=tmp;}
        if(y2<y1) { tmp=y1; y1=y2; y2=tmp;}
        return new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
    }
    
    /**
     * This method translates(moves) the offset of the mathcoordinatesys.
     */
    public void translate(long xTranslate, long yTranslate) {
        xOff -= xTranslate;
        yOff -= yTranslate;
    }
    
    double x1, y1;
    
    /**
     * This method zooms in an rectangle. defined by start point in
     * setZoomStart method before, and endpoint with params (newX, newY).
     */
    public void zoom(int newX, int newY) {
        double tmp, x2=xToMath(newX), y2=yToMath(newY);
        
        if(x2<x1) { tmp=x1; x1=x2; x2=tmp;}
        if(y2<y1) { tmp=y1; y1=y2; y2=tmp;}
        
        automaticScale(x1,x2, y1,y2);
    }
    
    /**
     * set the center for zooming
     */
    public void setZoomStart(Point p) {
        x1 = xToMath(p.x);
        y1 = yToMath(p.y);
    }
    
    /**
     * This method scale the axes in that manner that the params
     * "xyMinMax" (math coord sys) will define new coordinate bounds.
     */
    public void automaticScale(double xMin, double xMax, double yMin, double yMax) {
        if(xMax - xMin == 0) {
            xScale = 1;
        } else {
            xScale = (winWidth() - 150) / (xMax - xMin);
        }
        
        if(yMax - yMin == 0) {
            yScale = 1;
        } else {
            yScale = (winHeight() - 150) / (yMax - yMin);
        }
        
        //translate the origin
        xOff = -xMin *xScale + Axis.xOffset + 5;
        yOff = -yMin *yScale + Axis.yOffset + 5;
        
        /*todo:scaler.automaticScale();
         * to determine how many scalers we need (on y axis):
          log10(xMax);
         */
    }
    
    /**
     * This method draw the axes, the position is independend from
     * math coord sys.
     */
    public void drawAxes(Graphics g) {
        g.setColor(color);
        xAxis.draw(g);
        yAxis.draw(g);
    }
    
    /**
     * This method sets the background color of coordinate system
     */
    public void setBackground(Color c) {
        colorBackground = c;
    }
    
    /**
     * This method sets the textcolor of coordinate system
     */
    public void setColor(Color c) {
        color = c;
    }        
    
    /**
     * This method draws the current mouseposition.
     * todo: plot this in an extra jlabel
     */
    public void drawMousePosition(Graphics g, Point p) {
        //g.setColor(colorBackground);
        //g.fillRect(0,0,  50,30);
        g.setColor(color);
        //TODO LATER this is not font independent
        g.drawString("x = "+ numberFormatPosition.format(xToMath(p.x)), 0, 10);
        g.drawString("y = "+ numberFormatPosition.format(yToMath(p.y)), 0, 30);
    }
    
    public String format(double value) {
        return numberFormatAxes.format(value);
    }
    
    /**
     * This method draws the distance between two points.
     * todo: plot this in an extra jlabel
     */
    public void drawMouseDistance(Graphics g, Point start, Point end) {
        g.setColor(color);
        //todo: this is not font independent
        double startX = xToMath(start.x), startY = yToMath(start.y);
        double endX = xToMath(end.x), endY = yToMath(end.y);
        double x=endX-startX, y=endY-startY;
        double d = Math.sqrt(y*y+x*x);
        g.drawString("xDistance = "+ numberFormatPosition.format(x), 150,10);
        g.drawString("yDistance = "+ numberFormatPosition.format(y), 150,30);
        g.drawString( "Distance = "+ numberFormatPosition.format(d), 350,10);
    }
}