/*
 * XYData.java
 *
 * Created on 27. Juli 2007, 10:14
 *
 * This file is part of the nlo project.
 * Visit http://nlo.sourceforge.net/ for more information.
 * Copyright (C) 2007 Peter Karich.
 *
 * This project is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this project; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * or look at http://www.gnu.org
 */

package de.genvlin.core.data;

import de.genvlin.gui.plot.CoordinateSystem;
import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * This class defines a xyvector for usages in GPlotpanel.
 *
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
class XYData extends AbstractCollection implements XYDataInterface, Serializable {
    
    private static final int DOT = 0;
    private static final int CROSS = 1;
    private static final int CIRCLE = 2;
    
    private XYVectorInterface xyVector;
    private Color color;
    
    private int type = CROSS;
    
    /**
     * specifies how big the dots should be
     */
    private int size = 1;
    
    /**
     * The name of the data collection
     */
    private String name;
    
    /**
     * This variables indicates wether this data should be plotted if added to a
     * GPlotPanel.
     */
    private boolean hidden = false;
    
    /**
     * This variables indicates wether this data should be automatically rescaled
     * if added to a LivePlotPanel.
     */
    private boolean autoScale = true;
    
    private int startX = 0;
    private int endX = 0;
    
    /**
     * This constructor inits the datas needed for plotting.
     * By default size was set to 4 pixel
     */
    XYData(VectorInterface x, VectorInterface y, ID id) {
        this(MainPool.getDefault().createXYVector(x,y), id);
        if(x.size() != y.size()) {
            Logger.getLogger("de.genvlin.gui.plot").
                    warning("x.size should be equal to y.size! in XYData!");
        }
    }
    
    XYData(XYVectorInterface xyVector, ID id) {
        super(id);
        this.xyVector = xyVector;
        this.type = CROSS;
        this.color = Color.DARK_GRAY;
    }
    
    public boolean isHidden() {
        return hidden;
    }
    
    public void setHidden(boolean b) {
        hidden = b;
    }
    
    /**
     * {@inheritDoc}
     * TODO PERFORMANCE if we would sort the data, we do not need to check if the coord.
     * system contains the point.
     */
    public void draw(Graphics g, CoordinateSystem coordSys) {
        //WARNING: if you change sth. here, change it appropriate there: XYDecorator.draw 
        
        if(isHidden()) {
            return;
        }
        g.setColor(color);
        
        double tmpx, tmpy;
        int x, y;
        int oldX = 0, oldY = 0;
        int i = startX;
        boolean printLine = false;
        int vectorSize = Math.min(xyVector.size(), endX);
        
        //set to whole plot range
        if(startX == endX) {
            i = 0;
            vectorSize = xyVector.size();
        }
        
        for(; i < vectorSize ; i++) {
            try {
                tmpx = xyVector.getXDouble(i);
                tmpy = xyVector.getYDouble(i);
            } catch(Exception e) {
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
                case CIRCLE:
                    g.drawArc(x, y, size, size, 0, 360);
                    break;
                case DOT:
                    
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
    
    /**
     * This method returns the color of dataset
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * This method sets the color of dataset
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Supported properties:<br>
     * "dot.type"; Integer; default is 4 <br>
     * "dot.size"; The String's: "circle", "dot", "cross" are accepted;
     * default is "dot"<br>
     */
    public void setProperty(String propertyName, Object propertyValue) {
        if("dot.size".equalsIgnoreCase(propertyName)) {
            this.size = ((Number)propertyValue).intValue();
        } else if("dot.type".equalsIgnoreCase(propertyName)) {
            if("dot".equalsIgnoreCase(propertyValue.toString())) {
                this.type = DOT;
            } else if("cross".equalsIgnoreCase(propertyValue.toString())) {
                this.type = CROSS;
            } else if("circle".equalsIgnoreCase(propertyValue.toString())) {
                this.type = CIRCLE;
            } else {
                throw new IllegalArgumentException("Unsupported dot type:" + propertyValue);
            }
        } else {
            throw new IllegalArgumentException("Unsupported property name:" + propertyName);
        }
    }
    
    public void add(double x, double y, String object) throws ParseException {
        xyVector.add(x, y);
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
    
    public Iterator<XYDataEntry> iterator() {
        throw new UnsupportedOperationException("not yet implemented");
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
    
    public int compareTo(IDData o) {
        return xyVector.compareTo(o);
    }
    
    public Number getMaxX() {
        return xyVector.getMaxX();
    }
    
    public Number getMinX() {
        return xyVector.getMinX();
    }
    
    public Number getMaxY() {
        return xyVector.getMaxY();
    }
    
    public Number getMinY() {
        return xyVector.getMinY();
    }
    
    public XYDataEntry get(int i) {
        Number x = xyVector.getX(i);
        Number y = xyVector.getY(i);
        if(x == null || y == null) {
            return null;
        }
        return new XYDataEntry(x.doubleValue(), y.doubleValue(), null);
    }
    
    public void setPlotRange(int lowX, int hightX) {
        startX = lowX;
        endX = hightX;
    }
}
