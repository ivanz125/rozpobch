package com.ivanzhur;

import javax.swing.*;

public class SliderSetTask implements Runnable {

    private JSlider slider;
    private int value;
    private boolean finished;

    SliderSetTask(JSlider slider, int value) {
        this.slider = slider;
        this.value = value;
    }

    public void stop() {
        finished = true;
    }

    private synchronized void setSliderValue() {
        slider.setValue(value);
    }

    @Override
    public void run() {
        while (!finished) {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException ex) {
                return;
            }
            setSliderValue();
        }
    }
}
