    public void warn(String i_stringToBeLogged) {
        if (this.currentLogLevel > LoggerPriority.WARN_INT) return;
        this.writeToLog(Thread.currentThread().getName() + ": " + this.getLoggedClassName() + ": " + i_stringToBeLogged);
    }
