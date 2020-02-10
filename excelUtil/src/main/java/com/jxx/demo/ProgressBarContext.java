package com.jxx.demo;

import javafx.scene.control.ProgressBar;

/**
 * @author lingyun.jiang
 */
public class ProgressBarContext {

    private ProgressBarContext(){}

    private static ProgressBar progressBar = new ProgressBar(0);

    public static ProgressBar getInstance(){
        return progressBar;
    }

}
