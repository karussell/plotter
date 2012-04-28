/*
 * XYVectorInterface.java
 *
 * Created on 11. April 2006, 21:39
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

package de.genvlin.core.data;

import java.util.Iterator;

/**
 *
 * @author Peter Karich
 */
public interface XYVectorInterface extends BoundedXYCollectionInterface {
    
    /**
     * This method returns the current length of this <tt>XYVectorInterface</tt>.
     * If x and y-vector has different length's the minimal is returned.
     */
    public int size();
    
    /**
     * This operation is relative expensive! So cache the result!
     * NO check if concurrent modifactions was made!
     */
    public Iterator<java.awt.geom.Point2D.Double> iterator();
    
    /**
     * This method removes all values (x AND y) from this <tt>XYVectorInterface</tt>.
     */
    public void clear();
    
    /**
     * Removes the elements only if index is valid.
     */
    public boolean remove(int index);
    
    /** 
     * This method appends specified numbers to this <tt>XYVectorInterface</tt>.
     */
    public void add(Number xNum, Number yNum);
    
    /**
     * This method returns the Number at specified index from the x-vector.
     */
    public Number getX(int index);
    
    /**
     * This method returns the Number at specified index from the y-vector.
     */
    public Number getY(int index);
    
    public double getXDouble(int index);
    
    public double getYDouble(int index);        
}
