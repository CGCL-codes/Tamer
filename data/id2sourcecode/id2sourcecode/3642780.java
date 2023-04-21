    public void warn(String i_stringToBeLogged, Throwable i_throwable) {
        if (this.currentLogLevel > LoggerPriority.WARN_INT) return;
        this.writeToLog(Thread.currentThread().getName() + ": " + this.getLoggedClassName() + ": " + i_stringToBeLogged + "\n" + i_throwable.getClass().getName() + ": " + i_throwable.getMessage());
    }
