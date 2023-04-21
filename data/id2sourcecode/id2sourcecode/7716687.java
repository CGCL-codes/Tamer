    public static int getChannelCount() {
        if (appContext == null) {
            appContext = Application.getInstance(TelkkuApp.class).getContext();
        }
        if (resourceMap == null) {
            resourceMap = appContext.getResourceMap(TelkkuView.class);
        }
        return resourceMap.getInteger("ChannelList.channelCount");
    }
