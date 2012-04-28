/*
 * HistogrammVector.java
 *
 * Created on 21. Mai 2007, 14:13
 * This stands under Public domain
 */

package de.genvlin.core.data;

/**
 * This class adds some statistical methods to DataVector.
 *
 * @author Peter Karich
 */
class HistogrammVector extends DataVector implements HistogrammInterface {
    
    /**
     * Creates a new instance of HistogrammVector
     */
    HistogrammVector(ID id) {
        super(id, null);
    }
        
    public double getRMSError() {
        int size = size();
        if(size == 0) {
            return 0;
        }
        double mean = getMean();
        
        double result = 0;
        double tmp;
        Number n;
        int noOfNonNullValues = size;
        
        for(int i = 0; i < size; i++) {
            n = get(i);
            if(n != null) {
                tmp = n.doubleValue() - mean;
                result += tmp * tmp;
            } else {
                noOfNonNullValues--;
            }
        }
        if(noOfNonNullValues > 0) {
            return Math.sqrt(result/noOfNonNullValues);
        } else {
            return 0;
        }
    }
        
    public double getRMS() {
        int size = size();
        if(size == 0) {
            return 0;
        }
        
        double result = 0;
        double tmp;
        int noOfNonNullValues = size;
        Number n;
        
        for(int i = 0; i < size; i++) {
            n = get(i);
            if(n != null) {
                tmp = n.doubleValue();
                result += tmp * tmp;
            } else {
                noOfNonNullValues--;
            }
        }
        
        if(noOfNonNullValues > 0) {
            return Math.sqrt(result/size);
        } else {
            return 0;
        }
    }
        
    public double getMean() {
        //TODO PERFORMANCE: we can sum + substract on every add/remove call
        int size = size();
        if(size == 0) {
            return 0;
        }
        
        double result = 0;
        Number n;
        int noOfNonNullValues = size;
        
        for(int i = 0; i < size; i++) {
            n = get(i);
            if(n != null) {
                result += n.doubleValue();
            } else {
                noOfNonNullValues --;
            }
        }
        
        if(noOfNonNullValues > 0) {
            return result / noOfNonNullValues;
        } else {
            return 0;
        }
    }
}