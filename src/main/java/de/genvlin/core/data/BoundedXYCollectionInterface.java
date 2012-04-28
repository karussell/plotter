/*
 * BoundedXYCollectionInterface.java
 *
 * Created on 27. Juli 2007, 10:17
 * This stands under Public domain
 */

package de.genvlin.core.data;

/**
 *
 * @author Peter Karich
 */
public interface BoundedXYCollectionInterface extends CollectionInterface {
    
    Number getMaxX();
    Number getMinX();
    Number getMaxY();
    Number getMinY();
}
