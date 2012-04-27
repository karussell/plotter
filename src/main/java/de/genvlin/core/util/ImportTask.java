/*
 * ImportTask.java
 *
 * Created on 31. März 2006, 14:03
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

package de.genvlin.core.util;

import de.genvlin.core.data.VectorPool;
import de.genvlin.core.plugin.Log;
import de.genvlin.gui.table.GTableModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/** This class provides methods to read from a stream parallel.
 *
 * TODO: test concurrent modifications/access.
 * @author Peter Karich
 */
public class ImportTask extends GTask {
    
    private GTableModel tableModel = null;
    private String sep;
    private InputStream is = null;
    private int oldAvailable;
    private int newAvailable;
    private int size;
    
    
    /** TODO? called from TableGUISupport - bad performance on start up?
     */
    protected ImportTask() {    }
    
    /** This method imports a specific file from folder.<br>
     * TODO better performance via parsing and adding (not via "setting")!
     * And use "raw" AbstractVector for that.
     */
    public ImportTask(InputStream is) {
        this(is, "\t");
    }
    
    /** This contructs a task which imports a specific file from folder.
     * It will parse the stream and split it whereever the specified String
     * occurs to create the columns.<br>
     * TODO better performance via parsing and adding not setting!
     * And use "raw" AbstractVector for that.
     */
    public ImportTask(InputStream is, String sep) {
        setInputStream(is);
        setSeparator(sep);
    }
    
    public int getInitSize() {
        return size;
    }
    
    protected void setInputStream(InputStream is) {
        if(isStarted()) return;
        this.is = is;
        newAvailable  = oldAvailable = size = getAvailable();
    }
    
    /*
    protected InputStream getInputStream() {
        return is;
    }*/
    
    /** This method sets the column separator, but only if current thread
     * was not started!
     */
    public void setSeparator(String s) {
        if(isStarted()) return;
        sep = s;
    }
    
    /** Call start() instead!!
     */
    public void runTask() {
        String columns[];
        String line;
        
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        tableModel = new GTableModel();
        //update!
        tableModel.setColSep(sep);        
        
        try{
            int row = 0;
            //we want to have status info's, so we cant do tableModel.import(in)! (?)
            for(; !isInterrupted() && (line = in.readLine()) != null;
            row++) {
                if(line.trim().length() == 0) continue;
                if(line.trim().startsWith("#") ) continue;
                if(line.trim().startsWith("//")) continue;
                
                columns = line.split(sep);
                tableModel.setRow(row, columns);
                //the first time (row==0) indicates start event
                if(row % 500 == 0) {
                    checkAvailableChanged();
                }
            }
            Log.log(""+row, false);
        } catch(IOException ioe) {
            tableModel = null;
        }
        try { in.close();
        } catch (IOException ex) {
            Log.err("Exception while importing: " + ex.toString(), true);
            Log.err(ex, false);
        }
    }
    
    /** @returns true if importing finished successfully,
     * false otherwise.
     */
    public boolean isValid() {
        if(isFinished() && tableModel != null)
            return true;
        else return false;
    }
    
    /** Returns the current <tt>VectorPool</tt>.
     */
    public Object getResult(){
        if(isFinished())
            return tableModel.getPool();
        else return null;
    }
    
    /** This method sets the table where you want to put in the results.
     * But only if getStatus returns zero!
     */
    public void setPool(VectorPool table) {
        if(!isStarted()) return;
        
        if(table!=null) {
            tableModel.set(table);
            fireChangeEvent(STATUS, new Integer(-1));
        }
    }
    
    protected void checkAvailableChanged() {
        newAvailable = getAvailable();
        if(oldAvailable != newAvailable) {
            oldAvailable = newAvailable;
            fireChangeEvent(STATUS, new Integer(getInitSize() - newAvailable));
        }
    }
    
    private int getAvailable() {
        try { return is.available();
        } catch (IOException ex) {
            Log.err(ex.toString(), true);
        }
        return -1;
    }
}