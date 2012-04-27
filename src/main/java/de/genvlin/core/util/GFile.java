/*
 * GFile.java
 *
 * Created on 7. April 2006, 11:22
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

import de.genvlin.core.plugin.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 *
 * @author Peter Karich
 */
public class GFile {
    
    static public class Helper {
        public Helper(String location, String pluginName, String className) {
            this.location = location;
            this.pluginName = pluginName;
            this.className = className;
        }
        public String location, pluginName, className;
    }
    
    static public ArrayList parseProperty(InputStream is) throws IOException{
        ArrayList al = new ArrayList();
        
        try {
            Document pluginListDoc = DocumentBuilderFactory.
                    newInstance().newDocumentBuilder().parse(is);
            Element root = pluginListDoc.getDocumentElement();
            NodeList list;
            
            list = root.getElementsByTagName("plugin");
            for(int i=0; i<list.getLength(); i++) {
                Element classpath = (Element) list.item(i);
                al.add(
                        new Helper(classpath.getAttributes().getNamedItem("location").getNodeValue()
                        ,classpath.getAttributes().getNamedItem("pluginName").getNodeValue()
                        ,classpath.getAttributes().getNamedItem("className").getNodeValue()));
            }
            
        } catch (ParserConfigurationException e) {
            Log.err(e, false);//ml.core.exceptions.
            throw new IOException("unable to parse plugin list XML file");
        } catch (SAXException e) {
            //ml.core.exceptions.
            Log.err(e, false);
            throw new IOException("unable to parse plugin list XML file");
        } catch (IOException e) {
            Log.err(e, false);
            throw new IOException("unable to parse plugin list XML file");
        }
        return al;
    }
    
    
}
