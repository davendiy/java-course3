import com.smaxe.uv.media.java.video.VideoFrameFactory;
import com.smaxe.uv.na.WebcamFactory;
import com.smaxe.uv.na.webcam.IWebcam;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;

import java.io.FileOutputStream;

import java.awt.Robot;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * created: 10.11.2019
 *
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 *
 * Class that realize tool for making screenshots and snapshots by webcam.
 *
 * The screenshot maker doesn't work on linux system with not-configured XServer.
 */

public class Catcher {

    /**
     * Test function.
     */
    public static void main(String[] args){
        try {
            makeScreenShot("test/test4-23.png");
            final List<IWebcam> webcams = WebcamFactory.getWebcams();
            makeSnapShot(webcams.get(0), "test/test2.png");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Make screenshot and save it with given name.
     * I used the Robot from java.awt.
     *
     * This method doesn't work on linux with non-configured XServer.
     *
     * @param filename    - name of file where the screenshot will be saved
     * @throws Exception  - throws if there are some problems inside of builtin tools or
     *                      program can't create a file.
     */
    static void makeScreenShot(String filename) throws Exception{
        boolean res = true;

        // Make the frame with bounds same to device's display size.
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();

        // go through all the screens of device
        Rectangle allScreenBounds = new Rectangle();
        for (GraphicsDevice screen : screens) {
            Rectangle screenBounds = screen.getDefaultConfiguration().getBounds();
            allScreenBounds.width += screenBounds.width;
            allScreenBounds.height = Math.max(allScreenBounds.height, screenBounds.height);
            allScreenBounds.x=Math.min(allScreenBounds.x, screenBounds.x);
            allScreenBounds.y=Math.min(allScreenBounds.y, screenBounds.y);
        }

        // make the image of created frame
        Robot robot = new Robot();
        BufferedImage bufferedImage = robot.createScreenCapture(allScreenBounds);

        // save the image to the file.
        File file = new File(filename);
        if(!file.exists())
            res = file.createNewFile();
        if (res) {
            FileOutputStream fos = new FileOutputStream(file);
            ImageIO.write(bufferedImage, "png", fos);
        }else {
            throw new Exception("Can't create new file. Check your permissions");
        }
    }

    /**
     * Make snapShoot and save it to the given file.
     *
     * I used com.smaxe.uv.na.webcam.IWebcam.
     * This tool allows to record the video from webcam. So we just need to capture one of
     * frames of this video.
     *
     * @param webcam      - one of available cameras that user should choose
     * @param filename    - name of file where the snapShot will be saved
     * @throws Exception  - throws if there are some problems with camera.
     */
    static void makeSnapShot(IWebcam webcam, String filename) throws Exception{

        // Create the bounds of captured image
        IWebcam.FrameFormat tmp = new IWebcam.FrameFormat(320, 240);

        // event for reporting
        AtomicBoolean done = new AtomicBoolean(false);

        // Listener that handle captured video frames. We need for only one.
        IWebcam.IListener listener = videoFrame -> {
            File file = new File(filename);
            try {
//                System.out.println("Captured");
                done.set(true);
                VideoFrameFactory.saveAsJpg(file, videoFrame);
                webcam.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // Start recording of video and configure listener.
        webcam.open(tmp, listener);
        webcam.startCapture();
        while (!done.get()){
            Thread.sleep(10);
        }
//        System.out.println("Done.");
    }
}
