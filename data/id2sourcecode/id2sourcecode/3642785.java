    public void debug(String i_stringToBeLogged) {
        if (this.currentLogLevel > LoggerPriority.DEBUG_INT) return;
        this.writeToLog(Thread.currentThread().getName() + ": " + this.getLoggedClassName() + ": " + i_stringToBeLogged);
    }
