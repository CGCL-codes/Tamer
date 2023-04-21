    void registerEventHandler(final AbstractEventHandler eventHandler) throws IOException {
        assert eventHandler != null;
        assert isTransportLayerThread();
        assert state_ == State.OPEN;
        assert !eventHandlers_.contains(eventHandler);
        acquireSelectorGuard();
        try {
            eventHandler.setSelectionKey(eventHandler.getChannel().register(selector_, eventHandler.getInterestOperations(), eventHandler));
        } finally {
            releaseSelectorGuard();
        }
        eventHandlers_.add(eventHandler);
        Debug.getDefault().trace(Debug.OPTION_DEFAULT, String.format("Registered event handler '%s'", eventHandler));
    }
