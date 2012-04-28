/*
 * VectorRangeEvent.java
 *
 * Created on 19. March 2006, 23:23
 * This stands under Public domain
 */

package de.genvlin.core.data;

/** This class better discribes the range of change, to improve performance.
 *
 * @author Peter Karich
 */
class VectorRangeEvent extends CollectionEvent
{
    //private int from, to;
   
    /** This variable indicates a <tt>CollectionEvent</tt> with better
     * range discription -> <tt>CollectionRangeEvent</tt>.
     */
    //static final public String RANGE = "range";
    
    /** Constructs a new VectorRangeEvent.
     *
     * @param idDataSource the source of this event.
     * @param propName the property which changed.
     * @param from the start index of the changed elements in idDataSource.
     * @param to the end index of the changed elements in idDataSource.
     */
    public VectorRangeEvent(IDData source, String propName, int from, int to) {
        super(source, propName, from, to);
/*        if(from > to) throw new UnsupportedOperationException(
                "it should be \"from <= to\"");
        this.from = from;
        this.to = to;
  */  }
    
    /** Returns the "From-index". A Change occurs between "From" and "To".
     *
    public int getFrom() {
        return from;
    }*/
    
    /** Returns the "To-index". A Change occurs between "From" and "To".
     *
    public int getTo() {
        return to;
    }*/
}