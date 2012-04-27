/*
 * GTable.java
 *
 * Created on 26. April 2006, 14:26
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

package de.genvlin.gui.table;

import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Peter Karich
 */
public class GTable extends JTable {
    
    /** Creates a new instance of GTable */
    public GTable(TableModel tm) {
        super(tm);
    }
    
    /** Creates a new instance of GTable */
    public GTable() {
        super();
    }
    class Saver {
        int index;
        int width;
    }
    Saver s;

    /**
     * To let our table remember which width and oder it has before
     * craeting new tables from model!
     * TODO: prevent users from: 1. editing 2. cancel editing 
     * => width changes will lost (but only sometimes??)
     * ..strange..
     */
    public void createDefaultColumnsFromModel() {
        TableModel m = getModel();
        if (m != null) {
            // Remove any current columns and save values.
            TableColumnModel cm = getColumnModel();
            ArrayList al = new ArrayList();
            for(int viewIndex = 0; cm.getColumnCount() > 0; viewIndex++) {
                TableColumn c = cm.getColumn(0);
                s = new Saver();
                s.width = c.getWidth();
                s.index = c.getModelIndex();
                al.add(s);
                cm.removeColumn(c);
            }
            
            // Create new columns from the data model info
            for (int i = 0; i < m.getColumnCount(); i++) {
                TableColumn newColumn = new TableColumn(i);
                if(al.size()>i) {
                    Saver tmp = ((Saver)al.get(i));
                    newColumn.setModelIndex(tmp.index);
                    newColumn.setPreferredWidth(tmp.width);
                }
                addColumn(newColumn);
            }
        }
    }
}
