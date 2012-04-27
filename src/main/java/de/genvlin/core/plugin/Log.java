/*
 * Log.java
 *
 * Created on 23. März 2006, 22:18
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

import java.awt.Toolkit;

/**
 * Today only one Log.SPI could be added to this Logger!
 * May be Logger l = java.util.logging.Logger.getLogger(); is better...
 *
 * @author Peter Karich
 */
public class Log { //implements LogPlugin
    
    private static LogPlugin li=null;
    private static LogPlugin STD;
    
    private static synchronized LogPlugin getDefault() {
        //check if we have "better" logger or even null
        if(li == null || li instanceof STDLogger) {
            LogPlugin tmp = PluginPool.getDefault().getLogEngine();
            if(tmp==null) 
                li = getSTD();
            else li = tmp;
        }
        return li;
    }
    
    private static LogPlugin getSTD() {
        if(STD == null) STD= new STDLogger();
        return STD;
    }
    
    /** Today only one LogSPI implementation could be added!
     static public void add(LogSPI l) {
     li = l;
     }*/
    
    /**
     * Today only one LogSPI is available!
     static public void remove(LogSPI l) {
     if(l==li) li = null;
     }*/
    
    /** This method writes the whole exception to an error-log file.
     * @param notify if true, the user gets a message.
     */
    static public void err(Throwable t, boolean notify) {
        getDefault().err(t, notify);
    }
    
    /** This method writes the specified message to a log file.
     * @param notify if true, the user gets the message.
     */
    static public void err(String message, boolean notify) {
        getDefault().err(message, notify);
    }
    
    /** This method writes the message to a log file to a log file.
     * @param notify if true, the user gets a message.
     */
    static public void log(String message, boolean notify) {
        getDefault().log(message, notify);
    }
    
    /** An implementation which pipes all things to standard output.
     */
    static class STDLogger implements LogPlugin {
        public void log(String s, boolean notify) {
            System.out.println(s);
            if(notify) Toolkit.getDefaultToolkit().beep();
        }
        
        public void err(Throwable t, boolean notify) {
            t.printStackTrace();
            if(notify) Toolkit.getDefaultToolkit().beep();
        }
        
        public void err(String s, boolean notify) {
            System.err.println(s);
            if(notify) Toolkit.getDefaultToolkit().beep();
        }
        
        public String getName() {
            return "Standard Output Logger";
        }
        
        String s[] ={"logger"};
        
        public String[] getActionContextReasons() {
            return s;
        }
        
        public void sendRequest(RequestEvent ri) {
            
        }
    }
}