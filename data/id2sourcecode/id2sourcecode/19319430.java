    void addMessage(EncryptedMessageWriter writer, Message message, boolean encrypted) {
        taskQueue.add(new Task(Thread.currentThread(), writer, message, encrypted));
    }
