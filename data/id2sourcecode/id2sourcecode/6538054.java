    private int initServerManager(Context context, ServerManagerProps serverManagerProps, List<ServerProps> serverPropsList) {
        ServerManager serverManager = new ServerManagerImpl(serverManagerProps);
        int srvMaxTimeout = 0;
        for (ServerProps serverProps : serverPropsList) {
            if (srvMaxTimeout < serverProps.getChannelIdleTimeout()) srvMaxTimeout = serverProps.getChannelIdleTimeout();
            serverManager.registerServer(serverProps);
        }
        context.setServerManager(serverManager);
        return srvMaxTimeout;
    }
