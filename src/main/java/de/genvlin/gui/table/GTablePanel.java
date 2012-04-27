/*
 * GTablePanel.java
 *
 * Created on 14. September 2005, 20:42
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

import de.genvlin.core.data.IDData;
import de.genvlin.core.data.VectorInterface;
import de.genvlin.core.data.VectorPool;
import de.genvlin.core.data.XYPool;
import de.genvlin.core.plugin.Log;
import de.genvlin.core.plugin.RequestEvent;
import de.genvlin.gui.util.ButtonMenuItem;
import de.genvlin.core.plugin.PluginPool;
import de.genvlin.core.plugin.Platform;
import de.genvlin.gui.util.Detection;
import de.genvlin.gui.util.GMouseAdapter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


/** Requirements:
 * should store order of GUI-columns, tablemodel, separators and file where
 * this panel was loaded from. Then we could implements a refresh + import
 * with preview!
 * should provides a findPlotPanelsFrom(IDData)
 */

/** This class supports an (hopefully) easy access to the data saved in tabels<p>
 * The tables will arranged in TabPanels and there is an access to the data
 * through the beanshell console<p>
 * GTablePanel can be divided in six areas:<br>
 * <pre>
 * tableheader | invisible tableheader
 * ---------------------------------------
 * fixedgrid   | invis. fixed scrollpane
 * ---------------------------------------
 * tablegrid   | else(=invis. scrollpane)
 *</pre>
 *
 * @author Peter Karich
 */
public class GTablePanel implements ClipboardOwner {
    
    static final long serialVersionUID = 432233240;
    
    private ArrayList allActions;
    private PopupSupportImpl popupSupport;
    
    private JTable table;
    private JTable fixedTable;
    
    //private GPlotPanel plotPanel;
    /**
     * This variable holds all XYVector's which we want to plot.
     */
    private XYPool linkedPlotPool;
    
    public void setPoolForPlotting(XYPool linkedPlotPool) {
        this.linkedPlotPool = linkedPlotPool;
    }
    
    private JScrollPane scrollPane;
    private JPanel mainPanel;
    
    /** This variable is necessary so that the user can specify how many cols and rows */
    //private JTextField newSizeColumn;
    
    //--------- UNDO -------------
    transient Clipboard clip;//transient so we do not save in project
    
    /** contains the overpasted values*/
    private StringBuffer oldValues;
    
    /** the special col-indicies for oldValues */
    private int setCols[];
    
    /** the special row-indicies for oldValues */
    private int setRows[];
    
    
    public GTablePanel(VectorPool pool) {
        GTableModel model = new GTableModel(pool);
        table = new GTable(model);
        initTable();
    }
    
    public GTablePanel(GTableModel model) {
        table = new GTable(model);
        initTable();
    }
    
    public GTablePanel() {
        table = new GTable(new GTableModel());
        initTable();
    }
    
    public GTablePanel(int rows, int columns) {
        //(tableModel, new GColumnModel()) does not work??
        table = new GTable(new GTableModel(rows, columns));
        initTable();
    }
    
