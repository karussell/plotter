/*
 * StringNumber.java
 *
 * Created on 8. März 2006, 10:47
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

/** This class only extends Number and throws unsupported exceptoins for its
 * methods. We want to wrap around a string and don't want to write extra
 * MiscVector.add(String or Object) methods!
 *
 * @author Peter Karich
 */
class StringNumber extends Number {
    private String str;
    
    private StringNumber() {
    }
    
    /** Creates a new instance of StringNumber */
    public StringNumber(String s) {
        str = s;
    }
    
    public String toString() {
        return str;
    }
    static final private String OUTMSG = "This is not a number!";
    public double doubleValue() {
        throw new UnsupportedOperationException(OUTMSG);
//      return Double.NaN;
    }
    
    public float floatValue() {
        throw new UnsupportedOperationException(OUTMSG);
//        return Float.NaN;
    }
    
    public int intValue() {
        throw new UnsupportedOperationException(OUTMSG);
    }
    
    public long longValue() {
        throw new UnsupportedOperationException(OUTMSG);
    }
}
