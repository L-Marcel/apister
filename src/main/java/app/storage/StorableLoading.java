package app.storage;

import app.interfaces.StoringCallback;

public class StorableLoading extends Thread {
    private Storage storage = Storage.getInstance();
    private StoringCallback storedCallback;

    public StorableLoading() {
        this.setPriority(MIN_PRIORITY);
    };

    public void start(StoringCallback storedCallback) {
        this.storedCallback = storedCallback;
        super.start();
    };

    @Override
    public void run() {
        while(storage.hasTask()) {
            try {
                Thread.sleep(1000);
            } catch(Exception e) {
                e.printStackTrace();
            };
        };

        if(this.storedCallback != null) this.storedCallback.call();
    };
};
