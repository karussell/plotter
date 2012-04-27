/*
 * VectorRangeEvent.java
 *
 * Created on 19. März 2006, 23:23
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

/** This class better discribes the range of change, to improve performance.
 *
 * @author Peter Karich
 */
class VectorRangeEvent extends CollectionEvent
{
    //private int from, to;
   
    /** This variable indicates a <tt>CollectionEvent</tt> with better
     * range discription -> <tt>CollectionRangeEvent</tt>.
     */
    //static final public String RANGE = "range";
    
    /** Constructs a new VectorRangeEvent.
     *
     * @param idDataSource the source of this event.
     * @param propName the property which changed.
     * @param from the start index of the changed elements in idDataSource.
     * @param to the end index of the changed elements in idDataSource.
     */
    public VectorRangeEvent(IDData source, String propName, int from, int to) {
        super(source, propName, from, to);
/*        if(from > to) throw new UnsupportedOperationException(
                "it should be \"from <= to\"");
        this.from = from;
        this.to = to;
  */  }
    
    /** Returns the "From-index". A Change occurs between "From" and "To".
     *
    public int getFrom() {
        return from;
    }*/
    
    /** Returns the "To-index". A Change occurs between "From" and "To".
     *
    public int getTo() {
        return to;
    }*/
}