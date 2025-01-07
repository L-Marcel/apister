package app.log;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        } catch(Exception e) {};
    };

    public static Log getInstance() {
        if(Log.instance == null) Log.instance = new Log();
        return Log.instance;
    };
    //#endregion

    public void inspect() {
        if(this.semaphore.availablePermits() == 0) this.semaphore.release();
    };

    //#region Core
    public static synchronized void print(String message) {
        Log.print("Main",  message);
    };

    public static synchronized void print(String sender, String message) {
        Log log = Log.getInstance();
        log.put(sender, message);
    };

    private void put(String sender, String message) {
        this.queue.add("[" + this.formatter.format(LocalDateTime.now()) + "]:[" + sender + "] " + message);
        this.inspect();
    };

    @Override
    public void run() {
        while(running || !queue.isEmpty()) {
            try {
                if(queue.isEmpty()) {
                    this.semaphore.acquire();
                };

                while(!queue.isEmpty()) {
                    String log = queue.poll();
                    this.writer.write(log + "\n");
                };

                this.writer.flush();
            } catch(Exception e) {};
        };
    };
    //#endregion

    //#region Control
    public static void finish() {
        try {
            if(Log.instance != null) {
                Log.instance.running = false;
                Log.instance.inspect();
                Log.instance.join();
            };
        } catch(InterruptedException e) {};
    };

    @Override
    public void start() {
        try {
            super.start();
        } catch(IllegalThreadStateException e) {
        };
    };
    //#endregion
};