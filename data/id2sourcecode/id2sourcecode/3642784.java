    public void info(String i_stringToBeLogged) {
        if (this.currentLogLevel > LoggerPriority.INFO_INT) return;
        this.writeToLog(Thread.currentThread().getName() + ": " + this.getLoggedClassName() + ": " + i_stringToBeLogged);
    }
