/*
 * genvlin project.
 * Copyright (C) 2005 - 2007 Peter Karich.
 *
 * The initial version for the genvlin plotter you will find here:
 * http://genvlin.berlios.de/
 * The current release you will find here:
 * http://nlo.wiki.sourceforge.net/
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
import java.util.Map;
import java.util.TreeMap;

/**
 * This class implements <tt>PoolInterface</tt> via a <tt>TreeMap</tt>.
 * (The ID's are the key's.)<br>
 * And provides methods to access via a known {@link ID} or a known index.
 * Useful for PlotPanel and TablePanel implementations.
 * @author Peter Karich
 */
public class Pool<T extends IDData> extends AbstractCollection implements PoolInterface<T> {
    
    private Map<ID, T> intern;
    
    /** 
     * To compare two poolentries we need a <tt>Comparator</tt>.
     * If this is <tt>null</tt> the natural order will be used!
     * namely: the <tt>Comparable</tt>interface.
     */
    private Comparator<? super ID> comparator = null;
    
    /** Constructs a datapool with specified id.
     */
    Pool(ID id) {
        super(id);
        comparator = getDefaultComparator();
    }
    
    private static Comparator<? super ID> defaultComparator;
    
    private Comparator<? super ID> getDefaultComparator() {
        if(defaultComparator == null) {
            defaultComparator = new Comparator<ID>() {
                public int compare(ID o1, ID o2) {
                    //It should be clear that we can compare via the ID's
                    return o1.compareTo(o2);
                }
                public boolean equals(Object obj) {
                    return obj == defaultComparator;
                }
            };
        }
        return defaultComparator;
    }
    
    protected Map<ID, T> getColl() {
        if(intern == null)
            intern = new TreeMap<ID, T>(comparator);//if comp == null -> natural order
        
        return intern;
    }
    
    /** 
     * This methods provides the children of this class a simply
     * use of this pool to add data. It is not possible to overwrite
     * an existing <tt>IDData</tt>.
     * @return specified data if this pool already contains it. And returns
     * null if adding was NOT succesfull.
     */
    public <S extends T> S add(S data){
        if(getColl().containsKey(data.getID())) {
            return data;
        }
        
        getColl().put(data.getID(), data);
        
        fireEvent(AbstractCollection.ADD_DATA, data.getID());
        return data;
    }
    
    //CollectionInterface:
    
    public boolean remove(int index) {
        Iterator<T> iter = iterator();
        T tmp;
        for(int i=0; iter.hasNext(); i++) {
            tmp = iter.next();//if not-> IllegalStateException
            if(i == index) {
                iter.remove();
                fireEvent(AbstractCollection.REMOVE_DATA, tmp.getID());
                return true;
            }
        }
        return false;
    }
    
    public Iterator<T> iterator() {
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
    
    public T get(Comparable id) {
        return getColl().get(id);
    }
    
    public T get(int index) {
        Iterator<T> iter = iterator();
        
        for(int i=0; iter.hasNext(); i++) {
            if(i == index) return iter.next();
            else iter.next();
        }
        
        return null;
    }
    
    public boolean remove(Comparable id) {
        T tmp = getColl().remove(id);
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
