package com.ivanzhur;

import javax.swing.*;
import java.awt.*;

public class SemaphoreForm extends JFrame {
    private JPanel rootPanel;
    private JButton start1Button;
    private JButton stop2Button;
    private JButton start2Button;
    private JButton stop1Button;
    private JSlider slider;

    private Thread thread1;
    private Thread thread2;
    private SliderSetTask runnable1;
    private SliderSetTask runnable2;

    private volatile int semaphore;

    public static void main(String[] args) {
        new SemaphoreForm();
    }

    public SemaphoreForm() {
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

        stop1Button.setEnabled(false);
        stop2Button.setEnabled(false);

        start1Button.addActionListener(e -> {
            if (semaphore != 0) {
                System.out.println("Interface occupied with thread 2");
                return;
            }
            semaphore = 1;
            start1Button.setEnabled(false);
            stop1Button.setEnabled(true);
            startThread1();
        });

        start2Button.addActionListener(e -> {
            if (semaphore != 0) {
                System.out.println("Interface occupied with thread 1");
                return;
            }
            semaphore = 1;
            start2Button.setEnabled(false);
            stop2Button.setEnabled(true);
            startThread2();
        });

        stop1Button.addActionListener(e -> {
            runnable1.stop();
            start1Button.setEnabled(true);
            stop1Button.setEnabled(false);
            semaphore = 0;
        });

        stop2Button.addActionListener(e -> {
            runnable2.stop();
            start2Button.setEnabled(true);
            stop2Button.setEnabled(false);
        });
    }

    private void startThread1() {
        thread1 = new Thread(runnable1);
        thread1.setPriority(Thread.MIN_PRIORITY);
        thread1.start();
    }

    private void startThread2() {
        thread2 = new Thread(runnable2);
        thread2.setPriority(Thread.MIN_PRIORITY);
        thread2.start();
    }
}
