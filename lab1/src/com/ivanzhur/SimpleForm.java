package com.ivanzhur;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SimpleForm extends JFrame {
    private JPanel rootPanel;
    private JSlider slider;
    private JButton stopButton;
    private JButton startButton;
    private JSpinner spinner1;
    private JSpinner spinner2;

    private Thread thread1;
    private Thread thread2;
    private SliderSetTask runnable1;
    private SliderSetTask runnable2;

    public static void main(String[] args) {
        new SimpleForm();
    }

    public SimpleForm() {
        setContentPane(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 300));
        setResizable(false);
        setVisible(true);
        pack();

        runnable1 = new SliderSetTask(slider, 10);
        runnable2 = new SliderSetTask(slider, 90);
        initializeInterface();
    }

    private void initializeInterface() {
        slider.setMinimum(0);
        slider.setMaximum(100);
        slider.setValue(50);
        java.util.List<Integer> priorities = new ArrayList<>();
        for (int i = Thread.MIN_PRIORITY; i <= Thread.MAX_PRIORITY; i++) priorities.add(i);
        spinner1.setModel(new SpinnerListModel(priorities));
        spinner2.setModel(new SpinnerListModel(priorities));
        stopButton.setEnabled(false);

        // Threads priority
        spinner1.addChangeListener(e -> thread1.setPriority((Integer) spinner1.getValue()));
        spinner2.addChangeListener(e -> thread2.setPriority((Integer) spinner2.getValue()));

        // Start button
        startButton.addActionListener(e -> {
            createThreads();
            thread1.start();
            thread2.start();
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        });

        // Stop button
        stopButton.addActionListener(e -> {
            runnable1.stop();
            runnable2.stop();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        });
    }

    private void createThreads() {
        thread1 = new Thread(runnable1);
        thread2 = new Thread(runnable2);
        thread1.setPriority((Integer) spinner1.getValue());
        thread2.setPriority((Integer) spinner2.getValue());
    }
}
