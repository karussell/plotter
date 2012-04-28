/*
 * LivePlotPanel.java
 *
 * Created on 15. Juli 2007, 13:30
 *
 * genvlin project.
 * Copyright (C) 2005 - 2007 Peter Karich.
 *
 * The initial version for the genvlin plotter you will find here:
 * http://genvlin.berlios.de/
 * The current release you will find here:
 * http://nlo.wiki.sourceforge.net/
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

package de.genvlin.gui.plot;

import de.genvlin.core.data.AbstractCollection;
import de.genvlin.core.data.CollectionEvent;
import de.genvlin.core.data.CollectionListener;
import de.genvlin.core.data.MainPool;
import de.genvlin.core.data.XYDataInterface;
import de.genvlin.core.data.XYVectorInterface;
import de.genvlin.gui.util.ComponentContainer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class provides a plotter for data retrieved from a running application
 * or running experiment.
 *
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
public class LivePlotPanel implements ComponentContainer {
    
    private JPanel options;
    private JLabel title;
    private JPanel mainPanel;
    private JCheckBox keepBox;
    private float percent = 0.2f;
    private boolean eastOrWest;
    private JCheckBox enabledBox;
    private GPlotPanel plotter;
    private long lastMove = 0;
    
    public LivePlotPanel(GPlotPanel gPlotter, String borderLayoutString) {
        this.plotter = gPlotter;
        plotter.getPool().addVectorListener(new CollectionListener() {
            public void vectorChanged(CollectionEvent ve) {
                if(AbstractCollection.ADD_DATA.equals(ve.getProperty())) {
                    final XYDataInterface xyData = plotter.getPool().get(ve.getID());
                    JCheckBox box = new JCheckBox(xyData.getTitle(), true);
                    box.addActionListener(new ActionListener() {
                        private boolean show = true;
                        public void actionPerformed(ActionEvent e) {
                            show = !show;
                            xyData.setHidden(!show);
                        }
                    });
                    box.setForeground(xyData.getColor());
                    addToOptionsPanel(box);
                }
            }
        });
        
        if(BorderLayout.SOUTH.equals(borderLayoutString) ||
                BorderLayout.NORTH.equals(borderLayoutString)) {
            eastOrWest = false;
        } else if(BorderLayout.WEST.equals(borderLayoutString) ||
                BorderLayout.EAST.equals(borderLayoutString)) {
            eastOrWest = true;
        } else {
            eastOrWest = true;
            borderLayoutString = BorderLayout.EAST;
        }
        
        mainPanel = new JPanel(new BorderLayout());
        
        options = new JPanel(new FlowLayout(FlowLayout.LEADING));
        options.add(title = new JLabel("Options"));
        setOptionsSize(new Dimension(200, 300));
        mainPanel.add(plotter.getComponent(), BorderLayout.CENTER);
        mainPanel.add(options, borderLayoutString);
        
        enabledBox = new JCheckBox("Enable Plotter", true);
        addToOptionsPanel(enabledBox);
        keepBox = new JCheckBox("Keep data points", false);
        addToOptionsPanel(keepBox);
    }
    
    /**
     * @param borderLayoutString requires one of BorderLayout: NORTH, SOUTH, WEST, EAST.
     * where the class should posisitionate the options component.
     */
    public LivePlotPanel(String borderLayoutString) {
        this(new GPlotPanel(), borderLayoutString);
    }
    
    public LivePlotPanel() {
        this(new GPlotPanel(), BorderLayout.EAST);
    }
    
    protected void setComponent(Component comp, String borderLayoutString) {
        mainPanel.add(comp, borderLayoutString);
    }
    
    public void setEnabled(boolean b) {
        enabledBox.setSelected(b);
    }
    
    public boolean isEnabled() {
        return enabledBox.isSelected();
    }
    
    public void setKeepDataPoints(boolean b) {
        keepBox.setSelected(b);
    }
    
    public void removeFirstIndicies(boolean force) {
        if(force || !keepBox.isSelected() && isEnabled()) {
            int size = plotter.getPool().size();
            if(size > 0) {
                
                XYDataInterface points;
                for(int i = 0; i < size; i++) {
                    points = plotter.getData(i);
                    int pointsToRemove = Math.min(points.size(), (int)(points.size() * percent));                    
                    for(int n = 0; n < pointsToRemove; n++) {
                        points.remove(0);
                    }                    
                }//for data sets
            }//if size
        }//if keep
    }
    
    public void setPointsToRemove(float percents) {
        if(percents < 0 || percents >= 1) {
            throw new IllegalArgumentException("Percents must be between 0 and 1!");
        }
        
        percent = percents;
    }
    
    /**
     * This method sets the preferred size of the options panel.
     * The default size is 300, 300
     */
    private void setOptionsSize(Dimension dim) {
        options.setPreferredSize(dim);
    }
    
    /**
     * This method adds a component to the options panel. E.g. for controlling
     * the plotter panel.
     */
    public void addToOptionsPanel(Component comp) {
        options.add(comp);
        
        //increase size of options and title if necessary
        applySizeIfBigger(options, comp);
        applySizeIfBigger(title, comp);
        
        //don't increase size of added component if too small after previous steps
        applySizeIfBigger(comp, options);
        
        options.revalidate();
    }
    
    private void applySizeIfBigger(Component couldIncrease, Component from) {
        Dimension dim = couldIncrease.getPreferredSize();
        //increase width
        if(eastOrWest && from.getPreferredSize().width > dim.width) {
            couldIncrease.setPreferredSize(
                    new Dimension(from.getPreferredSize().width + 5, dim.height));
        }
        
        if(!eastOrWest && from.getPreferredSize().height > dim.height) {
            couldIncrease.setPreferredSize(
                    new Dimension(dim.width, from.getPreferredSize().height + 5));
        }
    }
    
    public Component getComponent() {
        return mainPanel;
    }
    
    public boolean consume(MouseEvent me) {
        if(!(me.getWhen() - lastMove > 300)) {
            lastMove = me.getWhen();
            return false;
        }
        return true;
    }
    
    public GPlotPanel getPlotPanel() {
        return plotter;
    }
}
