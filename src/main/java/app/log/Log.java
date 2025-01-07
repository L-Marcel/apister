package app.log;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import io.github.kamilszewc.javaansitextcolorizer.Colorizer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log extends Thread {
    private static Log instance;
    private Semaphore semaphore = new Semaphore(0);
    private boolean running = true;
    private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");

    //#region Singleton
    private Log() {
        super();
        setPriority(MIN_PRIORITY);
        this.start();
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
        this.queue.add(
            "[" + this.applyDataTimeColor(this.formatter.format(LocalDateTime.now())) + "]:[" + 
            this.applySenderColor(sender) + "] " + message
        );
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
                    System.out.println(log);
                };
            } catch(Exception e) {};
        };
    };
    //#endregion
    //#region Colors
    private String applyDataTimeColor(String dataTime) {
        return Colorizer.color(dataTime, Colorizer.Color.YELLOW_BRIGHT);
    };

    private String applySenderColor(String sender) {
        switch(sender) {
            case "Error":
                return Colorizer.color(sender, Colorizer.Color.RED_BRIGHT);
            case "Controller":
                return Colorizer.color(sender, Colorizer.Color.MAGENTA_BRIGHT);
            case "Main":
                return Colorizer.color(sender, Colorizer.Color.GREEN_BRIGHT);
            case "Storage":
                return Colorizer.color(sender, Colorizer.Color.BLUE_BRIGHT);
            case "Storable":
                return Colorizer.color(sender, Colorizer.Color.CYAN_BRIGHT);
            default:
                return sender;
        }
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
        } catch(IllegalThreadStateException e) {};
    };
    //#endregion
};