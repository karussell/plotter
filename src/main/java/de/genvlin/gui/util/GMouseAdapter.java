/*
 * GMouseAdapter.java
 *
 * Created on 9. April 2006, 23:31
 * This stands under Public domain
 */

package de.genvlin.gui.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/** 
 * Uses the gMouse* method instead of mouse*! To provide popup
 * abilities override showPopup.
 * @see #showPopup
 *
 * @author Peter Karich
 */
public class GMouseAdapter implements MouseListener {
    
    public GMouseAdapter() {
    }

    /** This method will be called after a popup action detection.
     */
    public void showPopup(MouseEvent me){}

    
    final public void mousePressed(MouseEvent e) {
        if(e.isPopupTrigger()) showPopup(e);
        gMousePressed(e);
    }

    final public void mouseReleased(MouseEvent e) {
        if(e.isPopupTrigger()) showPopup(e);
        gMouseReleased(e);
    }

    final public void mouseEntered(MouseEvent e) {
        gMouseEntered(e);
    }

    final public void mouseExited(MouseEvent e) {
        gMouseExited(e);
    }
    
    final public void mouseClicked(MouseEvent e) {
        gMouseClicked(e);
    }
    
    public void gMousePressed(MouseEvent e) {
    }

    public void gMouseClicked(MouseEvent e) {
    }

    public void gMouseReleased(MouseEvent e) {
    }

    public void gMouseEntered(MouseEvent e) {
    }

    public void gMouseExited(MouseEvent e) {
    }
}
