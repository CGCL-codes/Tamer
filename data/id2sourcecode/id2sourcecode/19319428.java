    public static void sendMessageAndWait(EncryptedMessageWriter writer, Message message, boolean encrypted) throws InterruptedException {
        prepareWriterThread();
        writerThread.addMessageAndWait(writer, message, encrypted);
    }
