    public void fatal(String i_stringToBeLogged) {
        if (this.currentLogLevel > LoggerPriority.FATAL_INT) return;
        this.writeToLog(Thread.currentThread().getName() + ": " + this.getLoggedClassName() + ": " + i_stringToBeLogged);
    }
