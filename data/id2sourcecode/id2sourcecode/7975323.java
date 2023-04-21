    private void handleRenameEvent(RenameEvent renameEvent) {
        final ManagerConnection connection = (ManagerConnection) renameEvent.getSource();
        final AsyncAgiConnectionHandler connectionHandler = getConnectionHandler(connection, renameEvent.getChannel());
        if (connectionHandler == null) {
            return;
        }
        removeConnectionHandler(connection, renameEvent.getChannel());
        setConnectionHandler(connection, renameEvent.getNewname(), connectionHandler);
        connectionHandler.updateChannelName(renameEvent.getNewname());
    }
