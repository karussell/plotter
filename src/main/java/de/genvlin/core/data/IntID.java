/*
 * IntID.java
 *
 * Created on 17. March 2006, 21:05
 *
 * genvlin project.
 * This stands under Public domain
 */

package de.genvlin.core.data;

/** This class provides an <tt>Integer</tt> implementation of ID.
 * @author Peter Karich
 */
public class IntID implements ID {
    
    private int i;
    
    /** Constructs a new ID.*/
    public IntID(int i) {
        this.i = i;
    }
    
    public int compareTo(ID o) {
        if(!(o instanceof IntID)) {
            return toString().compareTo(o.toString());
        }
        
        //only for the special case:
        int tmp = ((IntID) o).i;
        if(i < tmp) return -1;
        if(i > tmp) return 1;
        else return 0;
    }
    
    public String toString() {
        return Integer.toString(i);
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof IntID)) 
            return toString().equals(obj.toString());
        
        //only for the special case:
        return i == ((IntID)obj).i;
    }
}