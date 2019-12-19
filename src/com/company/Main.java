package com.company;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Main extends JFrame
{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private JFileChooser fileChooser = null;

    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;

    private GraphicsDisplay display = new GraphicsDisplay();

    private boolean fileLoaded = false;

    public Main()
    {
        super("Построение графиков функций на основе заранее подготовленных файлов");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);
        setExtendedState(MAXIMIZED_BOTH);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        Action openGraphicsAction = new AbstractAction("Открыть файл") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser == null)
                {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showOpenDialog(Main.this) == JFileChooser.APPROVE_OPTION)
                {
                    openGraphics(fileChooser.getSelectedFile());
                }
            }
        };
        fileMenu.add(openGraphicsAction);

        JMenu graphicsMenu = new JMenu("График");
        menuBar.add(graphicsMenu);

        Action showAxisAction = new AbstractAction("Показывать оси координат") {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setSelected(true);

        Action showMarkersAction = new AbstractAction("Показывать маркеры точек") {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
        showMarkersMenuItem.setSelected(true);

        graphicsMenu.addMenuListener(new GraphicsMenuListener());

        getContentPane().add(display, BorderLayout.CENTER);
    }

    protected void openGraphics(File selectedFile)
    {
        try
        {
            DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
            ArrayList graphicsData = new ArrayList(50);
            while (in.available() > 0) {
                Double x = Double.valueOf(in.readDouble());
                Double y = Double.valueOf(in.readDouble());
                graphicsData.add(new Double[] { x, y });
            }
            if (graphicsData.size() > 0) {
                fileLoaded = true;
                //resetGraphicsMenuItem.setEnabled(true);
                display.showGraphics(graphicsData);
            }
        }
        catch(FileNotFoundException ex)
        {
            JOptionPane.showMessageDialog(Main.this, "Указанный файл не найден", "Ошибка загрузки данных ", JOptionPane.WARNING_MESSAGE);
            return;
        }
        catch(IOException ex)
        {
            JOptionPane.showMessageDialog(Main.this, "Ошибка чтения координат точек из файла", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

    public static void main(String[] args)
    {
        /*try(DataOutputStream dos = new DataOutputStream(new FileOutputStream("x")))
        {
            Double[] a = {-5.1234, -5.6789, -4.789, -4.123, -3.456, -3.456, -2.0, -2.0, 0.0, 0.3, 1.234, 1.234, 2.0, 2.3, 3.0, 3.0};
            for(int i = 0; i < a.length; i++)
            {
                dos.writeDouble(a[i]);
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }*/

        Main frame = new Main();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private class GraphicsMenuListener implements MenuListener
    {

        @Override
        public void menuSelected(MenuEvent e) {
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
        }

        @Override
        public void menuDeselected(MenuEvent e) {

        }

        @Override
        public void menuCanceled(MenuEvent e) {

        }
    }
}