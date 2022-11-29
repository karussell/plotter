package de.genvlin.gui.plot;

import de.genvlin.core.data.MainPool;
import de.genvlin.core.data.VectorInterface;
import de.genvlin.core.data.XYVectorInterface;

import javax.swing.*;

public class Demo {

    public static void main(String[] args) {
        LivePlotPanel panel = new LivePlotPanel();
        MainPool pool = MainPool.getDefault();
        XYVectorInterface data = pool.createXYVector(VectorInterface.class, VectorInterface.class);
        for (double i = 0; i < 10; i += .1) {
            data.add(i, Math.sin(i));
        }
        panel.getPlotPanel().addData(data);
        panel.getPlotPanel().automaticScaleAll();

        SwingUtilities.invokeLater(() -> {
            int frameHeight = 800;
            int frameWidth = 1200;
            JFrame frame = new JFrame("Plotter UI - Fast&Ugly");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(frameWidth, frameHeight);
            frame.add(panel.getComponent());
            frame.setVisible(true);
        });
    }
}
