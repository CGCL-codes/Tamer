    public Info getDisplayedInfo() {
        final IModuleStorage.Info info = new IModuleStorage.Info();
        info.channelsList = getChannelsSetByName(config.currentChannelSetName);
        info.minDate = getDate();
        info.maxDate = getDate() + todayMillis;
        return info;
    }
