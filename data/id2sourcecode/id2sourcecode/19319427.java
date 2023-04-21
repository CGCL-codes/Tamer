    public static void sendMessage(EncryptedMessageWriter writer, Message message, boolean encrypted) {
        prepareWriterThread();
        writerThread.addMessage(writer, message, encrypted);
    }
