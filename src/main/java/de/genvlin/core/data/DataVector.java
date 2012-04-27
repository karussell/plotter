/*
 * genvlin project.
 * Copyright (C) 2005, 2006 Peter Karich.
 *
 * This project is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This  is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this ; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * or look at http://www.gnu.org
 *
 */

package de.genvlin.core.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/** This class provides general access to various number vectors.
 * This means that this class is able to handle all classes
 * which extends the abstract Number class. {@link StringNumber} included.
 * But use DoubleVector instead of using "pure" <tt>Double</tt>'s.
 * @author Peter Karich
 */
class DataVector extends AbstractCollection
        implements VectorInterface {
    private ArrayList al = null;
    
    DataVector(ID id, Collection coll) {
        super(id);
        if(coll != null) addAll(coll);
    }
    
    private ArrayList getAL() {
        if(al == null) al = new ArrayList();
        
        return al;
    }
    
    /** This method returns the entry (a double) with specified index.
     * It returns a Double.NaN if Number at specified index isn't a
     * <tt>Double</tt>.
     */
    public double getDouble(int i) {
        return get(i).doubleValue();
    }
    
    //CollectionInterface methods:
    public void clear() {
        fireEvent(AbstractCollection.REMOVE_SOME, 0, size());
        getAL().clear();
    }
    
    public boolean remove(int index) {
        if(getAL().remove(index) == null) return false;
        
        //TODO expensive:
        //fireEvent(AbstractCollection.REMOVE_DATA, index, index);
        return true;
    }
    
    public Iterator iterator() {
        return getAL().iterator();
    }
    
    //VectorInterface methods:
    
    public boolean addAll(Collection coll) {
        int tmp = size();
        if(getAL().addAll(coll)) {
            fireEvent(AbstractCollection.ADD_SOME, tmp, size());
            return true;
        } else
            return false;
    }
    
    public int size() {
        return getAL().size();
    }
    
    public boolean add(Number n) {
        return getAL().add(n);
        //TODO expensive:
        //fireEvent(AbstractCollection.ADD_DATA, size(), size());
    }
    
    public boolean addDouble(double d) {
        return getAL().add(new Double(d));
        //TODO expensive:
        //fireEvent(AbstractCollection.ADD_DATA, size(), size());
    }
    
    public Number get(int i) {
        return (Number)getAL().get(i);
    }
    
    public Number set(int i, Number n) {
        return (Number)getAL().set(i,n);
        //TODO expensive:
        //fireEvent(AbstractCollection.ADD_DATA, i,i);
    }
    
    public void trimToSize(int size) {
        if(this.size()<size) {
            //getAL().ensureCapacity(size);
            getAL().addAll(                    
                    Arrays.asList(new Object[size - this.size()]));
            //getAL().trimToSize();
        }
    }
}