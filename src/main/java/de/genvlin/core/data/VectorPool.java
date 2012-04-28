/*
 * VectorPool.java
 *
 * Created on 8. March 2006, 15:15
 * This stands under Public domain
 */

package de.genvlin.core.data;

/** 
 * This class provide a <tt>Pool</tt> of <tt>VectorInterface</tt>'s.
 *
 * @author Peter Karich
 */
public class VectorPool extends Pool<VectorInterface> {
    
    VectorPool(ID id) {
        super(id);
    }
    
    /** A "foreign" vector could be added to this pool. Foreign means:
     * This pool doesn't contain this vector! But it doesn't mean:
     * you can import vectors which aren't created by {@link MainPool}!!<br>
     * Use <pre> MainPool.import(MainPool pool) </pre> instead.
     */
    public <S extends VectorInterface> S add(S av) {
        return super.add(av);
    }
    
    /**
     * Use {@link #create(Class)} or {@link #add(VectorInterface)} instead!
     *
     public boolean add(Comparable com) {
     throw new UnsupportedOperationException("Please use create or add instead!");
     }*/
    
    
    /**
     * This method create's a new vector (<tt>VectorInterface</tt>) specified via
     * by the class argument and adds it - if possible (this should always work) -
     * to the pool.
     *
     * @return the added <tt>VectorInterface</tt>
     */
    public VectorInterface create(Class<VectorInterface> clazz) {
        
        try {
            return add(MainPool.getDefault().createVector(clazz));
        } catch(ClassCastException cce) {
            throw new UnsupportedOperationException("Argument class should be an " +
                    "instance of AbstractVector!");
        }
    }
}
