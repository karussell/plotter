/*
 * PoolInterface.java
 *
 * Created on 18. März 2006, 20:14
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

/** This interface extends CollectionInterface<tt> to provide pool
 * management on <tt>IDData</tt>. We can't add these methods to <tt>CollectionInterface</tt>,
 * because we "have" <tt>AbstractVector</tt>! So we "outsourced"
 * all "<tt>ID</tt> and <tt>IDData</tt>"-relevant things to <tt>PoolInterface</tt>
 *
 * @author Peter Karich
 */
public interface PoolInterface extends CollectionInterface {
    
    /** This method adds the specified comparable to the pool.
     */
    //public boolean add(Comparable id);
    
    /** This method removes the entry with specified <tt>ID</tt> or 
     * <tt>Comparable</tt>.
     */
    public boolean remove(Comparable id);
    
    /** This method returns the IDData from the specified <tt>ID</tt> or 
     * <tt>Comparable</tt>.
     * Should be equivalent to sth. like 
     * <pre>boolean contains(Comparable comp)</pre>. If <tt>null</tt>
     * is returned this indicates "not found".
     */
    public IDData get(Comparable id);
    
    /** This method returns the i-th element in the pool. And the
     * value <tt>null</tt> if not found or if index is not valid.
     * The order comes from the ID's (Comparable).
     */
    public IDData get(int index);
    
    /** This method returns the index of <tt>IDData</tt> 
     * with specifid <tt>ID</tt> or <tt>Comparable</tt>. 
     * And returns less than 0, if not found.
     */
    public int indexOf(Comparable id);
}
