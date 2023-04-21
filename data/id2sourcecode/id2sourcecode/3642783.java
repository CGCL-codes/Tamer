    public void error(String i_stringToBeLogged) {
        if (this.currentLogLevel > LoggerPriority.ERROR_INT) return;
        this.writeToLog(Thread.currentThread().getName() + ": " + this.getLoggedClassName() + ": " + i_stringToBeLogged);
    }
