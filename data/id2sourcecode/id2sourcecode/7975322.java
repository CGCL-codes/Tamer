    private void handleAsyncAgiEvent(AsyncAgiEvent asyncAgiEvent) {
        final ManagerConnection connection;
        final String channelName;
        final AsyncAgiConnectionHandler connectionHandler;
        connection = (ManagerConnection) asyncAgiEvent.getSource();
        channelName = asyncAgiEvent.getChannel();
        if (asyncAgiEvent.isStart()) {
            connectionHandler = new AsyncAgiConnectionHandler(getMappingStrategy(), asyncAgiEvent);
            setConnectionHandler(connection, channelName, connectionHandler);
            execute(connectionHandler);
        } else {
            connectionHandler = getConnectionHandler(connection, channelName);
            if (connectionHandler == null) {
                logger.info("No AsyncAgiConnectionHandler registered for channel " + channelName + ": Ignoring AsyncAgiEvent");
                return;
            }
            if (asyncAgiEvent.isExec()) {
                connectionHandler.onAsyncAgiExecEvent(asyncAgiEvent);
            } else if (asyncAgiEvent.isEnd()) {
                connectionHandler.onAsyncAgiEndEvent(asyncAgiEvent);
                removeConnectionHandler(connection, channelName);
            } else {
                logger.warn("Ignored unknown AsyncAgiEvent of sub type '" + asyncAgiEvent.getSubEvent() + "'");
            }
        }
    }
