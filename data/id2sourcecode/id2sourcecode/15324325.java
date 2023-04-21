    public ManagedReference<GameUniverse> register() {
        AppContext.getChannelManager().createChannel("chat-general", new ChatChannelListener(), Delivery.UNRELIABLE);
        AppContext.getChannelManager().createChannel("chat-trade", new ChatChannelListener(), Delivery.UNRELIABLE);
        ScalableList<ServerLocalSpace> localSpaceList = new ScalableList<ServerLocalSpace>();
        ScalableDeque<ServerLocalSpace> queue = new ScalableDeque<ServerLocalSpace>();
        localSpaceListRef = AppContext.getDataManager().createReference(localSpaceList);
        addQueue = AppContext.getDataManager().createReference(queue);
        ManagedReference<GameUniverse> ret = AppContext.getDataManager().createReference(this);
        AppContext.getDataManager().setBinding(bindingName, this);
        return ret;
    }
