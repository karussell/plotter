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
import java.util.List;

/**
 * This class provides general access to various number vectors.
 * This means that this class is able to handle all classes
 * which extends the abstract Number class. {@link StringNumber} included.
 * But use <tt>DoubleVector</tt> if you only add double's.
 *
 * @author Peter Karich
 */
class DataVector extends AbstractCollection implements VectorInterface {
    
    private List<Number> al;
    private boolean maxIsInvalid = true;
    private boolean minIsInvalid = true;
    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;
    
    DataVector(ID id, Collection<? extends Number> coll) {
        super(id);
        
        if(coll != null) {
            al = new ArrayList<Number>(coll);
        } else {
            al = new ArrayList<Number>();
        }
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
        maxIsInvalid = false;
        minIsInvalid = false;
        min = Double.POSITIVE_INFINITY;
        max = Double.NEGATIVE_INFINITY;
        
        al.clear();
    }
    
    public boolean remove(int index) {
        Number val = al.remove(index);
        
        if(val == null) {
            return false;
        }
        
        double d = val.doubleValue();
        if(d == min) {
            minIsInvalid = true;
        } //no else if!
        if(d == max) {
            maxIsInvalid = true;
        }
        
        //TODO expensive:
        //fireEvent(AbstractCollection.REMOVE_DATA, index, index);
        return true;
    }
    
    public Iterator<Number> iterator() {
        return al.iterator();
    }
    
    //VectorInterface methods:
    
    public boolean addAll(Collection<? extends Number> coll) {
        int tmp = size();
        if(al.addAll(coll)) {
            minIsInvalid = true;
            maxIsInvalid = true;
            fireEvent(AbstractCollection.ADD_SOME, tmp, size());
            return true;
        } else
            return false;
    }
    
    public int size() {
        return al.size();
    }
    
    public boolean add(Number n) {
        boolean ret = al.add(n);
        
        if(n.doubleValue() < min) {
            min = n.doubleValue();
        }
        if(n.doubleValue() > max) {
            max = n.doubleValue();
        }
        
        return ret;
        //TODO expensive:
        //fireEvent(AbstractCollection.ADD_DATA, size(), size());
    }
    
    public boolean addDouble(double d) {
        return add(new Double(d));
        //TODO expensive:
        //fireEvent(AbstractCollection.ADD_DATA, size(), size());
    }
    
    public Number get(int i) {
        return al.get(i);
    }
    
    public Number set(int i, Number n) {
        double old = al.set(i, n).doubleValue();
        
        if(old == min) {
            minIsInvalid = true;
        } else {
            if(n.doubleValue() < min) min = n.doubleValue();
        }
        
        if(old == max) {
            maxIsInvalid = true;
        } else {
            if(n.doubleValue() > max) max = n.doubleValue();
        }
        
        return old;
        
        //TODO expensive:
        //fireEvent(AbstractCollection.ADD_DATA, i,i);
    }
    
    public void trimToSize(int size) {
        if(this.size()<size) {
            al.addAll(Arrays.asList(new Number[size - this.size()]));
        }
    }
    
    public Number getMin() {
        if(minIsInvalid) {
            int size = size();
            Number n;
            min = Double.POSITIVE_INFINITY;//get(0) will not work: we allow null values!!
            
            for(int i = 0; i < size; i++) {
                n = get(i);
                if(n != null && n.doubleValue() < min) {
                    min = n.doubleValue();
                }
            }
            
            minIsInvalid = false;
        }
        
        return min;
    }
    
    public Number getMax() {
        if(maxIsInvalid) {
            max = Double.NEGATIVE_INFINITY;
            int size = size();
            Number n;
            for(int i = 0; i < size; i++) {
                n = get(i);
                if(n != null && n.doubleValue() > max) {
                    max = n.doubleValue();
                }
            }
            
            maxIsInvalid = false;
        }
        
        return max;
    }
}