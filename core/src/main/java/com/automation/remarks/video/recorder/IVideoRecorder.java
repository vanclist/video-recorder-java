package com.automation.remarks.video.recorder;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by sergey on 4/30/16.
 */
public interface IVideoRecorder {
    void start();

    File stopAndSave(String filename);
}
