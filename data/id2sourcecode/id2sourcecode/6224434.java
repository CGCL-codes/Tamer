    public void initialize(Properties p) {
        System.out.println("initializing block world server!");
        ChannelManager cm = AppContext.getChannelManager();
        Channel c = cm.createChannel("channel 1", new BoxWorldChannelListener(), Delivery.RELIABLE);
        channel = AppContext.getDataManager().createReference(c);
        TaskManager tm = AppContext.getTaskManager();
        tm.schedulePeriodicTask(new UpdateClientGames(channel), 0, 50);
    }
