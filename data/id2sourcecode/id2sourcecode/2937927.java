    @Override
    public void start() throws GeneralException {
        isStarted = true;
        writerThread.start();
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", CommonsLogImpl.class.getName());
        addLog(LogLevel.VERBOSE, "D�marrage");
    }
