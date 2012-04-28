/*
 * XYDataInterface.java
 *
 * Created on 27. Juli 2007, 09:37
 * This stands under Public domain
 */

package de.genvlin.core.data;

import de.genvlin.gui.plot.CoordinateSystem;
import java.awt.Color;
import java.awt.Graphics;
import java.text.ParseException;
import java.util.Iterator;

/** 
 * This interface defines methods for XYData and XYDecorator.
 *
 * @author Peter Karich
 */
public interface XYDataInterface extends BoundedXYCollectionInterface {
    
    /**
     * Returns wether this collection will be visible in plotter.
     */
    boolean isHidden();
    
    /**
     * Sets if this collection will be visible in plotter.
     */
    void setHidden(boolean b);
    
    /**
     * This method draws the points. The shape depends on the dot-type.
     */
    void draw(Graphics g, CoordinateSystem coordSys);
    
    Color getColor();
    void setColor(Color color);
    
    void setProperty(String propertyName, Object propertyValue);
    
    void add(double x, double y, String object) throws ParseException;
    
    XYDataEntry get(int i);
    
    Iterator<XYDataEntry> iterator();
    
    /**
     * The XYData implementation will only plot objects with indicies
     * between specified startX (inclusive) and specified endX (exclusive).
     * This will be disabled if you specify startX == endX.
     * Useful if you want to see only parts of the plot or want to implement
     * slow motion effects for the plotting.
     */
    void setPlotRange(int startX, int endX);
}
