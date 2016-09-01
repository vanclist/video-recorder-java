package com.automation.remarks.video.recorder.monte;

import com.automation.remarks.video.exception.RecordingException;
import com.automation.remarks.video.recorder.VideoConfiguration;
import com.automation.remarks.video.recorder.VideoRecorder;
import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.math.Rational;

import java.awt.*;
import java.io.File;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MediaType;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.*;

/**
 * Created by sergey on 13.04.16.
 */
public class MonteRecorder extends VideoRecorder {

    private MonteScreenRecorder screenRecorder;
    private VideoConfiguration videoConfiguration;

    public MonteRecorder() {
        this.videoConfiguration = conf();
        this.screenRecorder = getScreenRecorder();
    }

    public void start() {
        if (videoConfiguration.isVideoEnabled()) {
            screenRecorder.start();
        }
    }

    public File stopAndSave(String filename) {
        File video = null;
        if (videoConfiguration.isVideoEnabled()) {
             video = writeVideo(filename);
        }
        setLastVideo(video);
        return video;
    }

    private File writeVideo(String filename){
        try {
            return screenRecorder.saveAs(filename);
        } catch (IndexOutOfBoundsException ex) {
            throw new RecordingException("Video recording wasn't started");
        }
    }

    private GraphicsConfiguration getGraphicConfig() {
        return GraphicsEnvironment
                .getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
    }

    private MonteScreenRecorder getScreenRecorder() {
        Format fileFormat = new Format(MediaTypeKey, MediaType.VIDEO, MimeTypeKey, FormatKeys.MIME_AVI);
        Format screenFormat = new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey,
                ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                QualityKey, 1.0f,
                KeyFrameIntervalKey, 15 * 60);
        Format mouseFormat = new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
                FrameRateKey, Rational.valueOf(30));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        Rectangle captureSize = new Rectangle(0, 0, width, height);

        return MonteScreenRecorderBuilder
                .builder()
                .setGraphicConfig(getGraphicConfig())
                .setRectangle(captureSize)
                .setFileFormat(fileFormat)
                .setScreenFormat(screenFormat)
                .setFolder(videoConfiguration.getVideoFolder())
                .setMouseFormat(mouseFormat).build();
    }
}