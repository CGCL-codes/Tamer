    void addMessageAndWait(EncryptedMessageWriter writer, Message message, boolean encrypted) throws InterruptedException {
        Task task = new Task(Thread.currentThread(), writer, message, encrypted);
        taskQueue.add(task);
        task.waitForCompletion();
    }
