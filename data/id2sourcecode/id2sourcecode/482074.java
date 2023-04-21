    public static void sendFile(ConnectedThreadJava thread, String name) {
        StorageHandler readHandler = new StorageHandler(name);
        readHandler.openRead();
        thread.write(readHandler.read());
    }
