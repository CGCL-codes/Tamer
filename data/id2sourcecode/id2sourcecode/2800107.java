    public static void writeThreadExecutionMessage(String readWrite, String productIdMessage) {
        String threadName = Thread.currentThread().getName();
        StringBuilder message = new StringBuilder();
        message.append("thread ");
        message.append(threadName);
        message.append(" - ");
        message.append(readWrite);
        message.append(" product(s) with id(s)");
        message.append(productIdMessage);
    }
