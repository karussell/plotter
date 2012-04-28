/*
 * ID.java
 *
 * Created on 17. March 2006, 15:59
 *
 * genvlin project.
 * This stands under Public domain
 */

package de.genvlin.core.data;

/** This interface defines the ID abilities and is "<tt>Comparable</tt>".
 * Known subclasses {@link StringID} and {@link IntID}. If you subclass from 
 * here be sure that all classes are compatible with each other! (-> compareTo/equals)
 * @author Peter Karich
 */
public interface ID extends Comparable<ID> {
}
