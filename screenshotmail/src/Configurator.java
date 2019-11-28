import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * created: 10.11.2019
 *
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 *
 * Class that implements observer for a config file.
 * It reads given file with some interval and if there are some changes -
 * applies them to working Scheduler.
 */

public class Configurator implements Runnable{

    private String configFilename;  // configFile
    private Scheduler mainRunner;
    private Properties properties;  // values of parameters

    private int sleepTime;         // interval of checking

    // Parameters that we observe.
    private String receiverParam;
    private int timeParam;
    private String messageTextParam;
    private String messageSubjectParam;
    private String screenShotFileNameParam;
    private String snapShotFileNameParam;

    Configurator(Scheduler mainRunner, String configFilename, int sleepTime){
        this.mainRunner = mainRunner;
        this.configFilename = configFilename;
        this.sleepTime = sleepTime;

        // default configuration.
        receiverParam = mainRunner.getReceiver();
        timeParam = mainRunner.getTime();
        messageTextParam = mainRunner.getMessageText();
        messageSubjectParam = mainRunner.getMessageSubject();
        screenShotFileNameParam = mainRunner.getScreenShotFileName();
        snapShotFileNameParam = mainRunner.getSnapShotFileName();
    }

    /**
     * Read all the properties from configFile and if there are some changes - apply them.
     */
    private void makeChanges(){

        // read all the parameters from configFile and check correctness of types
        String receiverParamNext = properties.getProperty("receiver");
        int timeParamNext;
        try {
            timeParamNext = Integer.parseInt(properties.getProperty("time"));
        } catch (NumberFormatException e){
            System.out.println("ERROR: " + "Time must be integer number.");
            timeParamNext = timeParam;
        }

        String messageTextParamNext = properties.getProperty("messageText");
        String messageSubjectParamNext = properties.getProperty("messageSubject");
        String screenShotFileNameParamNext = properties.getProperty("screenShotFileName");
        String snapShotFileNameParamNext = properties.getProperty("snapShotFileName");

        // check the equality between captured and preset parameters
        // and handle all possible exceptions
        if ( !(receiverParam.equals(receiverParamNext)) ){
            try {
                mainRunner.setReceiver(receiverParamNext);
                receiverParam = receiverParamNext;
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }

        }
        if ( timeParam != timeParamNext ){
            try {
                mainRunner.setTime(timeParamNext);
                timeParam = timeParamNext;
            } catch (IllegalArgumentException e){
                System.out.println("ERROR: " + e.getMessage());
            }
        }
        if ( !(messageTextParam.equals(messageTextParamNext)) ){
            try {
                mainRunner.setMessageText(messageTextParamNext);
                messageTextParam = messageTextParamNext;
            } catch (IllegalArgumentException e){
                System.out.println("ERROR: " + e.getMessage());
            }

        }
        if ( !(messageSubjectParam.equals(messageSubjectParamNext)) ){
            try {
                mainRunner.setMessageSubject(messageSubjectParamNext);
                messageSubjectParam = messageSubjectParamNext;
            } catch (IllegalArgumentException e){
                System.out.println("ERROR: " + e.getMessage());
            }

        }
        if ( !(screenShotFileNameParam.equals(screenShotFileNameParamNext)) ) {
            try{
                mainRunner.setScreenShotFileName(screenShotFileNameParamNext);
                screenShotFileNameParam = screenShotFileNameParamNext;
            } catch (IllegalArgumentException e){
                System.out.println("ERROR: " + e.getMessage());
            }

        }
        if ( !(snapShotFileNameParam.equals(snapShotFileNameParamNext)) ){
            try{
                mainRunner.setSnapShotFileName(snapShotFileNameParamNext);
                snapShotFileNameParam = snapShotFileNameParamNext;
            } catch (IllegalArgumentException e){
                System.out.println("ERROR: " + e.getMessage());
            }

        }
    }

    @Override
    public void run(){
        while (true){
            // apply changes with lock in order to avoid the problems with Scheduler
            synchronized (mainRunner.lock){
                System.out.println("[!!] Starting to update configurations.");
                try (FileInputStream inputStream = new FileInputStream(configFilename)){
                    properties = new Properties();
                    properties.load(inputStream);
                    makeChanges();
                } catch (IOException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            }
            try {
                System.out.println("[!!] Configurations updated. Sleeping...");
                Thread.sleep(sleepTime);
            } catch (InterruptedException exp){
                break;
            }
        }
    }

}
