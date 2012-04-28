/*
 * XYDataInterface.java
 *
 * Created on 27. Juli 2007, 09:37
 *
 * genvlin project.
 * Copyright (C) 2005 - 2007 Peter Karich.
 *
 * The initial version for the genvlin plotter you will find here:
 * http://genvlin.berlios.de/
 * The current release you will find here:
 * http://nlo.wiki.sourceforge.net/
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
import java.text.ParseException;
import java.util.Iterator;

/** 
 * This interface defines methods for XYData and XYDecorator.
 *
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
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
