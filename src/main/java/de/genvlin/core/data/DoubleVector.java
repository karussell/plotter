/*
 * DoubleVector.java
 *
 * Created on 8. März 2006, 10:15
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

import java.util.Collection;
import java.util.Iterator;

/** This class provides an easy to use, but performant array of doubles.
 * TODO: test this class and improve performance!!
 *
 * @author Peter Karich
 */
class DoubleVector extends AbstractCollection
        implements VectorInterface {
    
    private double[] dArray;
    
    /* The user accesses dArray from 0 until firstFreeIndex-1,
     * so dArray[firstFreeIndex] should be the first free element
     */
    private int firstFreeIndex;
    
    /** Size of blocks to allocate          */
    private int increaseSize;
    
    /** This constructor creates a new DoubleVector.
     *
     * @param vSize Size of block to allocate
     * @param incSize Size of block to allocate if increase is required*/
    DoubleVector(ID id, Collection coll, int vSize, int incSize) {
        super(id);
        
        if(vSize<=0) vSize=32;
        if(incSize<=0) incSize=32;
        
        increaseSize = incSize;
        
        dArray = new double[vSize];
        
        if(coll != null) addAll(coll);
    }
    
    /** This method returns the allocated double-array, be sure that
     * you know what you are doing if you use this class!<p>
     * @see     #toArray() - toArray() for a more secure method!
     */
    public final double[] getRawArray() {
        return dArray;
    }
    
    /** This method provides direct acces to the double array!
     * So please do only use if you know what you are doing.
     * @see #get(int)
     */
    public final double getDouble(int i) {
        return dArray[i];
    }
    
    /** This method provides direct acces to the double array!
     * So please do only use if you know what you are doing.
     */
    public final void setDouble(double d, int i) {
        dArray[i] = d;
        //expensive?:
        //fireEvent(AbstractCollection.CHANGE_DATA);
    }
    
    /** This method returns all doubles as an double array.
     */
    public final double[] toArray() {
        double[] retArray = new double[firstFreeIndex];
        System.arraycopy(dArray, 0, retArray, 0, firstFreeIndex);
        return retArray;
    }
    
    /**
     * This method adds a double to the vector.
     *
     * @param value double to add to the vector
     */
    public final boolean addDouble(double value) {
        
        if ((firstFreeIndex + 1) >= dArray.length) {
            double[] newMap = new double[dArray.length + increaseSize];
            
            System.arraycopy(dArray, 0, newMap, 0, firstFreeIndex);
            
            dArray = newMap;
        }
        
        dArray[firstFreeIndex] = value;
        firstFreeIndex++;
        
        ////TODO expensive:
        //fireEvent(AbstractCollection.ADD_DATA);
        return true;
    }
    
    /** This method insert specified value at specified index.
     */
    public final void insert(int index, double value) {
        //if dArray is full, we need extra space
        if ((firstFreeIndex + 1) > dArray.length) {
            double[] newMap = new double[dArray.length + increaseSize];
            
            System.arraycopy(dArray, 0, newMap, 0, firstFreeIndex);
            
            dArray = newMap;
        }
        
        //if we had to "insert" (not only add), we need to move some doubles
        if (index < firstFreeIndex) {
            System.arraycopy(dArray, index, dArray, index + 1, firstFreeIndex - index);
        }
        
        dArray[index] = value;
        firstFreeIndex++;
        
        //TODO expensive:
        //fireEvent(AbstractCollection.CHANGE_DATA, index, index);
    }
    
    /** This class adds a double array to this DoubleVector.
     * @see #addAll(Collection)
     */
    public final boolean addAll(double[] collArray) {
        if(collArray == null || collArray.length == 0) return false;
        
        //if dArray is full we need extry space
        if ((firstFreeIndex + 1 + collArray.length) > dArray.length) {
            double[] newMap = new double[dArray.length
                    + increaseSize + collArray.length];
            
            System.arraycopy(dArray, 0, newMap, 0, firstFreeIndex);
            
            dArray = newMap;
        }
        
        //we need this to implement later insertAll
        int at = firstFreeIndex;
        
        //copy the collection into free space of dArray
        System.arraycopy(collArray, 0, dArray, at, collArray.length);
        firstFreeIndex+= collArray.length;
        
        fireEvent(AbstractCollection.ADD_SOME,
                firstFreeIndex-collArray.length, firstFreeIndex);
        return true;
    }
    
    public final boolean addAll(Collection coll) {
        if(coll == null || coll.size()==0) return false;
        
        Object[] collArray;
        try {
            collArray = coll.toArray();
        } catch(Exception e) {
            return false;
        }
        if(collArray == null || collArray.length == 0) return false;
        
        //if dArray is full we need extry space
        if ((firstFreeIndex + 1 + collArray.length) > dArray.length) {
            double[] newMap = new double[dArray.length
                    + increaseSize + collArray.length];
            
            System.arraycopy(dArray, 0, newMap, 0, firstFreeIndex);
            
            dArray = newMap;
        }
        
        //we need this to implement later insertAll
        int at = firstFreeIndex;
        
        /*
        //if we had to "insert" (not only add) we need to move some data
        if (at < firstFreeIndex)
        {
            System.arraycopy(dArray, at, dArray, at + collArray.length,
                    firstFreeIndex - at);
        }*/
        
        //copy the collection into free space of dArray
        
        for(int i=0; i<collArray.length; i++) {
            dArray[at+i] = ((Number)collArray[i]).doubleValue();
        }
        
        firstFreeIndex+= collArray.length;
        fireEvent(AbstractCollection.ADD_SOME,
                firstFreeIndex-collArray.length, firstFreeIndex);
        return true;
    }
    
    //AbstractCollection:
    
    public final void clear() {
        firstFreeIndex = 0;
    }
    
    public final boolean add(Number n) {
        try {
            addDouble(n.doubleValue());
            //TODO expensive:
            //fireEvent(AbstractCollection.ADD_DATA);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public final int size() {
        return firstFreeIndex;
    }
    
    public boolean remove(int index) {
        throw new UnsupportedOperationException("Not yet implemented!");
        //fireEvent(AbstractCollection.REMOVE_DATA);
    }
    
    public final Number get(int i) {
        if(i<0)
            throw new IndexOutOfBoundsException("Index has to be positive!");
        
        else if(i>=size())
            throw new IndexOutOfBoundsException("Index has to be smaller as .size()!");
        
        return new Double(getDouble(i));
    }
    
    public Number set(int i, Number n) {
        Number old = get(i);
        
        dArray[i] = n.doubleValue();
        //TODO expensive:
        //fireEvent(AbstractCollection.CHANGE_DATA, i,i);
        return old;
    }
    
    public Iterator iterator() {
        throw new UnsupportedOperationException("Not yet implemented!");
    }
    
    public void trimToSize(int size) {
        if(size>=0) {
            if(size > dArray.length) {
                double[] newMap = new double[size+increaseSize];
                System.arraycopy(dArray, 0, newMap, 0, firstFreeIndex);
                firstFreeIndex = size;
                dArray = newMap;
            } else if(size > firstFreeIndex && size < dArray.length) {
                firstFreeIndex = size;
            }
        }
    }
}