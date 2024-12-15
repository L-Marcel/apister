package app.storage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import app.log.Log;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Thread responsible for storing data in files.
 */
public class Storage extends Thread {
    private Semaphore semaphore = new Semaphore(0);
    private static Storage instance;
    private boolean running = false;
    private ConcurrentHashMap<String, Integer> tasks = new ConcurrentHashMap<String, Integer>();
    private ConcurrentHashMap<String, Serializable> map = new ConcurrentHashMap<String, Serializable>(); 

    //#region Singleton
    private Storage() {
        super();
        setPriority(3);
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (Exception e) {};
        this.start();
    };
    
    public static Storage getInstance() {
        if(Storage.instance == null) Storage.instance = new Storage();
        return Storage.instance;
    };
    //#endregion

    //#region Tasks
    /**
     * Verify if the thread is busy.
     * If not, start processing new tasks.
     */
    public void inspect() {
        if(this.semaphore.availablePermits() == 0) {
            this.semaphore.release();
        };
    };

    /**
     * Check if there is any task in queue.
     * @return true if there are tasks, false otherwise
     */
    public boolean hasTask() {
        return this.tasks.size() > 0 && this.tasks.values().stream().anyMatch((task) -> task != 0);
    };

    /**
     * Check if there is a specific named task in queue.
     * @param name task name
     * @return true if there are tasks, false otherwise
     */
    public boolean hasTask(String name) {
        return this.tasks.containsKey(name) && this.tasks.get(name) > 0;
    };
    //#endregion

    //#region Core
    /**
     * Request the storage of a list of serializable objects in a file.
     * @param name - the file name
     * @param data - the list
     */
    protected synchronized void store(String name, Serializable data) {
        try {
            boolean isFirstTask = !hasTask();
            this.tasks.merge(name, 1, Integer::sum);
            this.map.put(name, data);
            if(isFirstTask) {
                Log.print("Storage", "Tasks were requested.");
                this.inspect();
            };
        } catch (Exception e) {
            Log.print("Storage", "Task request failed.");
        };
    };

    /**
     * Run the thread, storing the data in the files.
     */
    @Override
    public void run() {
        this.running = true;
        Log.print("Storage", "Started.");
        String currentTaskName = "";
        while(this.running || this.hasTask()) {
            try {
                // Wait for tasks
                if(!this.hasTask()) {
                    semaphore.acquire();
                };

                // Process tasks by names
                for(String name : this.map.keySet()) {
                    // Wait for more requests
                    if(this.hasTask(name)) Thread.sleep(1000);
                    else continue;

                    // Remove unnecessary tasks
                    Integer count = this.tasks.getOrDefault(name, 0);
                    if(count > 1) {
                        this.tasks.put(name, 1);
                        continue;
                    };
                    
                    // Get task data
                    currentTaskName = name;
                    Serializable storable = this.map.get(name);
                    FileOutputStream file = new FileOutputStream("data/" + name + ".dat", false);
                    ObjectOutputStream object = new ObjectOutputStream(file);
                    
                    // Store the data and remove the task
                    object.writeObject(storable);
                    object.flush();
                    object.close();
                    file.close();
                    this.map.remove(name);
                    this.tasks.put(name, 0);
                    if(storable instanceof List<?>) {
                        List<?> list = (List<?>) storable;
                        Log.print("Storage", "Task finished, " + list.size() + " " + name + " were stored in a list.");
                    } else if(storable instanceof Map<?, ?>) {
                        Map<?, ?> map = (Map<?, ?>) storable;
                        Log.print("Storage", "Task finished, " + map.size() + " " + name + " were stored on a map.");
                    } else if(storable instanceof Set<?>) {
                        Set<?> set = (Set<?>) storable;
                        Log.print("Storage", "Task finished, " + set.size() + " " + name + " were stored in a set.");
                    } else if(storable instanceof Queue<?>) {
                        Queue<?> queue = (Queue<?>) storable;
                        Log.print("Storage", "Task finished, " + queue.size() + " " + name + " were stored in a queue.");
                    } else if(name.endsWith("s")) {
                        Log.print("Storage", "Task finished, " + name + " were stored.");
                    } else {
                        Log.print("Storage", "Task finished, " + name + " was stored.");
                    };
                };
            } catch (Exception e) {
                this.tasks.put(currentTaskName, 0);
                Log.print("Storage", "Tasks from " + currentTaskName + " failed.");
                Log.print("Error", e.getMessage());
            };
        };

        Log.print("Storage", "Finished.");
    };
    //#endregion

    //#region Control
    /**
     * Finish the thread safely.
     */
    public void finish() {
        try {
            if(Storage.instance != null) {
                Log.print("Storage", "Finishing...");
                Storage.instance.running = false;
                Storage.instance.inspect();
                Storage.instance.join();
            };
        } catch (InterruptedException e) {
            Log.print("Storage", "Can't finish safely.");
            Log.print("Error", e.getMessage());
        };
    };

    /**
     * Start the thread.
     */
    @Override
    public void start() {
        try {
            Log.print("Storage", "Starting...");
            super.start();
        } catch (IllegalThreadStateException e) {
            Log.print("Storage", "Can't start.");
            Log.print("Error", e.getMessage());
        };
    };
    //#endregion
};
