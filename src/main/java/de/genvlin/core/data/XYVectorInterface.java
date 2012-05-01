/*
 * XYVectorInterface.java
 *
 * Created on 11. April 2006, 21:39
 * This stands under Public domain
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
    
    VectorInterface getX();
    
    VectorInterface getY();
}
