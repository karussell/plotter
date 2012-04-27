package de.genvlin.core.plugin;

/**
 * This interface defines a set of methods, which are useful for
 * Platform implementation to create action context sensitive pop
 * up menus. The source (any component in the application) fires
 * such a RequestEvent to the service provider (=sp) - a plugin.
 */
public interface RequestEvent {
    
    /** This method returns any object. And the sp should know, 
     * which instance the returned object is.
     */
    public Object getObject();
    
    /** One Event has only one action context. (One plugin could be 
     * registered to <b>many</b>!)
     */
    public String getActionContextReason();
            
    /** This method returns the source of this <tt>RequestEvent</tt>.
     */
    public Object getSource();
    
    //OLD:
    /** This method returns the popup menu already created from source. So 
     * the sp can add/remove(!) components to/from it.
     */
    //public JPopupMenu getPopup();
    
    
    /** This method returns the MouseEvent, which was the reason for
     * this RequestEvent.
     */
    //public MouseEvent getMouseEvent();
}    