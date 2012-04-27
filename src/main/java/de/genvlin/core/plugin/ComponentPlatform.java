/*
 *  Copyright 2012 Peter Karich info@jetsli.de
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package de.genvlin.core.plugin;

import java.awt.Component;
import javax.swing.JComponent;

/**
 * @author Peter Karich
 */
public class ComponentPlatform implements Platform.SPI {

    Component comp;

    public ComponentPlatform(Component comp) {
        this.comp = comp;
    }

    @Override public boolean showPanel(Component panel, String position) {
        // TODO
        return true;
    }

    @Override public void showPopup(Platform.PopupSupport s) {
        s.createPopup().show(comp, s.getMouseEvent().getX(), s.getMouseEvent().getY());
    }

    @Override public String getName() {
        return comp.getName();
    }

    @Override public String[] getActionContextReasons() {
        // TODO
        return new String[0];
    }

    @Override public void sendRequest(RequestEvent ri) {
        // TODO
    }
}
