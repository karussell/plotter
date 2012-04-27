/*
 * AbstractCollection.java
 *
 * Created on 18. März 2006, 18:14
 *
 * genvlin project.
 * Copyright (C) 2005, 2006 Peter Karich.
 *
 * This project is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this project; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * or look at http://www.gnu.org
 */

package de.genvlin.core.data;

import java.util.HashSet;
import java.util.Iterator;

/** A base class providing several useful methods for a vector and
 * for a pool. See {@link VectorInterface} and {@link Pool}.
 *
 * @author Peter Karich
 */
public abstract class AbstractCollection
        implements CollectionInterface{
    private ID id;
    private HashSet listeners;
    
    /**
     * This variable indicates that exactly one data entry was
     * removed.
     */
    static final public String REMOVE_DATA = "Remove Data";
    
    /**
     * This variable indicates that exactly one data entry was
     * added.
     */
    static final public String ADD_DATA = "Add Data";
    
    /**
     * This variable indicates that more than one data entry was
     * added.
     */
    static final public String ADD_SOME = "Add Some";
    
    /**
     * This variable indicates that more than one data entry was
     * removed.
     */
    static final public String REMOVE_SOME = "Remove Some";
    
    /**
     * This variable indicates that the "decoration" (title,
     * info, etc.) of this vector was changed.
     */
    static final public String CHANGE_DECORATION = "Change Decoration";
    
    /**
     * This variable indicates that more than one data entry
     * was changed.     
     */
    static final public String CHANGE_SOME = "Change Soem";    
    
    /**
     * This variable indicates that one data entry was changed.     
     */
    static final public String CHANGE_DATA = "Change Data";    
    
    public AbstractCollection(ID id) {
        setID(id);
    }
    
    private HashSet getListeners() {
        if(listeners == null)
            listeners = new HashSet();
        
        return listeners;
    }
    
    protected Object clone() throws CloneNotSupportedException {
        return MainPool.getDefault().clone(this);
    }
    
    public String toString() {
        if(info !=null) return info;
        
        return getID().toString();
    }
    
    public boolean equals(Object obj) {
        return getID().equals(((AbstractCollection)obj).getID());
    }
    
    public int compareTo(Object o) {
        return getID().compareTo(((IDData)o).getID());
    }
    
    public ID getID() {
        return id;
    }
    
    private void setID(ID id) {
        this.id = id;
    }
    
    public void addVectorListener(CollectionListener vl) {
        getListeners().add(vl);
    }
    
    public void removeVectorListener(CollectionListener vl) {
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
        HashSet pcl = getListeners();
        
        Iterator iter = pcl.iterator();
        while(iter.hasNext()) {
            ((CollectionListener)iter.next()).vectorChanged(evt);
        }
    }
    
    public void update() {
        fireEvent(CHANGE_SOME, getID());
    }
    
    /**
     * Use this method only if more than one element of this
     * Collection are involved. Use {@link #fireEvent(String, ID)}
     * instead, if you want to fire a e.g. a "DECORATOR" event
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
