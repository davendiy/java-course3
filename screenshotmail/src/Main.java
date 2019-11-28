import com.smaxe.uv.na.WebcamFactory;
import com.smaxe.uv.na.webcam.IWebcam;

import java.util.List;
import java.util.Scanner;

/**
 * created: 10.11.2019
 *
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 *
 * The main classes, that configures all the threads for work.
 *
 * The Runnable Scheduler works always with some interval > 10 seconds. It makes the main work.
 * The auxiliary thread Configurator also works always. It reads the configFile and applies changes to Sceduler.
 */

public class Main {

    public static void main(String[] args){
        final List<IWebcam> webcams = WebcamFactory.getWebcams();
        System.out.println("Webcams:");
        System.out.println(webcams);
        Scanner in = new Scanner(System.in);
        int index = -1;
        while (index < 0 || index >= webcams.size()) {
            System.out.print("Please, choose the webcam [0 ... " + webcams.size() + "]\n--> ");
            index = in.nextInt();
        }
        IWebcam webcam = webcams.get(index);
        Scheduler scheduler = new Scheduler(webcam);
        Configurator configurator = new Configurator(scheduler, "config.properties", 20000);

        Thread configThread = new Thread(configurator);
        Thread schedThread = new Thread(scheduler);

        configThread.start();
        schedThread.start();

    }

}
