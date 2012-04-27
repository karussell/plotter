/*
 * CollectionInterface.java
 *
 * Created on 18. März 2006, 20:12
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

import java.util.Iterator;

/** This interface should be the base of all objects in genvlin.<br>
 * If not, be sure you implement {@link IDData}!
 *
 * @author Peter Karich
 */
public interface CollectionInterface extends IDData {    
    
    /** This method returns the no of <tt>data</tt> in this collection.
     *
     * @return length of the collection 
     */
    public int size();
    
    /** This method return an <tt>Iterator</tt> on all the entries. */
    public Iterator iterator();
    
    /** Returns a short desription of this collection.     */
    public String getInfo();
    
    /** Returns the title of this collection.     */
    public String getTitle();
    
    /** Sets a short desription.     */
    public void setInfo(String info);
    
    /** Sets the title.     */
    public void setTitle(String title);
    
    /* The following method are only useful for collection where
     * we could write on. They are not useful for the Function interface.
     * So how could we split into separate interface's??
     */
    
    /** This method adds the specified listener to this collection
     * to recieve change events.
     */
    public void addVectorListener(CollectionListener vl);
    
    /** This method removes the specified listener from this collection
     * if available.
     */
    public void removeVectorListener(CollectionListener vl);    
    
    /** This method removes all <tt>IDData</tt> from this collection. */
    public void clear();    
    
    /** This method removes the specified entry. */
    public boolean remove(int index);
    
    /**
     * This method will fire a global update change event to all the listeners.
     */
    public void update();
}