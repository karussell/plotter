/*
 * CollectionEvent.java
 *
 * Created on 17. March 2006, 15:33
 * This stands under Public domain
 */

package de.genvlin.core.data;

/** This is the basis for {@link CollectionListener}
 *
 * @author Peter Karich
 */
public class CollectionEvent extends java.util.EventObject {
    
    private String propName;
    
    /** Constructs a new CollectionEvent.
     *
     * @param idDataSource the source of this event.
     * @param propName the property which changed.
     * @param from the start index of the changed elements in idDataSource.
     * @param to the end index of the changed elements in idDataSource.
     * @param id the id of the changed element in idDataSource.     
     */
    private CollectionEvent(IDData idDataSource, String propName, ID id, int from, int to) {
        super(idDataSource);
        this.propName = propName;
        //ADDED
        this.id = id;
        if(from > to) throw new UnsupportedOperationException(
                "it should be \"from <= to\"");
        this.from = from;
        this.to = to;
    }
    
    /** Constructs a new CollectionEvent.
     *
     * @param idDataSource the source of this event.
     * @param propName the property which changed.
     * @param id the id of the changed element in idDataSource.
     */
    public CollectionEvent(IDData idDataSource, String propName, ID id) {
        this(idDataSource, propName, id, -1, -1);
    }
    
    /** Constructs a new CollectionEvent.
     *
     * @param idDataSource the source of this event.
     * @param propName the property which changed.
     * @param from the start index of the changed elements in idDataSource.
     * @param to the end index of the changed elements in idDataSource.
     */
    public CollectionEvent(IDData source, String propName, int from, int to) {
        this(source, propName, null, from, to);
    }
    
    /**
     * This method returns the property of this event.
     * @return the property of this event
     */
    public String getProperty() {
        return propName;
    }
    
    /**
     * You can cast the returned object to <tt>IDData</tt>.
     */
    public Object getSource() {
        return super.getSource();
    }
    
    //ADDED:
    private int from, to;
    private ID id;
    
    /** Returns the "From-index". A Change occurs between "From" and "To".
     */
    public int getFrom() {
        return from;
    }
    
    /** Returns the "To-index". A Change occurs between "From" and "To".
     */
    public int getTo() {
        return to;
    }
    
    /**
     * This method returns the id of the changed element in IDData (=source).
     * To get the id of IDData itself use:
     * <pre>((IDData)event.getSource()).getID()</pre>
     */
    public ID getID() {
        return id;
    }

    public String toString() {
        return "CollectionEvent:[id="+id+"; from="+from+"; to="+to
                +"; source="+source+"; property="+propName+";]";
    }
}
