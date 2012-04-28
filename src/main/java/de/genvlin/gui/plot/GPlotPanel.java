/*
 * GPlotPanel.java
 *
 * Created on 27. Juni 2004, 00:12
 * This stands under Public domain
 */
package de.genvlin.gui.plot;

import de.genvlin.core.data.CollectionEvent;
import de.genvlin.core.data.CollectionListener;
import de.genvlin.core.data.ID;
import de.genvlin.core.data.MainPool;
import de.genvlin.core.data.Pool;
import de.genvlin.core.data.VectorInterface;
import de.genvlin.core.data.XYDataInterface;
import de.genvlin.core.data.XYVectorInterface;
import de.genvlin.gui.util.ComponentContainer;
import de.genvlin.gui.util.GMouseAdapter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

/**
 * The main class for plotting the data in an extra panel. It should be a very simple plot class
 * with no extras.
 *
 * @author Peter Karich
 */
public class GPlotPanel implements CollectionListener, ComponentContainer {

    private static final long serialVersionUID = 432233244L;
    private static final int DO_NOT_SCALE = -1, SCALE_ALL = -2;
    private double xTranslate = 1, yTranslate = 1;
    private int xOff, yOff;
    private int automaticScaleDataNo = DO_NOT_SCALE;
    private Color axesColor;
    private List<Color> dataColor;
    private CoordinateSystem defaultCS;
    private CoordinateSystem cSys;
    private PlotML plotML;
    private List<Component> popMenuList;
    private Pool<XYDataInterface> pool;
    private JPanel panel;
    private int tmp, x1, x2, y1, y2;
    private int plotOne = -1;

    public GPlotPanel(Pool<XYDataInterface> p) {
        if (p == null) {
            throw new UnsupportedOperationException("param pool shouldn't be null!");
        }
        pool = p;
        reloadPureXYInterfaceToXYData(0, getPool().size());
        getPool().addVectorListener(this);

        popMenuList = new ArrayList<Component>();
        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(new JLabel("<html>"
                + "Measure Distance With Mouse-Dragging.<br>"
                + "Zoom With SHIFT + Drag.<br>"
                + "Translate With CTRL + Drag.</html>"));

        popMenuList.add(helpMenu);

        panel = new JPanel() {

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintPlot(g);
            }
        };
        panel.setName("plot:" + getPool().getID());
        panel.setMinimumSize(new Dimension(300, 300));
        // if (!RepaintManager.currentManager(panel).isDoubleBufferingEnabled())

        initColors();
        cSys = getDefaultCoordinateSystem();

        ColorUIResource cr = (ColorUIResource) UIManager.get("Label.foreground");
        if (cr == null) {
            //black theme
            axesColor = new Color(51, 51, 51);
        } else {
            //white theme
            axesColor = new Color(cr.getRGB());
        }

        cSys.setColor(axesColor);
        plotML = new PlotML();
        panel.addMouseListener(plotML);
        panel.addMouseMotionListener(plotML);

