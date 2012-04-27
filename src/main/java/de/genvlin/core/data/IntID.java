/*
 * IntID.java
 *
 * Created on 17. März 2006, 21:05
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

/** This class provides an <tt>Integer</tt> implementation of ID.
 * @author Peter Karich
 */
public class IntID implements ID {
    
    private int i;
    
    /** Constructs a new ID.*/
    public IntID(int i) {
        this.i = i;
    }
    
    public int compareTo(Object o) {
        if(!(o instanceof IntID)) 
            return toString().compareTo(o.toString());
        
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
        return i== ((IntID)obj).i;
    }
}