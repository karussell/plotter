/*
 * Detection.java
 *
 * Created on 26. April 2006, 20:47
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

import de.genvlin.core.plugin.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A simple class to detect externs programs.
 *
 * @author Peter Karich
 */
public class Detection {
    
    /** Creates a new instance of Detection */
    public Detection() {
    }

    /** A command, which will give us the programname and its version.
     */
    final static public String GNU_PLOT = "gnuplot --version";        
    
    static public void run(String program) {
        try {
            Process p = Runtime.getRuntime().exec(program);
            BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
            Log.log("Detected program:"+is.readLine(),true);
            p.waitFor();
            if(p.exitValue()==0)
                Log.log("Normal termination. Program seems to work.",true);
            else
                Log.log("Abnormal termination. Program will not work.",true);
        } catch (IOException ex) {
            Log.log("Sorry can't detect gnuplot! Reason:"+ex.getMessage(),true);        
        } catch (InterruptedException iex) {
            Log.log("Interrupted while waiting. No return value detected!"+
                    "Sorry, try again!",true);            
        }            
    }
}
