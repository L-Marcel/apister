package app.secure;

import app.log.Log;
import app.storage.Storage;

public class ShutdownHook extends Thread {
    @Override
    public void run() {
        System.out.println("\nEncerrando aplicação...");
        System.out.println("Sincronizando os dados salvos...");
        Storage.finish();
        Log.finish();
        System.out.println("Dados sincronizados com sucesso!");
    };
};
