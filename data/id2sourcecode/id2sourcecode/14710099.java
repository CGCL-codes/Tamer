    public void run() {
        logger.write(Logger.formatMsg(Logger.INFO, "ShutDown Hook Interrupting The Logger Thread" + Logger.NEWLINE));
        logger.interrupt();
    }
