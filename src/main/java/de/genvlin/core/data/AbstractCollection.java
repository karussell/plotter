/*
 * AbstractCollection.java
 *
 * Created on 18. March 2006, 18:14
 * This stands under Public domain
 */

package de.genvlin.core.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** A base class providing several useful methods for a vector and
 * for a pool. See {@link VectorInterface} and {@link Pool}.
 *
 * @author Peter Karich
 */
public abstract class AbstractCollection implements CollectionInterface {
    private ID id;
    private Set<CollectionListener> listeners;
    
    /**
     * This variable indicates that exactly one data entry was
     * removed.
     */
    static final public String REMOVE_DATA = "Remove Data";
    
    /**
     * This variable indicates that exactly one data entry was
     * added.
     */
    static final public String ADD_DATA = "Add Data";
    
    /**
     * This variable indicates that more than one data entry was
     * added.
     */
    static final public String ADD_SOME = "Add Some";
    
    /**
     * This variable indicates that more than one data entry was
     * removed.
     */
    static final public String REMOVE_SOME = "Remove Some";
    
    /**
     * This variable indicates that the "decoration" (title,
     * info, etc.) of this vector was changed.
     */
    static final public String CHANGE_DECORATION = "Change Decoration";
    
    /**
     * This variable indicates that more than one data entry
     * was changed.     
     */
    static final public String CHANGE_SOME = "Change Soem";    
    
    /**
     * This variable indicates that one data entry was changed.     
     */
    static final public String CHANGE_DATA = "Change Data";    
    
    public AbstractCollection(ID id) {
        setID(id);
    }
    
    private synchronized Set<CollectionListener> getListeners() {
        if(listeners == null) {
            listeners = new HashSet<CollectionListener>();
        }
        
        return listeners;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return MainPool.getDefault().clone(this);
    }
    
    @Override
    public String toString() {
        if(info !=null) return info;
        
        return getID().toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        } else if(obj instanceof AbstractCollection) {
            return getID().equals(((AbstractCollection)obj).getID());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 37 * hash + (this.title != null ? this.title.hashCode() : 0);
        hash = 37 * hash + (this.info != null ? this.info.hashCode() : 0);
        return hash;
    }
    
    public int compareTo(IDData o) {
        return getID().compareTo(o.getID());
    }
    
    public ID getID() {
        return id;
    }
    
    private void setID(ID id) {
        this.id = id;
    }
    
    public synchronized void addVectorListener(CollectionListener vl) {
        getListeners().add(vl);
    }
    
    public synchronized void removeVectorListener(CollectionListener vl) {
        getListeners().remove(vl);
    }
    
    /**
     * Use this method only if one or no elements of this
     * Collection are involved. Use {@link #fireEvent(String, int, int)}
     * instead, if you want to fire a "CHANGE_ALL" event or a range event.
     *
     * @param propName the property which changed.
     * @param id the id of the changed element in idDataSource.
     * @param from the start index of the changed elements in idDataSource.
     * @param to the end index of the changed elements in idDataSource.
     */
    protected void fireEvent(String propName, ID id) {
        fireEvent(new CollectionEvent(this, propName, id));
    }
    
    protected void fireEvent(CollectionEvent evt) {
        Set<CollectionListener> pcl = getListeners();
        
        Iterator<CollectionListener> iter = pcl.iterator();
        while(iter.hasNext()) {
            iter.next().vectorChanged(evt);
        }
    }
    
    public void update() {
        fireEvent(CHANGE_SOME, getID());
    }
    
    /**
     * Use this method only if more than one element of this
     * Collection are involved. Use {@link #fireEvent(String, ID)}
     * instead, if you want to fire a e.g. a "ADD_DATA" event
     *
     * @param propName the property which changed.
     * @param from the start index of the changed elements in this-idDataSource.
     * @param to the end index of the changed elements in this-idDataSource.
     */
    protected void fireEvent(String propName, int from, int to) {
        fireEvent(new CollectionEvent(this, propName, from, to));
    }
    
    private String title;
    private String info;
    
    public String getInfo() {
        return info;
    }
    
    public void setInfo(String info) {
        fireEvent(CHANGE_DECORATION, null);
        this.info = info;
        if(title==null) setInfo(info);
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        fireEvent(CHANGE_DECORATION, null);
        this.title = title;
        if(info==null) setInfo(title);
    }
}
