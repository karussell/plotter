/*
 * ButtonMenuItem.java
 *
 * Created on 4. Dezember 2005, 23:19
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

package de.genvlin.gui.util;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 *
 * @author Peter Karich
 */
public class ButtonMenuItem extends JPanel implements ActionListener {
    JButton button;
    JPopupMenu popMenu;
    JTextField textField;
    
    /**
     * Creates a new instance of ButtonMenuItem
     */
    public ButtonMenuItem( String text, ActionListener al, final JPopupMenu popMenu) {
        this.popMenu = popMenu;
        setLayout(new FlowLayout());
        button = new JButton(text);
        button.setActionCommand(text);
        button.addActionListener(al);
        button.addActionListener(this);
        add(button);
        textField = new JTextField(10);
        
        textField.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_ENTER, 0), "check");
        textField.getActionMap().put("check", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                button.doClick();
            }
        });
        
        add(textField);
        //for(int i=0; i<comp.length; i++)        {            add(comp[i]);        }
        
        if(popMenu != null) {
            popMenu.add(this);
        }
    }
    
    public JTextField getTextField() {
        return textField;
    }
    
    public void actionPerformed(ActionEvent e) {
        if(popMenu != null) {
            if(e.getActionCommand().equals(button.getActionCommand())) {
                popMenu.setVisible(false);
            }
        }
    }
}
