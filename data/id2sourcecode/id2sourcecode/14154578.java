    public ManagedReference<ServerLocalSpace> register() {
        AppContext.getChannelManager().createChannel(chatPrefix + localSpaceName, new ChatChannelListener(), Delivery.UNRELIABLE);
        AppContext.getChannelManager().createChannel(updatePrefix + localSpaceName, new LocalSpaceChannelListener(), Delivery.RELIABLE);
        selfReference = AppContext.getDataManager().createReference(this);
        themeManager = spaceType.getManager(this).register();
        return selfReference;
    }
