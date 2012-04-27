/*
 * AbstractPlotPluginImplOld.java
 *
 * Created on 28. April 2006, 23:00
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

import de.genvlin.core.data.ID;
import de.genvlin.core.data.MainPool;
import de.genvlin.core.data.VectorInterface;
import de.genvlin.core.data.XYPool;
import de.genvlin.gui.table.GTablePanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * This class simplifies the PlotPlugin implementation.
 *
 * @author Peter Karich
 */
abstract public class AbstractPlotPluginImplOld
        implements PlotPlugin, ActionListener {
    
    private String context[] = {PluginPool.TABLE, PluginPool.HEADER, PluginPool.SELECTED_COLS};
    private ID oldPlotPanel;
    private VectorInterface[] selected;
  
    abstract protected String getMenuName();
    
    public String[] getActionContextReasons() {
        return context;
    }
    
    public void actionPerformed(ActionEvent e) {
        /*XYPool oldPool = getOldPool();
        if(oldPool != null) {
            for(int i=1; i < selected.length; i++)
                oldPool.add(selected[0], selected[i]);
        } else {*/
            XYPool pool = (XYPool)MainPool.getDefault().create(XYPool.class);
            
            for(int i=1; i < selected.length; i++)
                pool.add(selected[0], selected[i]);
            plot(pool, pool.getID());
            setOldPlotID(pool.getID());
        //}
    }
    
    /** We know that only one source fits so we need not to check this via
     * if(getSource() instanceof ..).
     */
    public void sendRequest(RequestEvent ri) {
        //int i[]=((GTablePanel)ri.getSource()).getSelectedColumns()
        //get vi=new VectorInterface[] via forloop and getPopup(vi):
        
        //But plot context is directly supported by gtablemodel so:
        //return
        
        //if you do NOT check this here, possible losses could occur (ri.getObject==null),
        //because we listen to HEADER AND TABLE AND SELECTED_COLS
        if(ri.getActionContextReason()==PluginPool.SELECTED_COLS) {
            selected = (VectorInterface[])((GTablePanel)ri.getSource()).getSelectedVectors();
            JMenu menu = new JMenu(getName());
            JMenuItem item = new JMenuItem(getMenuName());
            menu.add(item);
            item.addActionListener(this);
            ((JPopupMenu)ri.getObject()).add(menu);
        }
    }
    
    private XYPool getOldPool() {
        if(oldPlotPanel != null)
            return ((XYPool)MainPool.getDefault().get(oldPlotPanel));
        return null;
    }
    
    private void setOldPlotID(ID id) {
        oldPlotPanel = id;
    }
    
    protected boolean showPanel(Component panel, String position) {
        return PluginPool.getDefault().getPlatform().showPanel(panel, position);
    }
}