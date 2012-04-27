/*
 * PluginSPI.java
 *
 * Created on 6. April 2006, 00:22
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

package de.genvlin.core.plugin;

/** Service Provider Interface (SPI) for the plugins in genvlin.
 * E.g. the JFreechart library offers the "plot" service for genvlin.
 * The SPI <b>forces</b> the plugins to provide these methods!
 *
 * @author Peter Karich
 */
public interface PluginSPI {
    
    /** This method returns the human readable name of this plugin.
     */
    public String getName();
    
    /** This method returns the action strings. E.g.:
     * <pre>
     * return new String[] = { "tablepanel/header", "tablepanel/tablegrid"};
     * </pre>
     */
    public String[] getActionContextReasons();
    
    /** This method is called from source (any component) to let the
     * sp create a popupmenu (a <tt>Component</tt> is more general!
     * {@link RequestEvent#getPopup()})
     * from a context sensitive action (mouseevent) in the source.
     * E.g.: The right click on GTablePanel can be on the header or on tablegrid
     * ... and the plotlibrary wants to add an menuitem to popupmenu if there
     * are at least two vector selected.
     */
    public void sendRequest(RequestEvent ri);
    
    //OLD
    /** This method returns the PopupMenu created by the specified MouseEvent.
     * Add ActionListener's to PopupMenuItem's if you need.<br>
     * TODO e.g. How to provide "construct popmenu if tableheader and
     * else let it be" ?? <br>Via <pre>Class[] getComponentClasses()</pre> ??
     */
    //public Component getPopup(MouseEvent me);
    //public Component getPopup(VectorInterface[] vi);
    
    /** This method returns the class, for which this plugin should be used.
     * This registers the plugin to specified class. <br>
     * TODO How to specify more than one class? -> Class[]? Is this useful?
     */
    //public Class getComponentClass();
    
    /** This method contructs a JPanel plot from the specified <tt>XYVector</tt>.
     */
    //public JPanel actionPerformed(XYVector xyVector);
    
}
