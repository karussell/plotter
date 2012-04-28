/*
 * CollectionListener.java
 *
 * Created on 17. March 2006, 15:33
 * This stands under Public domain
 */

package de.genvlin.core.data;

/** Through this listener interface a collection (or other classes)
 * is able to recieve change events.
 *
 * @author Peter Karich
 */
public interface CollectionListener extends java.util.EventListener {
    /**
     * This fine grain notification tells listeners the exact range
     * of indicies that changed. @see javax.swing.table.TableModelListener
     */
    public void vectorChanged(CollectionEvent ve);
}
