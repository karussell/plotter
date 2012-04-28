/*
 * StringID.java
 *
 * Created on 17. March 2006, 21:23
 *
 * genvlin project.
 * This stands under Public domain
 */

package de.genvlin.core.data;

/** This class provides a <tt>String</tt> implementation of <tt>ID</tt>.
 * @author Peter Karich
 */
public class StringID implements ID {
    
    private String id;
    
    public StringID(String id) {
        this.id = id;
    }
    
    public StringID(ID id) {
        this.id = id.toString();
    }
    
    public StringID(Number n) {
        this.id = n.toString();
    }
    
    public int compareTo(ID o) {
        return id.compareTo(o.toString());
    }
    
    public boolean equals(Object obj) {
        return id.equals(obj.toString());
    }
    
    public String toString() {
        return id;
    }
}
