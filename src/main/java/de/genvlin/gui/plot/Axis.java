/*
 * Axis.java
 *
 * Created on 25. September 2005, 13:41
 *
 */

package de.genvlin.gui.plot;

import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

/**
 * This class contains necessary infos about the axes.
 * Offsetinfos:  screen + text + origin.
 * Axisinfos: their scalers: no. + size.
 * @author Peter Karich
 */
public class Axis implements Serializable {
    public static final int //TOP =3, DOWN = 4, LEFT = 5, RIGHT = 6,
            // POS_X = 7, NEG_X = 8, POS_Y = 9, NEG_Y = 10;
            X = 0, Y = 1;
    
    private int type;
    private CoordinateSystem cSys;
    
    /** offset of origin= startpoint of axis in Window coord.
     * (Warning: CoordinateSystem.x/yOff is the math offset!)
     */
    static int xOffset = 50;
    static int yOffset = 50;
    
    /** offset for description of x axis in Window coord
     */
    private int x_xTextOff = -5;
    private int x_yTextOff = 40;
    
    /** offset for description of y axis in Window coord
     */
    private int y_xTextOff = -40;
    private int y_yTextOff = 5;
    
    /** how many scalers are on this axis:
     */
    private int noOfScalers;
    
    /** The default size in window coordinates
     */
    private int scalerSize = 8;
    
    /** Creates a new instance of Axis
     * @param type needs one of POS_X NEG_X, POS_Y or NEG_Y
     * @param noOfScalers how many scaler shall be on this axis
     * @param cSys on which coordinate system we scale the axis
     */
    public Axis(int type, int noOfScalers, CoordinateSystem cSys) {
        this.type = type;
        this.cSys = cSys;
        this.noOfScalers = noOfScalers;
    }
    
    /**
     * @return one of Axis.X or Axis.Y
     */
    public int getType() {
        return type;
    }
    
    /** This method returns the x offset of the origin
     */
    public int getXOffset() {
        return xOffset;
    }
    
    /** This method returns the y offset of the origin
     */
    public int getYOffset() {
        return yOffset;
    }
    
    /**
     * return the startpoint of axis in window coord
     */
    public Point getStartPoint() {
        switch(type) {
            case X:  return new Point(0+ getXOffset(),cSys.winHeight()-getYOffset());
            //case Y:
            default: return new Point(0+ getXOffset(),cSys.winHeight()-getYOffset());
        }
    }
    
    /** return the endpoint of axis in window coord
     */
    public Point getEndPoint() {
        switch(type) {
            case X:  return new Point(cSys.winWidth() -getXOffset(), cSys.winHeight()-getYOffset());
            default: return new Point(0+getXOffset(), 0+getYOffset());//NEG_Y
        }
    }
    
    /**  This method returns a beauty rounded (less fraction digist) distance from 'end' to 'start'.
     * We can round distance, if we know how many fraction digits are important <p>
     * e.g.: start=4321 and end=4311 and no=5 => dist/no = 10/5 = 2 => now we know all digits until 10^0 are important. No we can round distance!<p>
     * e.g.2: start=0.442 and end=0.023 => dist/no =(ca) 0.419/5 = 0.084 => now we know all digits until 10^-2 are important.<p>
     *
     * @param no contains the number of scalers which should be on the axis.
     */
    protected double getRounded(double old, double closestNumber) {
        //number of important digist
        long s = (long)Math.floor(Math.log(closestNumber)/Math.log(10));
        //(floor returns the largest double to argument, but not greater than the arg.)
        
        double fact = Math.pow(10, s);
        
        return Math.round(old / fact) * fact;
        
    }
    /**
     * This method draws the axis line and its scalers in respect to
     * the actual properties of CoordinateSystem
     */
    public void draw(Graphics g) {
        Point s=getStartPoint(), e= getEndPoint();
        
        drawScalers(g, s, e);
        g.drawLine(s.x, s.y, e.x, e.y);
    }
    
    /** This method draw the scalers + its text<p>
     * If you want to actualize : call automaticScale(newcoordinatebounds).
     */
    public void drawScalers(Graphics g, Point start, Point end) {
        double tmp, roundedDist;
        
        switch(getType()) {
            case Axis.X:
                
                double xMath = cSys.xToMath(start.x);
                double xEndMath = cSys.xToMath(end.x);
                
                //make sure that start<end =>direction is not important for scalers
                if(xMath > xEndMath) {
                    tmp = xEndMath; xEndMath = xMath; xMath = tmp;
                }
                int xWin;
                
                //distance from start to end point
                roundedDist = Math.abs(xEndMath - xMath)/(noOfScalers + 1);
                
                //we can round distance, if we know how many fraction digits are important
                //e.g.: start=4321 and end=4311 and no=5 => dist/no = 10/5 = 2 => now we know all digits until 10^0 are important. No we can round distance!
                //e.g.2: start=0.442 and end=0.023 => dist/no =(ca) 0.419/5 = 0.084 => now we know all digits until 10^-2 are important.
                roundedDist = getRounded(roundedDist, roundedDist);
                xMath = getRounded(xMath, roundedDist);
                
                for(;  xMath < xEndMath;  xMath += roundedDist) {
                    xWin = cSys.xToWin(xMath);
                    g.drawLine(xWin, start.y - scalerSize/2,
                            xWin, start.y + scalerSize/2);
                    
                    g.drawString(cSys.format(xMath), xWin+x_xTextOff, start.y+x_yTextOff);
                    
                }
                break;
                
            default:
                
                double yMath = cSys.yToMath(start.y);
                double yEndMath = cSys.yToMath(end.y);
                if(yMath > yEndMath) {
                    tmp = yEndMath; yEndMath = yMath; yMath = tmp;
                }
                int yWin;
                //distance from start to end point
                roundedDist = Math.abs(yEndMath - yMath)/(noOfScalers + 1);
                
                roundedDist = getRounded(roundedDist, roundedDist);
                yMath = getRounded(yMath, roundedDist);
                
                for(;  yMath < yEndMath;  yMath += roundedDist) {
                    yWin = cSys.yToWin(yMath);
                    g.drawLine(start.x-scalerSize/2, yWin,
                            start.x+scalerSize/2, yWin);
                    
                    g.drawString(cSys.format(yMath), start.x+y_xTextOff, yWin+y_yTextOff);
                }
                break;
        }
    }
    
    /** This method sets the height(for x) resp. width(for y) of scalers
     * in pixels => the unit is "window coordinates"
     */
    public void setScalerSize(int pixels) {
        scalerSize = pixels;
    }
}
