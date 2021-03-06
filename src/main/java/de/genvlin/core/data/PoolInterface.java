/*
 * PoolInterface.java
 *
 * Created on 18. March 2006, 20:14
 * This stands under Public domain
 */

package de.genvlin.core.data;

/** This interface extends CollectionInterface<tt> to provide pool
 * management on <tt>IDData</tt>. We can't add these methods to <tt>CollectionInterface</tt>,
 * because we "have" <tt>AbstractVector</tt>! So we "outsourced"
 * all "<tt>ID</tt> and <tt>IDData</tt>"-relevant things to <tt>PoolInterface</tt>
 *
 * @author Peter Karich
 */
public interface PoolInterface<T extends IDData> extends CollectionInterface {
    
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
    public T get(Comparable id);
    
    /** This method returns the i-th element in the pool. And the
     * value <tt>null</tt> if not found or if index is not valid.
     * The order comes from the ID's (Comparable).
     */
    public T get(int index);
    
    /** This method returns the index of <tt>IDData</tt> 
     * with specifid <tt>ID</tt> or <tt>Comparable</tt>. 
     * And returns less than 0, if not found.
     */
    public int indexOf(Comparable id);
}
