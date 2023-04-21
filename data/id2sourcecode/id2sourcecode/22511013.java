    public Object createCoreServiceImplementation(ServiceImplementationFactoryParameters factoryParameters) {
        log = factoryParameters.getLog();
        log.info("Starting NetworkEventThread");
        try {
            eventThread = new NetworkEventThreadImpl();
        } catch (IOException e) {
            log.error("Error while creating NetworkEvenThread", e);
            return null;
        }
        eventThread.setAcceptEventHandler(acceptEventHandler);
        eventThread.setLog(factoryParameters.getLog());
        eventThread.setName("network-event-thread");
        eventThread.setReadEventHandler(readEventHandler);
        eventThread.setWriteEventHandler(writeEventHandler);
        eventThread.start();
        ThreadState threadState = new ThreadState();
        threadState.setName("RUNNING");
        threadStateManager.setThreadState(eventThread, threadState);
        return eventThread;
    }
