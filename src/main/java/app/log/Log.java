package app.log;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Thread responsible for storing logs in files.
 */
public class Log extends Thread {
    private static Log instance;
    private Semaphore semaphore = new Semaphore(0);
    private boolean running = true;
    private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();;
    private FileWriter writer;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");

    //#region Singleton
    private Log() {
        super();
        setPriority(MIN_PRIORITY);
        try {
            Files.createDirectories(Paths.get("data"));
            File file = new File("data/debug.log");
            file.createNewFile();
            file.setWritable(true);
            this.writer = new FileWriter(file);
            this.start();
        } catch (Exception e) {};
    };

    public static Log getInstance() {
        if(Log.instance == null) Log.instance = new Log();
        return Log.instance;
    };
    //#endregion

    /**
     * Verify if the thread is busy.
     * If not, start processing new tasks.
     */
    public void inspect() {
        if(this.semaphore.availablePermits() == 0) this.semaphore.release();
    };

    //#region Core
    /**
     * Request the thread to store a message.
     * @param message message to be stored
     */
    public static synchronized void print(String message) {
        Log.print("Main",  message);
    };

    /**
     * Request the thread to store a message.
     * @param sender sender of the message
     * @param message message to be stored
     */
    public static synchronized void print(String sender, String message) {
        Log log = Log.getInstance();
        log.put(sender, message);
    };

    /**
     * Middleware to format all messages before 
     * requesting the thread to store them.
     * @param sender sender of the message
     * @param message message to be stored
     */
    private void put(String sender, String message) {
        this.queue.add("[" + this.formatter.format(LocalDateTime.now()) + "]:[" + sender + "] " + message);
        this.inspect();
    };

    /**
     * Run the thread, storing the logs in the file.
     */
    @Override
    public void run() {
        while(running || !queue.isEmpty()) {
            try {
                // Wait for tasks
                if(queue.isEmpty()) {
                    this.semaphore.acquire();
                };

                // Store logs
                while(!queue.isEmpty()) {
                    String log = queue.poll();
                    this.writer.write(log + "\n");
                };

                this.writer.flush();
            } catch (Exception e) {};
        };
    };
    //#endregion

    //#region Control
    /**
     * Finish the thread safely.
     */
    public static void finish() {
        try {
            if(Log.instance != null) {
                Log.instance.running = false;
                Log.instance.inspect();
                Log.instance.join();
            };
        } catch (InterruptedException e) {};
    };

    /**
     * Start the thread.
     */
    @Override
    public void start() {
        try {
            super.start();
        } catch (IllegalThreadStateException e) {
        };
    };
    //#endregion
};