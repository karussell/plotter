/*
 * XYPool.java
 * This stands under Public domain
 *
 */

package de.genvlin.core.data;

/**
 * This class provide a <tt>Pool</tt> of <tt>XYInterface</tt>'s.
 *
 * @author Peter Karich
 */
class XYPool extends Pool<XYVectorInterface> 
        implements PoolInterface<XYVectorInterface> {
    
    XYPool(ID id) {
        super(id);
    }
    
    /**
     * This method will create a new <tt>XYInterface</tt>(created by MainPool)
     * and sets specified arguments as x and y respectivly.
     */
    public XYVectorInterface add(VectorInterface x, VectorInterface y) {
        XYVectorInterface xy = MainPool.getDefault().createXYVector(x,y);
        
        return super.add(xy);
    }
    
    /**
     * A "foreign" <tt>XYVectorInterface</tt> could be added to this pool. Foreign means:
     * This pool doesn't contain this <tt>XYInterface</tt> with its ID!
     * But you cannot import <tt>XYInterface</tt>'s which aren't created by
     * {@link MainPool}!!<br>
     * Use <pre> MainPool.import(MainPool pool) </pre> instead.
     */
    public <S extends XYVectorInterface> S add(S data){
        return super.add(data);
    }    
    
    /**
     * This method create's a new XYVectorInterface specified via the specified
     * classes and adds it if possible.
     *
     * @return the added and created <tt>XYInterface</tt>
     */
    public XYVectorInterface create(Class<VectorInterface> clazzX,
            Class<VectorInterface> classY) {
        return add(MainPool.getDefault().createXYVector(clazzX, classY));
    }
}