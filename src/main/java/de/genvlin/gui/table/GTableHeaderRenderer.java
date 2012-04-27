/*
 * GTableHeaderRenderer.java
 *
 * Created on 24. Oktober 2005, 11:38
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

import de.genvlin.core.plugin.Log;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/** This class defines a new two-line column header
 * @author Peter Karich
 */
public class GTableHeaderRenderer extends JPanel implements TableCellRenderer {
    
    private JLabel charLabel;
    private JLabel stringLabel;
    private JComboBox combo;
    private TableColumnModel linkedTableColumnModel;
    
    /** Creates a new instance of GTableCellRenderer
     * @param linkedTable links the behavior (widthchanges) to another
     * header. <tt>null</tt> accepted.
     */
    public GTableHeaderRenderer(TableColumnModel linkedTable) {
        this.linkedTableColumnModel = linkedTable;
        charLabel = new JLabel("", SwingConstants.CENTER);
        stringLabel = new JLabel("", SwingConstants.CENTER);
        setLayout(new GridLayout(0,1));
        add(charLabel);
        //add(stringLabel);
        
        charLabel.setBorder(lBorder);
        setBorder(lBorder);
    }
    
    static LineBorder lBorder = new LineBorder(new Color(0.1f, 0.1f, 0.1f), 1);
    String split[];
    
    /** This method defines a new double line header for genvlin's GTableModel.
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        charLabel.setFont(table.getFont());
        stringLabel.setFont(table.getFont());
        if(value == null || value.toString() == null) {
            Log.log("TableHeaderRenderer: value == " + value, false);            
        } else {
            split = value.toString().split("\n",2);
            //in header cell renderer row is always -1
            if(split.length == 2)
                charLabel.setText(split[0]+":"+split[1]);
            else
                charLabel.setText(value.toString());
           /* 
            charLabel.setText(split[0]);
            if(split.length==2)
                stringLabel.setText(split[1]);
            else stringLabel.setText("---------");
            */
        }
        
        if(linkedTableColumnModel !=null && row < 0) {
            //resizing:
            int width1 = table.getColumnModel().getColumn(column).getWidth();//"table" should be the GTablePanel.fixedTable
            TableColumn tc2 = linkedTableColumnModel.getColumn(column);
            tc2.setPreferredWidth(width1);
            
            //moving:
            //see GTablePanel.FixedTableColumnModelListener.columnMoved
        }
        return this;
    }
}
