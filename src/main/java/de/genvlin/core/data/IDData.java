/*
 * IDData.java
 *
 * Created on 8. March 2006, 23:15
 *
 * genvlin project.
 * This stands under Public domain
 */

package de.genvlin.core.data;

/** This interface makes it possible that all the classes which implements 
 * this interface could be compared and lookup by {@link MainPool}.
 * The objects should created in MainPool too!
 *
 * @author Peter Karich 
 */
public interface IDData extends Comparable<IDData>
{
    /** This method returns the identification.
     * @return id of this special dataobject */    
    public ID getID();
}
