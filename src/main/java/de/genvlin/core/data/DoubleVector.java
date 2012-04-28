/*
 * DoubleVector.java
 *
 * Created on 8. March 2006, 10:15
 * This stands under Public domain
 */

package de.genvlin.core.data;

import java.util.Collection;
import java.util.Iterator;

/**
 * This class provides an easy to use, but performant array of doubles.
 * TODO LATER: write test cases for this class, implement missing methods
 * and improve performance!!
 *
 * @author Peter Karich
 */
class DoubleVector extends AbstractCollection implements VectorInterface {
    
    private boolean maxIsInvalid = true;
    private boolean minIsInvalid = true;
    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;
    
    private double[] dArray;
    
    /*
     * The user accesses dArray from 0 until firstFreeIndex-1,
     * so dArray[firstFreeIndex] should be the first free element
     */
    private int firstFreeIndex;
    
    /** Size of blocks to allocate          */
    private int increaseSize;
    
    /**
     * This constructor creates a new DoubleVector.
     *
     * @param vSize Size of block to allocate
     * @param incSize Size of block to allocate if increase is required
     */
    DoubleVector(ID id, Collection<? extends Number> coll, int vSize, int incSize) {
        super(id);
        
        if(vSize<=0) vSize=32;
        if(incSize<=0) incSize=32;
        
        increaseSize = incSize;
        
        dArray = new double[vSize];
        
        if(coll != null) {
            addAll(coll);
        }
    }
    
    /**
     * This method returns the allocated double-array, be sure that
     * you know what you are doing if you use this class!<br>
     *
     * @see     #toArray() - toArray() for a more secure method!
     */
    public final double[] getRawArray() {
        return dArray;
    }
    
    /**
     * This method provides direct acces to the double array!
     * So please do only use if you know what you are doing.
     *
     * @see #get(int)
     */
    public final double getDouble(int i) {
        return dArray[i];
    }
    
    /**
     * This method provides direct acces to the double array!
     * So please do only use if you know what you are doing.
     */
    public final double setDouble(int i, double d) {
        
        double old = dArray[i];
        
        if(old == min) {
            minIsInvalid = true;
        } else {
            if(d < min) min = d;            
        }
        
        if(old == max) {
            maxIsInvalid = true;
        } else {
            if(d > max) max = d;            
        }
        
        dArray[i] = d;
        return d;
        //expensive?:
        //fireEvent(AbstractCollection.CHANGE_DATA);
    }
    
    /**
     * This method returns all doubles as an double array.
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
        
        if(value < min) {
            min = value;
        }
        if(value > max) {
            max = value;
        }
        
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
    
    /**
     * This method insert specified value at specified index.
     */
    public final void insert(int index, double value) {
        //TODO PERFORMANCE: performance draw back for getMin/getMax
        maxIsInvalid = true;
        minIsInvalid = true;
        
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
    
    /**
     * This class adds a double array to this DoubleVector.
     *
     * @see #addAll(Collection)
     */
    public final boolean addAll(double[] collArray) {
        if(collArray == null || collArray.length == 0) return false;
        
        minIsInvalid = true;
        maxIsInvalid = true;
        
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
        
        minIsInvalid = true;
        maxIsInvalid = true;
        
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
        maxIsInvalid = false;
        minIsInvalid = false;
        min = Double.POSITIVE_INFINITY;
        max = Double.NEGATIVE_INFINITY;
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
        
        /*
         don't forget:
         
         double d = "doubleValue()";
        if(d == min) {
            minIsInvalid = true;
        } //no else if!
        if(d == max) {
            maxIsInvalid = true;
        }
         */
    }
    
    public final Number get(int i) {
        if(i<0)
            throw new IndexOutOfBoundsException("Index has to be positive!");
        
        else if(i>=size())
            throw new IndexOutOfBoundsException("Index has to be smaller as .size()!");
        
        return new Double(getDouble(i));
    }
    
    public Number set(int i, Number n) {
        return setDouble(i, n.doubleValue());
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
    
    public Number getMin() {
        if(minIsInvalid) {
            int size = size();            
            min = Double.POSITIVE_INFINITY;
            
            for(int i = 0; i < size; i++) {                
                if(dArray[i] < min) {
                    min = dArray[i];
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
            
            for(int i = 0; i < size; i++) {                
                if(dArray[i] > max) {
                    max = dArray[i];
                }
            }
            
            maxIsInvalid = false;
        }
        
        return max;
    }
}