    private void initTable() {
        //JTable will not change height if we change font!
        //table.setColumnModel(new GColumnModel());
        table.setCellEditor(table.getDefaultEditor(Number.class));
        clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//important for scrollPane pane
        table.setColumnSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        scrollPane = new JScrollPane(table) {
            //to hide the header, because header will be visible if table is in a scrollpane!!
            public void setColumnHeaderView(Component view) {}
        };
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //getModel().update(); CALLING THIS LATER. THIS HERE WONT WORK??!!
        
        //now fixedTable workaround:
        fixedTable = new GTable(new FixedTableModel(getModel()));
        getTableHeader().setReorderingAllowed(true);
        fixedTable.setCellSelectionEnabled(false);
        fixedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        FixedColumnModel colModel = new FixedColumnModel(table);
        fixedTable.setColumnModel(colModel);
        colModel.addColumnModelListener(new FixedTableColumnModelListener());
        JScrollPane fixedScroll = new JScrollPane(fixedTable);
        fixedScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        fixedScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        //to paint a "empty" rectangle instead of the bar:
        JScrollBar bar = fixedScroll.getVerticalScrollBar();
        JScrollBar dummyBar = new JScrollBar() {
            public void paint(Graphics g) {}
        };
        dummyBar.setPreferredSize(bar.getPreferredSize());
        fixedScroll.setVerticalScrollBar(dummyBar);
        
        final JScrollBar bar1 = fixedScroll.getHorizontalScrollBar();
        JScrollBar bar2 = scrollPane.getHorizontalScrollBar();
        bar2.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                bar1.setValue(e.getValue());
            }
        });
        
        //LATER=> because of new columnmodel we should fire tablestructure changes etc.
        //(may be there was an initial size setting! gcolumnmodel should initialised with
        // these values: namly the different columnrenderers)
        getModel().update();
        
        int WIDTH = 400;
        scrollPane.setPreferredSize(new Dimension(WIDTH, 200));
        int rows = (int)Math.round(fixedTable.getCellRect(0, 0, true).getHeight())*
                fixedTable.getModel().getRowCount();
        JTableHeader tableHeader = fixedTable.getTableHeader();
        int header = (int)Math.round(tableHeader.getPreferredSize().getHeight());
        
        fixedScroll.setPreferredSize(new Dimension(WIDTH, header + rows));
        fixedScroll.setBorder(new EmptyBorder(2,2,1,2));//remove bottom
        scrollPane.setBorder(new EmptyBorder(0,2,2,2));//remove top
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(fixedScroll, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        //fixedTable workround end
        
        initActions();
    }
    
    private void initActions() {
        InputMap im = table.getInputMap();
        ActionMap am = table.getActionMap();
        
        im.put(KeyStroke.getKeyStroke("control C"), "copy");
        am.put("copy", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                clip.setContents(new StringSelection(
                        getSelected().toString()), GTablePanel.this);
            }
        });
        
        im.put(KeyStroke.getKeyStroke("control shift X"), "cut(del)");
        //TODO 1
        
        im.put(KeyStroke.getKeyStroke("control X"), "cut(clear)");
        am.put("cut(clear)", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                clip.setContents(new StringSelection(
                        getSelected().toString()), GTablePanel.this);
                
                removeSelected();
            }
        });
        
        im.put(KeyStroke.getKeyStroke("control shift V"), "paste(overwr)");
        am.put("paste(overwr)", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Transferable cont = clip.getContents(GTablePanel.this);
                if (cont != null) {//if clibpoard is not empty
                    try {
                        String s = (String)cont.getTransferData(DataFlavor.stringFlavor);
                        set(s);
                    } catch (Exception exc) {
                        Log.err("\nError while pasting:", false);
                        Log.err(exc.getMessage(), false);
                    }
                }
            }
        });
        
        //paste area(will only insert, where cells are selected)
        im.put(KeyStroke.getKeyStroke("control alt V"), "paste(area)");
        am.put("paste(area)", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Transferable cont = clip.getContents(GTablePanel.this);
                if (cont != null) {//if clibpoard is not empty
                    try {
                        String s = (String)cont.getTransferData(DataFlavor.stringFlavor);
                        setSelected(s);
                    } catch (Exception exc) {
                        Log.err("\nWhile pasting:", false);
                        Log.err(exc.getCause(), false);
                    }
                }
            }
        });
        
        im.put(KeyStroke.getKeyStroke("control V"), "paste(ins)");
        //TODO 3
        
        //TODO: we need a more general undo operation!
        im.put(KeyStroke.getKeyStroke("control Z"), "undo");
        am.put("undo", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                undoSetSelection();
            }
        });
        /**
         TODO we need:
         clear & delete & add & insert: cols, rows
         hide area & unhide all
         */
        
        
        
        addMouseListener(new GTPMouseListener());
        allActions = new ArrayList();
        
        //Set Title
        allActions.add(new Helper() {
            String s[]={PluginPool.HEADER};
            public String[] getActionContextStrings() {
                return s;
            }
            
            ButtonMenuItem pmi;
            
            public boolean addItem(MouseEvent me, JPopupMenu popup) {
                //int col = ((JTableHeader)comp).columnAtPoint(me.getPoint());
                //if(col < 0) return null;
                IDData idData = getColumnAtPoint(me.getPoint());
                if(idData==null) return false;
                pmi = new ButtonMenuItem(
                        "Set title of :"+idData.getID(), this, popup);
                pmi.getTextField().setText("editedtitle");
                return true;
            }
            
            public void actionPerformed(ActionEvent ae) {
                String ac = ae.getActionCommand();
                int i = ac.indexOf(":")+1;
                getModel().setTitle(ac.substring(i), pmi.getTextField().getText());
            }
        });
        
        //Plot
        allActions.add(new Helper() {
            String s[] = {PluginPool.HEADER, PluginPool.TABLE , PluginPool.ELSE};
            public String[] getActionContextStrings() {
                return s;
            }
            
            //VectorInterface vector[];
            
            public boolean addItem(MouseEvent me, JPopupMenu popup) {
                if(table.getSelectedColumns().length <= 1) return false;
                
                PluginPool.getDefault().sendRequest(
                        new GTPRequestEvent(PluginPool.SELECTED_COLS, popup));
                
                /*if(col.length == 2) {
                    JMenuItem regression = new JMenuItem("Linear Regression");
                    regression.addActionListener(this);
                    popup.add(regression);
                }*/
                return true;
            }
            
            public void actionPerformed(ActionEvent ae) {
            /*    //String ac = ae.getActionCommand();
                //if(plotPanel == null) {    //OLD gFrame.getDataPoolPanel().createDefaultPlot();
                //TODO VectorPool.getDefault().createVector()
                //}
                //int col[]=table.getSelectedColumns();
                //int row[]=table.getSelectedRows();
                //XYInterface al[] = ((GTableModel)table.getModel()).getXYColumns(row, col);
                //for(int i = 1; i < al.length; i++) {
                //TODO plotPanel.addData(al[i]);PlotLibraryInterface.addXY}
                XYPool xyPool = (XYPool)MainPool.getDefault().create(XYPool.class);
                XYInterface interfac = xyPool.add(vector[0], vector[1]);
                xyPool.add(PluginPool.getDefault().getFitEngine().fit(interfac));
                PluginPool.getDefault().getPlotEngine().plot(xyPool, xyPool.getID());
             */
            }
            
        });
        
        //Add Columns
        allActions.add(new Helper() {
            String s[]={PluginPool.ELSE, PluginPool.HEADER, PluginPool.TABLE};
            public String[] getActionContextStrings() {
                return s;
            }
            
            ButtonMenuItem pmi;
            
            public boolean addItem(MouseEvent me, JPopupMenu popup) {
                pmi = new ButtonMenuItem("Add Columns", this, popup);
                pmi.getTextField().setText("  1");
                return true;
            }
            
            public void actionPerformed(ActionEvent ae) {
                //String ac = ae.getActionCommand();
                int cols =-1;
                try{ cols = Integer.parseInt(pmi.getTextField().
                        getText().trim());
                } catch(Exception exc){ cols  = 1;}
                
                for(int i=0; i<cols; i++)
                    getModel().setValueAt("", GTableModel.DEFAULT_ROWS,
                            table.getColumnCount());
            }
        });
        
        //Add Rows
        allActions.add(new Helper() {
            String s[]={PluginPool.ELSE, PluginPool.HEADER, PluginPool.TABLE};
            
            public String[] getActionContextStrings() {
                return s;
            }
            ButtonMenuItem pmi;
            
            public boolean addItem(MouseEvent me, JPopupMenu popup) {
                pmi = new ButtonMenuItem("Add Rows", this, popup);
                pmi.getTextField().setText("  1");
                return true;
            }
            public void actionPerformed(ActionEvent ae) {
                //String ac = ae.getActionCommand();
                int cols =-1;
                try{ cols = Integer.parseInt(pmi.getTextField().
                        getText().trim());
                } catch(Exception exc){  cols = 1; }
                
                for(int i=0; i<cols; i++)
                    getModel().setValueAt("", table.getRowCount(),
                            GTableModel.DEFAULT_COLS);
            }
        });
        
        //Gnuplot
        allActions.add(new Helper() {
            String s[]={PluginPool.ELSE, PluginPool.HEADER, PluginPool.TABLE};
            public String[] getActionContextStrings() {
                return s;
            }
            
            JMenuItem pmi;
            
            public boolean addItem(MouseEvent me, JPopupMenu popup) {
                pmi = new JMenuItem("Detect Gnuplot");
                pmi.addActionListener(this);
                popup.add(pmi);
                return true;
            }
            
            public void actionPerformed(ActionEvent ae) {
                Detection.run(Detection.GNU_PLOT);
            }
        });
        
    }
    
    /** This class helps a little bit with action init,performing...
     * ActionHelper is not short enough :-)
     */
    interface Helper extends ActionListener {
        
        public String[] getActionContextStrings();
        public boolean addItem(MouseEvent me, JPopupMenu popup);
    }
    
    /** This class specifies how we send info's to possible listeners(plugins)
     */
    class GTPRequestEvent implements RequestEvent {
        //MouseEvent me;
        String reason;
        JPopupMenu pop;
        //Object o;
        
        public GTPRequestEvent(String reason, JPopupMenu pop) {
            this.pop = pop;
            //this.me = me;
            this.reason = reason;
        }
        
        public Object getObject() {
            return pop;
        }
        
        public String getActionContextReason() {
            return reason;
        }
        
        //        public JPopupMenu getPopup() {            return pop;        }
        
        public Object getSource() {
            return GTablePanel.this;
        }
        
        /*public MouseEvent getMouseEvent() {
            return me;
        }*/
    }
    
    
    class GTPMouseListener extends GMouseAdapter {
        
        /** Overload mouseclicked to select one whole column
         */
        public void gMouseClicked(MouseEvent me) {
            Component comp = me.getComponent();
            try {
                //a click should update cells! but update will remove selection !? getModel().update();                

                //the next line causes an exception if !comp instanceof JTableHeader
                int col = ((JTableHeader)comp).columnAtPoint(me.getPoint());
                table.getSelectedRowCount();
                table.addRowSelectionInterval(0,table.getRowCount()-1);
                table.addColumnSelectionInterval(col,col);
                
            } catch(Exception exc) {
                //it is 'normal' that user clicked not on JTableHeader ...
            }
        }
        
        public void showPopup(MouseEvent me) {
            if(popupSupport == null)
                popupSupport = new PopupSupportImpl(me);
            else popupSupport.setMouseEvent(me);
            
            PluginPool.getDefault().getPlatform().showPopup(popupSupport);
        }
    }
    
    protected class PopupSupportImpl implements Platform.PopupSupport {
        
        public PopupSupportImpl(MouseEvent me) {
            setMouseEvent(me);
        }
        
        public JPopupMenu createPopup() {
            Component comp = me.getComponent();
            JPopupMenu popMenu = new JPopupMenu();
            //JMenuItem mi;
            
            String pos = PluginPool.ELSE;
            if(comp == table) pos = PluginPool.TABLE;
            else if(comp==getTableHeader()) {
                if(getTableHeader().columnAtPoint(me.getPoint())==-1)
                    pos = PluginPool.INVISIBLE_HEADER;
                else
                    pos = PluginPool.HEADER;
            }
            String context[];
            for(int i=0; i<allActions.size();i++) {
                Helper helper = (Helper)allActions.get(i);
                context = helper.getActionContextStrings();
                for(int n=0; n < context.length; n++)
                    if(context[n].equals(pos))
                        helper.addItem(me, popMenu);
            }
            
            PluginPool.getDefault().sendRequest(
                    new GTPRequestEvent(pos, popMenu));
            
            return popMenu;
        }
        
        private void setMouseEvent(MouseEvent me) {
            this.me = me;
        }
        
        MouseEvent me;
        
        public MouseEvent getMouseEvent() {
            return me;
        }
        
        public boolean isPopupAllowed() {
            return true;
        }
    }
    
    /** Removes specified MouseListener from this component.
     */
    public void removeMouseListener(MouseListener ml) {
        table.removeMouseListener(ml);
        getTableHeader().removeMouseListener(ml);
        scrollPane.removeMouseListener(ml);
    }
    
    void stopCellEditing() {
        if(!getTable().getCellEditor().stopCellEditing())
            getTable().getCellEditor().cancelCellEditing();
    }
    
    /** Who wants the Listener have? JScrollpane, GTable and JTableHeader!
     */
    public void addMouseListener(MouseListener l) {
        //super.addMouseListener(l);
        table.addMouseListener(l);
        getTableHeader().addMouseListener(l);
        scrollPane.addMouseListener(l);
    }
    
    
    public Component getComponent() {
        return mainPanel;
    }
    
    protected JTableHeader getTableHeader() {
        return fixedTable.getTableHeader();
    }
    
    /** This method returns the current view position of scrollpanes viewport.
     */
    protected Point getViewPosition() {
        return scrollPane.getViewport().getViewPosition();
    }
    
    /** Who wants the Listener have? gtable,
     */
    //public void addKeyListener(KeyListener l) {    }
    
    /** This method defines where all the data should plotted into.
     * This method changes the locale of plotPanel.
     */
    /*public void setPlotPanel(GPlotPanel plotPanel) {
        this.plotPanel = plotPanel;
        //TODO:
        plotPanel.setLocale((Locale)GProperties.getDefault().get("locale"));
    }*/
    
    /** This method returns the plotarea where all the data should plotted into.
     *
     public GPlotPanel getPlotPanel() {
     return plotPanel;
     }*/
    
    /** This method returns the object of the underlying tablemodel.
     */
    public GTableModel getModel() {
        return (GTableModel)table.getModel();
    }
    
    protected JTable getTable() {
        return table;
    }
    
    protected IDData getColumnAtPoint(Point p) {
        int col = getTableHeader().columnAtPoint(p);
        return getModel().get(getColumnModelIndex(col));
    }
    
    protected int getColumnModelIndex(int index) {
        if(index < 0) return index;
        return getTableHeader().getColumnModel().getColumn(index).getModelIndex();
    }
    
    public int[] getSelectedRows() {
        return table.getSelectedRows();
    }
    
    public int[] getSelectedColumns() {
        return table.getSelectedColumns();
    }
    
    /** This method returns the selected data as StringBuffer
     */
    protected StringBuffer getSelected() {
        int cols[]=getSelectedColumns();
        int rows[]=getSelectedRows();
        viewIndiciesToModel(cols);
        return getModel().get(rows, cols);
    }
    
    /** This method inits the selected cells with the argument(String).
     * This method enables us to use the "paste" feature.
     * With saving old values for this operation we can do an undo.
     */
    public void setSelected(String data_colsAndRows) {
        setCols = table.getSelectedColumns();
        setRows = table.getSelectedRows();
        
        GTableModel tm = getModel();
        String dataAsRows[] = data_colsAndRows.split(tm.getLineSep());
        String cSep = tm.getColSep();
        viewIndiciesToModel(setCols);
        String cells[];
        oldValues = getSelected();
        
        for(int row = 0; row < Math.min(dataAsRows.length, setRows.length); row++) {
            cells = dataAsRows[row].split(cSep);
            
            for(int col = 0; col < Math.min(cells.length, setCols.length); col++) {
                tm.setValueAt(cells[col], setRows[row], setCols[col]);
            }
        }
    }
    
    /** This method enables us to use the "paste" feature.
     * With saving old values for this operation we can do an undo.
     */
    public void set(String data_colsAndRows) {
        int startCol = table.getEditingColumn();
        int startRow = table.getEditingRow();
        if(startCol ==-1) {
            startCol = table.getSelectedColumn();
            startRow = table.getSelectedRow();
        }
        if(startCol ==-1) {
            startCol=0;
            startRow=0;
        }
        
        GTableModel tm = getModel();
        String lSep = tm.getLineSep();
        String cSep = tm.getColSep();
        String dataAsRows[] = data_colsAndRows.split(lSep);
        String cells[];
        
        //This method is a little bit tricky: we want the view indicies of cols
        //so we would do "for(columns){ for(rows){...}}"
        //
        //SOLUTION: CHANGE THIS TO "for(rows){for(column){...}}"!! is slower but easier!
        //
        //AND we can split the data_colsAndRows string only 1. for lines and
        //then 2. for cols => we would do "for(rows){for(column){...}}"
        //so how would you do it???
        
        
        TableColumn tc;
        //enum over view-indicies
        Enumeration enu;
        //if(oldValues!=null)
        //oldValues.delete(0,oldValues.length());
        //else oldValues = new StringBuffer();
        
        for(int row = 0; row < dataAsRows.length; row++) {
            cells = dataAsRows[row].split(cSep);
            enu = getTableHeader().getColumnModel().getColumns();
            
            //find start col
            for(int temp=0;enu.hasMoreElements();temp++){
                if(temp==startCol) break;
                enu.nextElement();
            }
            
            for(int col=0; enu.hasMoreElements() && col < cells.length; col++) {
                tc = (TableColumn)enu.nextElement();
                
                //oldValues.append(
                //        tm.valueToString(tm.getValueAt(row+startRow, tc.getModelIndex())));
                //oldValues.append(cSep);
                tm.setValueAt(cells[col], row+startRow, tc.getModelIndex());
            }
            //oldValues.delete(oldValues.length()-cSep.length(),oldValues.length());
            //oldValues.append(lSep);
        }
    }
    
    /** This method transforms an view-column-indicies-array to an model-inicies-array.
     */
    private void viewIndiciesToModel(int viewColInd[]) {
        TableColumnModel cm = table.getColumnModel();
        //transform: view model -> col-model
        for(int i = 0; i < viewColInd.length; i++) {
            if(viewColInd[i] < 0)
                continue;
            viewColInd[i] = cm.getColumn(viewColInd[i]).getModelIndex();
        }
    }
    
    /** This method removes the selected cells in table. With saving
     * old values for this operation we can do an undo
     */
    public void removeSelected() {
        setCols = table.getSelectedColumns();
        setRows = table.getSelectedRows();
        GTableModel tm = getModel();
        
        viewIndiciesToModel(setCols);
        oldValues = getSelected();
        
        for(int row = 0; row < setRows.length; row++) {
            for(int col = 0; col < setCols.length; col++) {
                //the value has to be != "" and !=null see setValue
                tm.setValueAt(" ", setRows[row], setCols[col]);
            }
        }
        
    }
    
    /** This method makes an undo from setSelected or removeSelected.
     */
    public void undoSetSelection() {
        if(oldValues == null) return;
        
        GTableModel tm = getModel();
        String cSep = tm.getColSep();
        String dataAsRows[] = oldValues.toString().split(tm.getLineSep());
        String cells[];
        
        for(int row = 0; row < dataAsRows.length; row++) {
            cells = dataAsRows[row].split(cSep);
            
            for(int col = 0; col < cells.length; col++) {
                tm.setValueAt(cells[col], setRows[row], setCols[col]);
            }
        }
    }
    
    
    public VectorInterface[] getSelectedVectors() {
        int col[]=table.getSelectedColumns();
        //int row[]=table.getSelectedRows();
        
        VectorInterface vector[] = new VectorInterface[col.length];
        
        for(int i = 0; i< col.length; i++)
            vector[i] = (VectorInterface)getModel().get(getColumnModelIndex(col[i]));
        
        return vector;
    }
    
    /** overwritten, because of ClipboardOwner
     */
    public void lostOwnership(Clipboard clip, Transferable ta) {
    }
    
    class FixedTableColumnModelListener implements TableColumnModelListener {
        public void columnAdded(TableColumnModelEvent e) {
        }
        
        public void columnRemoved(TableColumnModelEvent e) {
        }
        
        public void columnMoved(TableColumnModelEvent e) {
            JTableHeader th = fixedTable.getTableHeader();
            TableColumn tc = th.getDraggedColumn();
            
            if(tc != null ) {
                if(e.getFromIndex() != e.getToIndex()) {
                    table.getColumnModel().
                            moveColumn(e.getFromIndex(),e.getToIndex());
                }
                //TODO
                /*if(th.getDraggedDistance()!=0) {
                    table.getTableHeader().setDraggedColumn(
                            table.getColumnModel().getColumn(e.getFromIndex()));
                    table.getTableHeader().setDraggedDistance(
                            th.getDraggedDistance());
                }*/
            }
            
        }
        
        public void columnMarginChanged(ChangeEvent e) {
        }
        
        public void columnSelectionChanged(ListSelectionEvent e) {
        }
    }
}
