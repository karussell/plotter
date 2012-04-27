/*
 * GTabelModel.java
 *
 * Created on 14. September 2005, 20:36
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

import de.genvlin.core.data.CollectionEvent;
import de.genvlin.core.data.CollectionListener;
import de.genvlin.core.data.PoolInterface;
import de.genvlin.core.util.GProperties;
import de.genvlin.core.data.ID;
import de.genvlin.core.data.IDData;
import de.genvlin.core.data.IntID;
import de.genvlin.core.data.MainPool;
import de.genvlin.core.data.VectorInterface;
import de.genvlin.core.data.VectorPool;
import de.genvlin.core.data.XYInterface;
import de.genvlin.core.plugin.Log;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.util.Iterator;
import javax.swing.table.*;


/** This class implements a simple to use table model.
 * (wrappes around <tt>VectorPool</tt>)<p>
 * Simple means, that if we set a value the model will set this value
 * by expanding the table so, that this position is valid if table was too small.
 * <tt>GTableModel</tt> is a pool with <tt>VectorInterface</tt>'s as entries.
 * TODO remove public access (->ImportTask)
 * @author Peter Karich
 */
public class GTableModel extends AbstractTableModel
        implements PoolInterface, PropertyChangeListener,
        CollectionListener {
    static final long serialVersionUID = 432233241L;
    
    /** This variable contains the columns. */
    protected VectorPool data;
    
    /** Because of performance reasons: not all columns have the same size.
     * But the JTable need a row size (see RowCount) for visualisation.
     */
    private int maxRowCount = 0;
    
    /** default row size if adding a column
     */
    static public int DEFAULT_ROWS = -1;
    
    /** default col size if adding a row
     */
    static public int DEFAULT_COLS = -1;
    
    private NumberFormat numberFormat;
    private String importColSeparator;
    private String importLineSeparator;
    
    /** These sep. are only because we don't want to call GProp....
     */
    private String globalColSeparator, globalLineSeparator;
    
    /** This method constructs a new tablemodel with rows, cols
     */
    public GTableModel(int rowIndex, int columnIndex) {
        this(null);
        setValueAt(null, rowIndex, columnIndex);
    }
    
    /** Creates a new instance of GTabelModel */
    public GTableModel() {
        this(null);
    }
    
    public GTableModel(VectorPool pool) {
        super();
        //updateSeparators();
        //enableSeparatorAutomaticUpdate(true);
        
        //listen on global sep. changes:
        GProperties.getDefault().addPropertyChangeListener(this);
        //init global separators
        propertyChange(null);
        //init import separators
        importColSeparator = globalColSeparator;
        importLineSeparator = globalLineSeparator;
        
        DEFAULT_ROWS = ((Integer)GProperties.getDefault().get("table.defaultrows")).intValue();
        DEFAULT_COLS = ((Integer)GProperties.getDefault().get("table.defaultcols")).intValue();
        set(pool);
        
        numberFormat = (NumberFormat)GProperties.getDefault().get("numberformat");
        update();//really necessary?
    }
    
/*    public void enableSeparatorAutomaticUpdate(boolean update) {
        if(update)
            GProperties.getDefault().addPropertyChangeListener(this);
        else
            GProperties.getDefault().removePropertyChangeListener(this);
    }
 */
    
    /** This method returns the column string-separator e.g. "\t" used for
     * import and export.
     */
    public String getColSep() {
        return importColSeparator;
    }
    
    /** This method returns the line string-separator e.g. "\n" used for
     * import and export.
     */
    public String getLineSep() {
        return importLineSeparator;
    }
    
    public void setColSep(String s) {
        if(s == null || s.length()==0) return;
        importColSeparator = s;
    }
    
    public void setLineSep(String s) {
        if(s == null || s.length()==0) return;
        importLineSeparator = s;
    }
    /** returns the default (whole) column name of col i
     *
     public String getNextDefaultColumnName() {
     int i = data.getCurrentID().intValue() + 1;
     
     return intToChar(i)+"\n"+i;
     }*/
    
    /**
     * This method returns the title of a column (the second part)
     * with specified id-character
     *
     *     public String getTitle(ID id) {
     *     String str = getColumnName(indexOf(id));
     *     //if(str.length()==0) return "";
     *     //return str.substring(str.indexOf('\n')+1);
     *
     *     }
     *
     *     /** This method sets the title of a column (the second part).
     * with specified idCharacter
     *
     *     public void setTitle(ID id, String str) {
     *     DataForCol dfc = getRawColumnAt(indexOf(idAsChar));
     *     if(dfc == null) return;
     *     dfc.colName = idAsChar + "\n"+str;
     *     fireTableStructureChanged();
     *     }
     */
    
    /*TODO: remove this method!?*/
    public VectorPool getPool() {
        return data;
    }
    
    /**
     * This method will initialise the table with data.<br>
     * If specified pool is <tt>null</tt> nothing happens if
     * the data already has values. Otherwise (data==<tt>null</tt>)
     * this method will create a default pool with default row and col sizes.
     */
    public void set(VectorPool pool) {
        if(pool == null) {
            if(data == null) {
                data = (VectorPool)MainPool.getDefault().create(VectorPool.class);
                setValueAt("", DEFAULT_ROWS-1, DEFAULT_COLS-1);//minus one, cause of size=row-1!!
            }
        } else {
            data = pool;
            maxRowCount = 0;
            int tmpSize;
            for(int i=0; i<pool.size(); i++) {
                tmpSize = ((VectorInterface)pool.get(i)).size();
                if(tmpSize > maxRowCount)
                    maxRowCount = tmpSize;
            }
        }
        
        update();
        addVectorListener(this);
    }
    
    /** This method sets the title of a column (the second part).
     * with specified idCharacter
     * TODO @deprecated substitue call of IntID();!!??
     */
    public void setTitle(String idAsInteger, String str) {
        VectorInterface av = (VectorInterface)get(new IntID(
                new Integer(idAsInteger).intValue()));
        if(av == null) return;
        av.setTitle(str);
        fireTableStructureChanged();
    }
    
    /** This method returns the names of each column
     */
    public String getColumnName(int columnIndex) {
        VectorInterface iddata = (VectorInterface)get(columnIndex);
        if(iddata == null) return "";
        if(iddata.getTitle() == null) iddata.setTitle("notitle");
        return iddata.getID().toString() +"\n"+ iddata.getTitle();
    }
    
    public ID getID() {
        return data.getID();
    }
    
    /** This method sets a value at specific position in data fiels<p>
     * If position is not valid it allocates memory to avoid exceptions!
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(rowIndex < 0 || columnIndex < 0) return;
        if(aValue == null) return;
        
        boolean changedStruct=false;
        int oldColSize= data.size();
        
        //ensure that we have 'enough' cols
        if(columnIndex >= oldColSize) {
            for(int i = oldColSize-1; i< columnIndex; i++) {
                data.create(VectorInterface.class);
            }
            changedStruct = true;
        }
        
        VectorInterface actualCol = (VectorInterface)get(columnIndex);
        int oldRowSize = actualCol.size();
        
        //expand rows
        if(rowIndex >= oldRowSize) {
            //not minus 1, because we want to add the value directly in the NEXT step after the loop
            for(int i=oldRowSize; i< rowIndex; i++)
                actualCol.add(null);
            
            //TODO test change - old: actualCol.add(rowIndex, stringToValue(aValue.toString()));
            //new: next line
            actualCol.add(stringToValue(aValue.toString()));
            
            if(actualCol.size() >  maxRowCount)
                maxRowCount = actualCol.size();
            changedStruct = true;
        } else actualCol.set(rowIndex, stringToValue(aValue.toString()));
        
        if(changedStruct)
            fireTableStructureChanged();
        
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    /** This method init a complete row with an array of strings
     */
    public void setRow(int row, String rowAsString[]) {
        //init "backwards" (i--) => setValue is more efficient
        for(int i = rowAsString.length-1; i>=0 ; i--) {
            setValueAt(rowAsString[i], row, i);
        }
    }
    
    
    /** This method sets the complete column to specified column.
     * But it does only overwrite the elements, it does not remove
     * further elements in this column! It does not create new columns!
     *
     public void set(ID id, ArrayList al) {
     for(int row = 0; row < al.size(); row++)
     setValueAt(al.get(row), row, idToIndex(i));
     } */
    
    /** All cells are editable.
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    
    
    /** This method returns returns the specific value as Object
     * at row,column = (y,x) and is with index checking.
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex >= data.size()) return null;
        
        VectorInterface al= (VectorInterface)get(columnIndex);
        if(al == null || rowIndex >= al.size() ) return null;
        else return al.get(rowIndex);
    }
    
    
    public int indexOf(Comparable id) {
        return data.indexOf(id);
    }
    
    /** We do not need this! Use "this.get(index).getID()" instead!
     *
     *This method finds the ID of column with specified index.
     * Returns null if not found.
     *
     private ID idOf(int index) {
     Iterator iter = data.iterator();
     
     for(int i=0; iter.hasNext(); i++) {
     if(i==index)
     return ((AbstractVector)data.get(i)).getID();
     }
     return null;
     } */
    
    
    /** If the global separators has changed.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        //evt.getPropertyName()
        globalColSeparator = (String)GProperties.getDefault().get("separator.col");
        globalLineSeparator = (String)GProperties.getDefault().get("separator.line");
    }
    
    public void update() {
        fireTableStructureChanged();
        
        for(int i=0; i < data.size(); i++) {
            maxRowCount = Math.max(maxRowCount, ((VectorInterface)data.get(i)).size());
        }
        fireTableCellUpdated(getRowCount(), getColumnCount());
    }
    
    /** This method returns the number of rows (y)
     */
    public int getRowCount() {
        return maxRowCount;
    }
    
    /** This method ensures that all vector's in this model have the same
     * length.
     */
    public void trimAllToMaxRowCount() {
        int maxRow = getRowCount();
        for(int i=0; i<size(); i++)
            setValueAt("", maxRow - 1, i);//minus one, cause of size=row-1!!
    }
    
    /** This method returns the number of columns (x)
     */
    public int getColumnCount() {
        return data.size();
    }
    
    
    /** This method returns ALL data in the table as StringBuffer
     */
    public StringBuffer getAll() {
        StringBuffer sb = new StringBuffer();
        Object obj;
        //refreshSeparators();
        
        for(int rows=0; rows < getRowCount(); rows++) {
            for(int cols=0; cols < getColumnCount(); cols++) {
                obj = getValueAt(rows, cols);
                
                try {
                    sb.append(valueToString(obj)+globalColSeparator);
                }
                //catch Parse + NullPointer
                catch(Exception pe) {
                    Log.err(pe, false);
                    sb.append(globalColSeparator);
                }
            }
            sb.append(globalLineSeparator);
        }
        
        return sb;
    }
    
    /** This methods returns the rectangle of table entries as StringBuffer.
     * Define the rectangle with an array of indicies in rows respectivly cols.
     */
    public StringBuffer get(int allRows[], int allCols[]) {
        StringBuffer sb = new StringBuffer();
        Object obj;
        //refreshSeparators
        
        for(int rows=0; rows < allRows.length; rows++) {
            for(int cols=0; cols < allCols.length; cols++) {
                obj = getValueAt(allRows[rows], allCols[cols]);
                
                try {
                    sb.append(valueToString(obj));
                    
                    if(cols+1 < allCols.length)//append to all, but the last col
                        sb.append(globalColSeparator);
                } catch(Exception exc) {//catch Parse + NullPointerException
                    Log.err(exc, false);
                    sb.append(globalColSeparator);
                }
            }
            sb.append(globalLineSeparator);
        }
        
        return sb;
    }
    
    /**
     * This method returns an array of XYVectors. colInd[0] as x and
     * colInd[1] as y.
     * If(colInd.length > 2) the next XYInterface will get "column with index
     * colInd[2]" as y and colInd[0] as x.
     *
     * The indicies has to be the model indicies!
     * This would be a reference, not a deep copy!
     * TODO rowInd[] is not yet implemented! may be we only need a range?
     */
    public XYInterface[] getXYColumns(int rowInd[], int colInd[]) {
        XYInterface al[] = new XYInterface[colInd.length-1];
        
        for(int cols=1; cols < colInd.length; cols++) {
            al[cols-1] = (XYInterface)MainPool.getDefault().
                    create((VectorInterface)get(colInd[0]),
                    (VectorInterface)get(colInd[cols]));
        }
        return al;
    }
    
    /** This method returns an array of AbstractVector of the whole data.
     *
     no usage found
     public ArrayList[] getAllAsAL() {
     ArrayList al[] = new ArrayList[getColumnCount()];
     
     for(int cols=0; cols < getColumnCount(); cols++) {
     al[cols] = getColumn(cols);
     }
     
     return al;
     }*/
    
    /** This method clear all content of the columns
     */
    public void clearAllColumns() {
        for(int col = 0; col< getColumnCount(); col++) {
            ((VectorInterface)get(col)).clear();
        }
        update();
    }
    
    private VectorInterface undoVec;
    
    /** This method removes specified column.
     * Todo: undo removing*/
    public boolean remove(Comparable id) {
        undoVec = (VectorInterface)data.get(id);
        if(undoVec == null) return false;
        data.remove(id);
        fireTableStructureChanged();
        return true;
    }
    
    /** This method removes specified column.
     * Todo: undo removing*/
    public boolean remove(int index) {
        return remove(get(index).getID());
    }
    
    //check +correct these methods:
    
    /** cols will get wrong (id's) titles */
    public void undoColumnRemoving() {
        data.add(undoVec);
        fireTableStructureChanged();
    }
    
    /** This method returns the complete column. And the
     * value <tt>null</tt> if not found or if index is not valid.
     */
    public IDData get(int i) {
        return data.get(i);
    }
    
    /** This method returns the complete column via its id.
     */
    public IDData get(Comparable id) {
        return data.get(id);
    }
    
    /** This method parse a given string with the configured locale numberformat.
     * If we can't get a number this method will return <tt>null</tt>.
     */
    public Number stringToValue(String str) {
        Number obj = null;
        
        try {
            obj = numberFormat.parse(str);//if not okay -> exc
            
            //if okay double would be better:
            obj = new Double(((Number)obj).doubleValue());
        } catch(Exception exc) {
            //obj = new StringNumber(str);
            //Log.err(exc.getMessage(), false);
        }
        return obj;
    }
    
    /** This method formats a given value with the configured locale numberformat.
     */
    public String valueToString(Object aValue) {
        if(aValue == null) return "";
        String str = aValue.toString();
        
        try {
            str = numberFormat.format(aValue);
        } catch(Exception exc) {
            //Log.err(exc, false);
        }
        return str;
    }
    
    /**
     * This method imports a file with a table inside.
     * @returns int how many rows we could import
     * @param file the special file to import.
     * @param regexSep defines which column separator we should use
     */
    public int importFromFile(File file) throws IOException//, java.text.ParseException
    {
        BufferedReader in;
        String line;
        String columns[];
        
        in=new BufferedReader(new FileReader(file));
        
        clearAllColumns();
        //refreshSeparators();
        
        int row = 0;
        while( (line = in.readLine()) != null) {
            if(line.trim().length() == 0) continue;
            if(line.trim().startsWith("#") ) continue;
            if(line.trim().startsWith("//")) continue;
            
            columns = line.split(importColSeparator);
            
            setRow(row, columns);
            row++;
        }
        in.close();
        return row;
    }
    
    /** This method exports the visible table to a stream.
     */
    public boolean exportToFile(OutputStream os) throws IOException {
        /*FileWriter f;
        f = new FileWriter(file);
        StringBuffer sb = getAll();
        f.write(sb.toString());
        f.close();*/
        boolean exc = false;
        OutputStreamWriter osw = new OutputStreamWriter(os);
        try {
            osw.write(getAll().toString());
            osw.close();
        } catch(IOException ioe) {
            exc = true;
        }
        
        return exc;
    }
    
    public void clear() {
        data.clear();
    }
    
    public int compareTo(Object o) {
        if(o instanceof GTableModel)
            return data.compareTo(((GTableModel)o).data);
        else return data.compareTo(o);
    }
    
    public String getInfo() {
        return data.getInfo();
    }
    
    public String getTitle() {
        return data.getTitle();
    }
    
    public Iterator iterator() {
        return data.iterator();
    }
    
    public void removeVectorListener(CollectionListener vl) {
        data.removeVectorListener(vl);
    }
    
    public void addVectorListener(CollectionListener vl) {
        data.addVectorListener(vl);
    }
    
    public void setInfo(String info) {
        data.setInfo(info);
    }
    
    public void setTitle(String title) {
        data.setTitle(title);
    }
    
    public int size() {
        return data.size();
    }
    
    public void vectorChanged(CollectionEvent ve) {
        update();
    }
}