/*
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
 *
 */

package de.genvlin.core.data;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * This class implements <tt>PoolInterface</tt> via a <tt>TreeMap</tt>.
 * (The ID's are the key's.)<br>
 * And provides methods to access via a known {@link ID} or a known index.
 * Useful for PlotPanel and TablePanel implementations.
 * @author Peter Karich
 */
class Pool extends AbstractCollection
        implements PoolInterface {
    
    private TreeMap intern = null;
    
    /** To compare two poolentries we need a <tt>Comparator</tt>.
     * If this is <tt>null</tt> the natural order will be used!
     * namely: the <tt>Comparable</tt>interface.
     */
    private Comparator comparator = null;
    
    /** Constructs a datapool with specified id.     
     */
    Pool(ID id) {
        super(id);
        comparator = getDefaultComparator();
    }
    
    static Comparator defaultComparator;
    private Comparator getDefaultComparator() {
        if(defaultComparator == null) {
            defaultComparator = new Comparator() {
                public int compare(Object o1, Object o2) {
                    //It should be clear that we can compare via the ID's
                    return ((Comparable)o1).compareTo(o2);
                }
                public boolean equals(Object obj) {
                    return obj == defaultComparator;
                }
            };
        }
        return defaultComparator;
    }
    
    protected TreeMap getColl() {
        if(intern == null)
            intern = new TreeMap(comparator);//if comp == null -> natural order
        
        return intern;
    }
    
    /** This methods provides the children of this class a simply
     * use of this pool to add data. It is not possible to overwrite
     * an existing <tt>IDData</tt>.
     * @return specified data if this pool already contains it. And returns
     * null if adding was succesfull.
     */
    protected IDData add(IDData data){
        if(getColl().containsKey(data.getID()))
            return data;
        //IDData tmp = (IDData)
                getColl().put(data.getID(), data);
        
        fireEvent(AbstractCollection.ADD_DATA, data.getID());
        return null;
    }
    
    //CollectionInterface:
    
    public boolean remove(int index) {
        Iterator iter = iterator();
        IDData tmp;
        for(int i=0; iter.hasNext(); i++) {
            tmp=(IDData)iter.next();//if not-> IllegalStateException
            if(i == index) {
                iter.remove();
                fireEvent(AbstractCollection.REMOVE_DATA, tmp.getID());        
                return true;
            }
        }
        return false;
    }
    
    public Iterator iterator() {
        return getColl().values().iterator();
    }
    
    public int size() {
        return getColl().size();
    }
    
    public void clear() {
        fireEvent(AbstractCollection.REMOVE_DATA, 0, size());        
        getColl().clear();
    }
    
    //PoolInterface:
    
    public IDData get(Comparable id) {
        return (IDData)getColl().get(id);
    }
    
    public IDData get(int index) {
        Iterator iter = iterator();
        
        for(int i=0; iter.hasNext(); i++) {
            if(i == index) return (IDData)iter.next();
            else iter.next();
        }
        
        return null;
    }
    
    public boolean remove(Comparable id) {
        IDData tmp = (IDData)getColl().remove(id);
        if(tmp == null) 
            return false;
        
        fireEvent(AbstractCollection.REMOVE_DATA, tmp.getID());
        return true;
    }
    
    public int indexOf(Comparable id) {
        Iterator iter = iterator();
        
        for(int i=0; iter.hasNext(); i++) {
            if(((VectorInterface)get(i)).getID().equals(id))
                return i;
        }
        return -1;
    }
}
