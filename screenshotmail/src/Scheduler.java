import com.smaxe.uv.na.webcam.IWebcam;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * created: 10.11.2019
 *
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 *
 *
 * Here is class that implements mainloop ouf the program:
 *      1. Make a screenShot (and save it to respective directory);
 *      2. Make a snapShot   (and save it to respective directory);
 *      3. Send both files to the given receiver;
 *      4. Sleep on the given time.
 */

public class Scheduler implements Runnable {

    // constants
    private String screenShotFileName = "screenshots/screen";
    private String snapShotFileName = "snapshots/cam";
    private IWebcam webcam;
    private String receiver = "davendiy@gmail.com";
    private String messageText = "Testing sending";
    private String messageSubject = "Testing";
    private int time = 40000;

    // object that provides the successful finish of one step of the loop
    // avoiding problems with data integrity
    public final Object lock = new Object();

    Scheduler(IWebcam webcam) {
        this.webcam = webcam;
    }


    // ---------------------------------------Getters-------------------------------------------------------------------
    public String getReceiver() {
        return receiver;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getScreenShotFileName() {
        return screenShotFileName;
    }

    public String getSnapShotFileName() {
        return snapShotFileName;
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public int getTime() {
        return time;
    }


    // -------------------------------Setters with exceptions-----------------------------------------------------------
    public void setMessageText(String new_value) {
        // text of message could be anything
        System.out.println("[!!] Set new value for messageText: " + new_value);
        this.messageText = new_value;
    }

    public void setMessageSubject(String new_value) {
        // subject of message could be anything
        System.out.println("[!!] Set new value for messageSubject: " + new_value);
        this.messageSubject = new_value;
    }

    public void setTime(int new_value) throws IllegalArgumentException {

        // Time must be greater than 10 seconds in order to avoid ban from gmail
        if (new_value < 10000) {
            throw new IllegalArgumentException("Very small value for time. Use > 10 secs instead (10000 msecs).");
        }
        System.out.println("[!!] Set new value for time of sleeping: " + new_value);
        this.time = new_value;
    }

    public void setReceiver(String new_value) throws IllegalArgumentException {

        // email should be correct
        try {
            InternetAddress tmp = new InternetAddress(new_value);
            tmp.validate();
        } catch (AddressException ex) {
            throw new IllegalArgumentException("Invalid email: " + new_value);
        }
        System.out.println("[!!] Set new value for receiver: " + new_value);
        receiver = new_value;
    }

    public void setScreenShotFileName(String new_value) throws IllegalArgumentException {

        // path for files should be correct (specified directories should exist)
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            LocalDateTime now = LocalDateTime.now();
            String test = new_value + dtf.format(now) + ".png";   // try to create similar to screenshots file
            File testFile = new File(test);
            testFile.createNewFile();
        } catch (IOException e) {
            throw new IllegalArgumentException("Bad path for screenshots: " + new_value);
        }
        System.out.println("[!!] Set new value for screenShotFileName: " + new_value);
        screenShotFileName = new_value;
    }

    public void setSnapShotFileName(String new_value) throws IllegalArgumentException {

        // path for files should be correct (specified directories should exist)
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            LocalDateTime now = LocalDateTime.now();
            String test = new_value + dtf.format(now) + ".png";  // try to create similar to snapshots file
            File testFile = new File(test);
            testFile.createNewFile();
        } catch (IOException e) {
            throw new IllegalArgumentException("Bad path for snapshots: " + new_value);
        }
        System.out.println("[!!] Set new value for snapShotFileName: " + new_value);
        snapShotFileName = new_value;
    }

    @Override
    public void run() {
        while (true) {

            // do one step with locking in order to keep all specified parameters without changing
            synchronized (lock) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
                LocalDateTime now = LocalDateTime.now();

                System.out.println("[*] " + dtf.format(now) + ". Starting...");

                String resScreenShotName = screenShotFileName + dtf.format(now) + ".png";
                String resSnapShotName = snapShotFileName + dtf.format(now) + ".png";

                String[] names = new String[2];
                names[0] = resScreenShotName;
                names[1] = resSnapShotName;
                try {
                    System.out.println("[*] Making the screenshot...");
                    Catcher.makeScreenShot(resScreenShotName);
                    System.out.println("[*] Screenshot successfully saved to " + resScreenShotName + ".");

                    System.out.println("[*] Making the snapshot...");
                    Catcher.makeSnapShot(webcam, resSnapShotName);
                    System.out.println("[*] Snapshot successfully saved to " + resSnapShotName + '.');

                    System.out.println("[-->] Sending the files...");
                    Sender.send(names, receiver, messageSubject, messageText);
                    System.out.println("[*] Files successfully sent.");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("[*] Sleeping....");
            }
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}