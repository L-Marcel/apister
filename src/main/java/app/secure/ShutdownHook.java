package app.secure;

import app.log.Log;
import app.storage.Storage;

public class ShutdownHook extends Thread {
    @Override
    public void run() {
        Log.print("Finishing process...");
        Log.print("Syncronizing data...");
        Storage.getInstance().finish();
        Log.print("Data syncronized!");
        Log.finish();
    };
};
