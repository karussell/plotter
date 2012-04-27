/*
 * SaveTask.java
 *
 * Created on 1. April 2006, 12:21
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
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/** TODO: test.
 *
 * @author Peter Karich
 */
public class SaveTask extends GTask {
    
    private OutputStream os;
    private GTableModel tableModel = null;
    private boolean noException = true;
    
    /** No. of steps (only the exponent), which will be performed while
     * reading - to update status.
     */
    private int SIZE = 16;
    
    /** No. of steps (only the exponent) =2^ 4 =16 steps!!
     */
    private int SHIFT = 4;
    
    public SaveTask(OutputStream os) {
        this.os = os;
    }
    
    /** The file will be in a undefine state. So save again after that
     * or backup before using this method!!!
     */
    public void interrupt() {
        super.interrupt();
    }
    
    private GTableModel getTableModel() {
        if(tableModel == null)
            tableModel = new GTableModel();
        
        return tableModel;
    }
    
    /** @returns true if saving finished without exceptions,
     * false otherwise.
     */
    public boolean isValid() {
        if(!isFinished()) return false;
        
        return noException;
    }
    
    /** Returns the current <tt>VectorPool</tt>.
     */
    private VectorPool getPool(){
        if(!isFinished()) return null;
        
        return getTableModel().getPool();
    }
    
    /** This method returns null!
     */
    public Object getResult() {
        return null;
    }
    
    /** Sets the pool which should be saved to outputstream.
     */
    public void setPool(VectorPool table) {
        if(table!=null) {
            getTableModel().set(table);
            getTableModel().trimAllToMaxRowCount();
            fireChangeEvent(STATUS, new Integer(-1));
        }
    }
    
    public void runTask() {
        OutputStreamWriter osw = new OutputStreamWriter(os);
        StringBuffer all = getTableModel().getAll();
        char block[];
        try {
            int calc = all.length()>>SHIFT;
            int i=0;
            
            for(; i<SIZE && !isInterrupted(); i++) {
                fireChangeEvent(STATUS,new Integer(i));
                block = new char[calc];
                all.getChars(i*calc, (i+1)*calc, block, 0);
                osw.write(block);
            }
            //write the rest
            block = new char[all.length()-i*calc];
            if(block.length>0) {
                all.getChars(i*calc, all.length(), block, 0);
                osw.write(block);
            }
        } catch(IOException ioe) {
            noException = false;
        }
        try { osw.close();
        } catch (IOException ex) {
            noException = false;
            Log.err("Exception while saving: " + ex.toString(), true);
            Log.err(ex, false);
        }
        
        /** All this could be easier, if you want no status informations:
         OutputStreamWriter osw = new OutputStreamWriter(os);
         try{
         osw.write(getTableModel().getAll().toString());
         osw.close();
         } catch(IOException ioe) {
         noException = false;
         }*/
    }
}