        //This avoids null pointer exceptions, in methods which uses autoscale
        //or other methods depending on window bounds.
        cSys.setWinBounds(panel.getBounds());
    }

    public GPlotPanel() {
        this(MainPool.getDefault().createPool(XYDataInterface.class));
    }

    protected void initColors() {
        //30 should be sufficient
        dataColor = new ArrayList<Color>(30);
        //dataColor.add(Color.BLACK);
        //dataColor.add(Color.WHITE);
        dataColor.add(Color.BLUE.brighter());
        dataColor.add(Color.RED);
        dataColor.add(Color.ORANGE);
        dataColor.add(Color.GREEN.darker());
        dataColor.add(Color.PINK);
        dataColor.add(Color.MAGENTA);
        dataColor.add(Color.CYAN);
        dataColor.add(Color.YELLOW.darker());
        dataColor.add(Color.GRAY);
        int size = dataColor.size();

        for (int i = 0; i < size; i++) {
            dataColor.add(dataColor.get(i).darker());
        }

        for (int i = 0; i < size; i++) {
            dataColor.add(dataColor.get(i).brighter());
        }
    }

    public final Pool<XYDataInterface> getPool() {
        return pool;
    }

    protected void paintPlot(Graphics g) {
        cSys.setWinBounds(panel.getBounds());

        //now automatic scaling for only one time!!
        _automaticScale(automaticScaleDataNo);
        automaticScaleDataNo = DO_NOT_SCALE;

        cSys.drawAxes(g);
        cSys.drawMousePosition(g, plotML.currPoint);

        //plotFunction(g);
        plotData(g);

        if (plotML.isDistancing()) {
            cSys.drawMouseDistance(g, plotML.distPoint, plotML.currPoint);
        }

        if (plotML.isZooming()) {
            x1 = plotML.zoomPoint.x;
            y1 = plotML.zoomPoint.y;
            x2 = plotML.currPoint.x;
            y2 = plotML.currPoint.y;
            if (x2 < x1) {
                tmp = x1;
                x1 = x2;
                x2 = tmp;
            }
            if (y2 < y1) {
                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            g.setColor(axesColor);
            g.drawRect(x1, y1, x2 - x1, y2 - y1);
        }
    }

    /**
     * @return the standard coordinate bounds of the real coordinate and set the locale to default
     * locale
     */
    public CoordinateSystem getDefaultCoordinateSystem() {
        defaultCS = new CoordinateSystem(+10, +30, 0.5, 0.5);
        return defaultCS;
    }

    /**
     * This method sets the locale (=> and the numberformat).
     */
    public void setAxesNumberFormat(Locale locale, int maxDigits) {
        cSys.setAxesNumberFormat(locale, maxDigits);
    }

    public void clearColor() {
        dataColor.clear();
    }

    public void addColor(Color color) {
        dataColor.add(color);
    }
    int colorDataIndex = 0;

    /**
     * Which dataset has which color??
     */
    public Color getColor(int index) {
        return getPool().get(index).getColor();
    }

    public void setPlotOne(int index) {
        plotOne = index;
    }

    /**
     * This method adds specified data to panel
     */
    public void addData(VectorInterface xVector, VectorInterface yVector) {
        initXYData(MainPool.getDefault().createXYData(xVector, yVector));
    }

    /**
     * This method adds specified xVector as x values and yVector as y values to the panel.
     *
     * @param title specifies the titled of this xy plot.
     */
    public void addData(VectorInterface xVector, VectorInterface yVector, String title) {
        XYDataInterface xyData = MainPool.getDefault().createXYData(xVector, yVector);
        xyData.setTitle(title);
        initXYData(xyData);
    }

    /**
     * This method adds specified data to panel
     */
    public void addData(XYVectorInterface xyVector) {
        initXYData(MainPool.getDefault().createXYData(xyVector));
    }

    /**
     * This method adds specified data to panel
     */
    public void addData(XYDataInterface xyData) {
        initXYData(xyData);
    }

    protected void initXYData(XYDataInterface xyData) {
        if (xyData.getTitle() == null || xyData.getTitle().length() == 0) {
            xyData.setTitle("Data:" + getPool().size());
        }
        int color = colorDataIndex;
        automaticOneScale(colorDataIndex);
        color %= dataColor.size();
        xyData.setColor(dataColor.get(color));
        colorDataIndex++;
        getPool().add(xyData);
    }

    public XYDataInterface getData(int index) {
        return getPool().get(index);
    }

    public XYDataInterface getData(String str) {
        int index = indexOf(str);
        if (index >= 0) {
            return getPool().get(index);
        }

        return null;
    }

    /**
     * This method removes the specified plot from this plotwindow.
     */
    public void hideData(String str) {
        int i = indexOf(str);
        if (i > -1) {
            XYDataInterface xyData = (XYDataInterface) getPool().get(i);
            xyData.setHidden(!xyData.isHidden());
        }
    }

    /**
     * This method removes an entire plot from this plotwindow.
     */
    public void removeData(int i) {
        getPool().remove(i);
    }

    /**
     * This method removes the specified plot from this plotwindow.
     */
    public void removeData(String str) {
        int i = indexOf(str);
        if (i > -1) {
            removeData(i);
        }
    }

    /**
     * This method removes the points from a plot.
     */
    public void clearData(int i) {
        if (i < getPool().size()) {
            getPool().get(i).clear();
        }
    }

    /**
     * This method removes all data sets from plot panel.
     */
    public void clear() {
        getPool().clear();
    }

    /**
     * This method plots all datasets to the window.
     */
    protected void plotData(Graphics g) {
        xOff = cSys.getYAxis().getXOffset();
        yOff = cSys.getXAxis().getYOffset();
        g.setClip(xOff, yOff, cSys.winWidth() - 2 * xOff, cSys.winHeight() - 2 * yOff);
        XYDataInterface xyData;

        if (plotOne >= 0) {
            xyData = (XYDataInterface) getPool().get(plotOne);
            xyData.draw(g, cSys);
        } else
            for (int c = 0; c < getPool().size(); c++) {
                xyData = (XYDataInterface) getPool().get(c);
                xyData.draw(g, cSys);
            }

        //reset clipping area:
        g.setClip(0, 0, cSys.winWidth(), cSys.winHeight());
    }

    /**
     * This method sets the mouse translate factor
     */
    public void setTranslateFactor(double xFactor, double yFactor) {
        xTranslate = yFactor;
        yTranslate = yFactor;
    }

    protected int indexOf(String str) {
        if (str != null) {
            for (int i = 0; i < getPool().size(); i++) {
                if (str.equals(getPool().get(i).getTitle())) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean automaticScale(String subString) {
        return _automaticScale(indexOf(subString));
    }

    /**
     * This method scales the window so that we can see the specified data (in the most cases) very
     * good.
     *
     * protected boolean automaticScale(int index) { if(_automaticScale(index)) { repaint(); return
     * true; }
     *
     * return false; }
     */
    private boolean _automaticScale(int index) {
        if (getPool().size() == 0 || index >= getPool().size()) {
            return false;
        }

        if (index == SCALE_ALL) {
            double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
            double minY = Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;

            for (int i = 0; i < getPool().size(); i++) {
                if (((XYDataInterface) getPool().get(i)).isHidden()) {
                    continue;
                }

                if (minX > getPool().get(i).getMinX().doubleValue()) {
                    minX = getPool().get(i).getMinX().doubleValue();
                }

                if (maxX < getPool().get(i).getMaxX().doubleValue()) {
                    maxX = getPool().get(i).getMaxX().doubleValue();
                }

                if (minY > getPool().get(i).getMinY().doubleValue()) {
                    minY = getPool().get(i).getMinY().doubleValue();
                }

                if (maxY < getPool().get(i).getMaxY().doubleValue()) {
                    maxY = getPool().get(i).getMaxY().doubleValue();
                }
            }

            cSys.automaticScale(minX, maxX, minY, maxY);
        } else if (index < 0) {
            return false;
        } else {
            XYDataInterface xyData = getPool().get(index);
            cSys.automaticScale(xyData.getMinX().doubleValue(), xyData.getMaxX().doubleValue(),
                    xyData.getMinY().doubleValue(), xyData.getMaxY().doubleValue());
        }

        return true;
    }

    /**
     * This method sets the data in this plotpanel which should scaled automatically for one time.
     * -1 indicates that no scaling will happen. This works only if panel has a size != 0. This
     * means you have to realize the component first.
     */
    public void automaticOneScale(int dataNo) {
        automaticScaleDataNo = dataNo;
    }

    /**
     * This method scales the plot window so that all datasets are visible.
     */
    public void automaticScaleAll() {
        //Workaround: to let autoscaling directly after importing datasets (nlo)
        //Sometimes we need to set bounds before autoscaling.
        //Only if pane.width > 0 and height > 0 cSys.automaticScale will work.
        automaticScaleDataNo = SCALE_ALL;
    }

    /**
     * The MouseListener provides distance measurement via dragging, zooming via drag+shift and
     * popupmenu displaying.
     */
    private class PlotML extends GMouseAdapter implements MouseMotionListener, Serializable {

        private static final int DISTANCE = 0, ZOOM = 1, NORMAL = 2;
        private int clickButton = -1;
        private Point changeVector = new Point();
        private Point clickPoint = new Point();
        private Point currPoint = new Point();
        private Point zoomPoint = new Point();
        private Point distPoint = new Point();
        private int state = NORMAL;
        double tmpX, tmpY, xP, yP;

        public boolean isZooming() {
            return state == ZOOM;
        }

        public boolean isDistancing() {
            return state == DISTANCE;
        }

        public boolean isNormal() {
            return state == NORMAL;
        }

        @Override
        public void gMousePressed(MouseEvent me) {
            clickPoint.x = me.getX();
            clickPoint.y = me.getY();
            clickButton = me.getButton();

            if (me.isShiftDown() && clickButton == me.BUTTON1) {
                zoomPoint.x = me.getX();
                zoomPoint.y = me.getY();

                state = ZOOM;
                cSys.setZoomStart(zoomPoint);
            } else {
                state = DISTANCE;

                distPoint.x = me.getX();
                distPoint.y = me.getY();
            }
        }

        @Override
        public void gMouseReleased(MouseEvent me) {
            if (isZooming()) {
                cSys.zoom(me.getX(), me.getY());
                repaint();
            }
            state = NORMAL;
        }

        public void mouseMoved(MouseEvent me) {
            //avoid to much repaint, because this would be a performance issue!
            if (!consume(me)) {
                currPoint.x = me.getX();
                currPoint.y = me.getY();
                repaint();
            }
        }

        public void mouseDragged(MouseEvent me) {
            currPoint.x = me.getX();
            currPoint.y = me.getY();
            changeVector.x = currPoint.x - clickPoint.x;
            changeVector.y = currPoint.y - clickPoint.y;

            //for the next call we need for zoom/translate
            clickPoint.x = currPoint.x;
            clickPoint.y = currPoint.y;

            if (me.isControlDown() && clickButton == MouseEvent.BUTTON1) {
                tmpX = -changeVector.x * xTranslate;
                tmpY = changeVector.y * yTranslate;

                cSys.translate(Math.round(tmpX) * 2, Math.round(tmpY) * 2);
                repaint();
            } else if (!isNormal()) {
                repaint();
            }
        }

        @Override
        public void showPopup(MouseEvent me) {
            JPopupMenu pm = getNewPopMenu();
            pm.show(me.getComponent(), me.getX(), me.getY());
        }
    }//PlotML

    public boolean consume(MouseEvent me) {
        return false;
    }

    public void repaint() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    panel.repaint();
                }
            });
        } else {
            panel.repaint();
        }
    }

    public Component getComponent() {
        return panel;
    }
    private static final String automaticScalingString = "Automatic Scaling";
    private static final String removeString = "Remove";
    private static final String hideString = "Hide/Show";

    private synchronized JPopupMenu getNewPopMenu() {
        /*
         * Why does this not work: create a static popMenu, add dynamic stuff like data-scaling? the
         * created static popMenu will be empty on a second call!?
         */
        JPopupMenu popMenu = new JPopupMenu();

        //clone
        for (Component comp : popMenuList) {
            popMenu.add(comp);
        }

        for (int i = 0; i < getPool().size(); i++) {
            final String str = getPool().get(i).getTitle();
            JMenu menu = new JMenu(str);
            menu.setForeground(getColor(i));

            final int fin = i;
            menu.addMouseListener(new MouseAdapter() {

                @Override public void mouseEntered(MouseEvent e) {
                    setPlotOne(fin);
                    repaint();
                }

                @Override public void mouseExited(MouseEvent e) {
                    setPlotOne(-1);
                    repaint();
                }
            });

            menu.add(new AbstractAction(automaticScalingString) {

                public void actionPerformed(ActionEvent e) {
                    automaticScale(str);
                    repaint();
                }
            });

            menu.add(new AbstractAction(hideString) {

                public void actionPerformed(ActionEvent e) {
                    hideData(str);
                    repaint();
                }
            });

            menu.add(new AbstractAction(removeString) {

                public void actionPerformed(ActionEvent e) {
                    removeData(str);
                    repaint();
                }
            });

            popMenu.add(menu);
        }

        //scale + remove for all datasets:
        JMenu menu = new JMenu("All");
        menu.add(new AbstractAction(automaticScalingString) {

            public void actionPerformed(ActionEvent e) {
                automaticScaleAll();
                repaint();
            }
        });

        menu.add(new AbstractAction(removeString) {

            public void actionPerformed(ActionEvent e) {
                clear();
                repaint();
            }
        });

        popMenu.add(menu);
        return popMenu;
    }

    /**
     * User can add here its menuitems.
     */
    public synchronized void addPopElement(Component comp) {
        popMenuList.add(comp);
    }

    /**
     * This method returns the mathmetical bounds of the current zoomed state of this plot panel.
     */
    public Rectangle2D.Double getZoomedRectangle() {
        return cSys.getMathBounds();
    }

    private boolean reloadPureXYInterfaceToXYData(int from, int to) {
        XYDataInterface data;
        getPool().removeVectorListener(this);
        for (int index = from; index < to; index++) {
            try {
                data = getPool().get(index);
            } catch (ClassCastException cce) {
                throw new RuntimeException(cce);
            }
            getPool().remove(index);
            addData(data);
        }
        getPool().addVectorListener(this);
        return true;
    }

    public void vectorChanged(CollectionEvent ve) {
        /*
         * if(ve.getProperty() == AbstractCollection.ADD_DATA || ve.getProperty() ==
         * AbstractCollection.ADD_SOME) { if(ve.getID()!=null) {
         * reloadPureXYInterfaceToXYData(ve.getID()); } else {
         * reloadPureXYInterfaceToXYData(ve.getFrom(), ve.getTo()); } }
         */
    }
}
