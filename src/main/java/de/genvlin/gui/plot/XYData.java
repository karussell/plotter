/*
 * XYPair.java
 *
 * Created on 20. September 2005, 17:20
 *
 * genvlin project.
 * Copyright (C) 2005, 2006 Peter Karich.
 *
 * This project is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this project; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * or look at http://www.gnu.org
 */

package de.genvlin.gui.plot;

import de.genvlin.core.data.AbstractCollection;
import de.genvlin.core.data.CollectionListener;
import de.genvlin.core.data.ID;
import de.genvlin.core.data.MainPool;
import de.genvlin.core.data.VectorInterface;
import de.genvlin.core.data.XYInterface;
import de.genvlin.core.plugin.Log;
import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Iterator;

/**
 * This class defines a xyvector for usages in plotpanel.
 * (It is a kind of wrapper around the old xydata-style, instead of xyvector)
 * @author Peter Karich
 */
public class XYData extends AbstractCollection
        implements XYInterface, Serializable {
    
    /**
     * specifies the type 'DOT' of how the point should be displayed
     */
    public static final int DOT = 0;
    
    /**
     * specifies the type 'CROSS' of how the point should be displayed
     */
    public static final int CROSS = 1;
    
    /**
     * contains the x points for the plot
     */
    //public ArrayList xData;//ArrayList<Double>
    
    /**
     * contains the y points for the plot
     */
    //public ArrayList yData;//ArrayList<Double>
    private XYInterface xyVector;
    private Color color;
    
    private int type = DOT;
    
    /**
     * specifies how big the dots should be
     */
    private int size = 2;
    
    /**
     * The name of the data collection
     */
    private String name;
    
    /**
     * This constructor inits the datas needed for plotting.
     * By default size was set to 4 pixel
     public XYData(double x[], double y[], int dotType, Color color, String name)
     {
     if(x.length != y.length) throw new InvalidParameterException(" x.length should be equal to y.length!");
     this.type = dotType;
     this.color = color;
     this.xData = x;
     this.yData = y;
     
     }
     */
    
    /*
     * This constructor inits the datas needed for plotting.
     * By default size was set to 4 pixel
     * @deprecated use XYData(VectorInterface, VectorInterface) instead.
     *
     public XYData(ArrayList x, ArrayList y, boolean createNew) {
     if(x.size() != y.size())
     Log.log("WARNING: x.size should be equal to y.size! in XYData!", false);
     this.type = CROSS;
     this.color = Color.DARK_GRAY;
     xData = new ArrayList(x.size());
     yData = new ArrayList(y.size());
     Double tmp;
     for(int i = 0; i < Math.min(x.size(), y.size()); i++) {
     try {
     if(createNew) {
     tmp = new Double(((Number)y.get(i)).doubleValue());//if exc. occur-> we dont add this and x!
     xData.add(new Double(((Number)x.get(i)).doubleValue()));
     yData.add(tmp);
     } else {
     tmp = (Number)y.get(i);//if cast exc occur-> we dont add this and x!
     yData.add((Number)x.get(i));
     yData.add(tmp);
     }
     } catch(Exception exc) {
     Log.log("Info: Double is needed in XYData constructor!", false);
     //Log.err(exc, false);
     }
     }
     
     if(false) {
     
     for(int i = 0; i < Math.min(xData.size(), yData.size()); i++) {
     Log.log("x "+((Number)xData.get(i)).doubleValue(), false);
     Log.log(" ;y "+((Number)yData.get(i)).doubleValue(), false);
     }
     }
     
     }*/
    
    /**
     * This constructor inits the datas needed for plotting.
     * By default size was set to 4 pixel
     */
    public XYData(VectorInterface x, VectorInterface y) {
        this((XYInterface)MainPool.getDefault().create(x,y));
        if(x.size() != y.size())
            Log.log("WARNING: x.size should be equal to y.size! in XYData!", false);
        
        /*Double tmp;
        for(int i = 0; i < Math.min(x.size(), y.size()); i++) {
            try {
                if(true) {
                    tmp = new Double(((Number)y.get(i)).doubleValue());//if exc. occur-> we dont add this and x!
                    x.add(new Double(((Number)x.get(i)).doubleValue()));
                    yData.add(tmp);
                } else {
                    tmp = (Number)y.get(i);//if cast exc occur-> we dont add this and x!
                    yData.add((Number)x.get(i));
                    yData.add(tmp);
                }
            } catch(Exception exc) {
                Log.err("Double is needed in XYData constructor!", false);
                Log.err(exc, false);
            }
        }*/
        
        /*if(false) {
         
            for(int i = 0; i < Math.min(xData.size(), yData.size()); i++) {
                Log.log("x "+((Number)xData.get(i)).doubleValue(), false);
                Log.log(" ;y "+((Number)yData.get(i)).doubleValue(), false);
            }
        }*/
        
    }
    
    public XYData(XYInterface xyVector) {
        super(xyVector.getID());
        this.xyVector = xyVector;
        setName(""+xyVector.getID());
        this.type = CROSS;
        this.color = Color.DARK_GRAY;
    }
    /**
     * This method sets the size of plottingshape.
     * By default size was set to 4 pixel
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    /** @return the name of the data collection
     */
    public String getName() {
        return name;
    }
    
    /** This method sets the name of the data collection
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
        
    /** This method sets the dot type of the data collection
     */
    public void setDotType(int type) {
        this.type = type;
    }
    
    
    
    /** This method draws the point. The shape depends on the dot-type.
     * todo: performance if we sort data, we do not need to check if the coord.
     * sys. contains the point.
     */
    public void draw(Graphics g, CoordinateSystem coordSys) {
        int x=0, y=0;
        double tmpx, tmpy;
        int oldX=0, oldY=0;
        boolean printLine = false;
        
        for(int i=0; i < xyVector.size(); i++) {
            g.setColor(color);
            
            try {
                tmpx = ((Number)xyVector.getX(i)).doubleValue();
                tmpy = ((Number)xyVector.getY(i)).doubleValue();
            }catch(Exception e) {
                printLine = false;
                continue;
            }
            
            x = coordSys.xToWin(tmpx);
            y = coordSys.yToWin(tmpy);

            if(printLine && i!=0) {
                g.drawLine(oldX, oldY, x, y);
            } else {
                printLine = true;
            }
            //always initialise the old values with the last ones to plot a line
            oldX = x;    oldY = y;
            
            if(!coordSys.contains(tmpx, tmpy)) continue;
            
            switch(type) {
                case DOT:
                    //g.drawArc(x, y, size, size, 0, 360);
                    g.fillArc(x, y, size, size, 0, 360);
                    break;
                    
                case CROSS:
                    g.drawLine(x-size, y, x+size, y);
                    g.drawLine(x, y-size, x, y+size);
                    break;
                default:
            }//switch
        }//for
    }//method-draw
    
    
    /** This method returns the color of dataset
     */
    public Color getColor() {
        return color;
    }
    
    /** This method sets the color of dataset
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    public ID getID() {
        return xyVector.getID();
    }
    
    public void addVectorListener(CollectionListener vl) {
        xyVector.addVectorListener(vl);
    }
    
    public void removeVectorListener(CollectionListener vl) {
        xyVector.removeVectorListener(vl);
    }
    
    public int size() {
        return xyVector.size();
    }
    
    public void clear() {
        xyVector.clear();
    }
    
    public Iterator iterator() {
        return xyVector.iterator();
    }
    
    public boolean remove(int index) {
        return xyVector.remove(index);
    }
    
    public String getInfo() {
        return xyVector.getInfo();
    }
    
    public String getTitle() {
        return xyVector.getTitle();
    }
    
    public void setInfo(String info) {
        xyVector.setInfo(info);
    }
    
    public void setTitle(String title) {
        xyVector.setTitle(title);
    }
    
    public int compareTo(Object o) {
        return xyVector.compareTo(o);
    }
    
    /*public VectorInterface getX() {
        return xyVector.getX();
    }
     
    public VectorInterface getY() {
        return xyVector.getY();
    }
     
    public void add(Number xNum, Number yNum) {
        xyVector.add(xNum, yNum);
    }*/
    
    public Number getX(int index) {
        return xyVector.getX(index);
    }
    
    public Number getY(int index) {
        return xyVector.getY(index);
    }
    
    /*
    public Point2D.Double get(int i) {
        return xyVector.get(i);
    }*/
    
    public double getXDouble(int index) {
        return xyVector.getXDouble(index);
    }
    
    public double getYDouble(int index) {
        return xyVector.getYDouble(index);
    }
    
    public double getMaxX() {
        return xyVector.getMaxX();
    }
    
    public double getMinX() {
        return xyVector.getMinX();
    }
    
    public double getMaxY() {
        return xyVector.getMaxY();
    }
    
    public double getMinY() {
        return xyVector.getMinY();
    }
}