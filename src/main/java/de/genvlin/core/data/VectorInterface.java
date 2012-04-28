/*
 * AbstractVector.java
 *
 * Created on 18. March 2006, 15:55
 * This stands under Public domain
 */

package de.genvlin.core.data;

/** This interface defines method to access a Vector of Number.
 * Known implementations are {@link DataVector} and extension
 * {@link DoubleVectorInterface}.
 *
 * @author Peter Karich
 */
public interface VectorInterface extends CollectionInterface {    
    
    /** This method adds a Number to the vector */
    public boolean add(Number o);
    
    /** This method adds a double to the vector */
    public boolean addDouble(double d);
    
    /** 
     * This method appends a {@link Collection} to this
     * vector.<br>
     * It returns true if successfully added, false otherwise.
     */
    public boolean addAll(java.util.Collection<? extends Number> coll);
    
    /** This method returns the i-th element in the vector */
    public Number get(int i);
    
    /** This method returns the i-th element in the vector */    
    public double getDouble(int i);
    
    /** 
     * This method sets the i-th element to the specified one.
     * It returns the old element.
     */
    public Number set(int i, Number n);
    
    public Number getMin();
    
    public Number getMax();
        
    /**
     * This method ensure that the set method will not fire NullPointer
     * Exceptions until the new size.
     * @param size the new size of the VectorInterface's implementation.
     */
    public void trimToSize(int size);
}
