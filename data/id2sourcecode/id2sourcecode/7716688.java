    public static String getChannelUrl(int index) {
        if (appContext == null) {
            appContext = Application.getInstance(TelkkuApp.class).getContext();
        }
        if (resourceMap == null) {
            resourceMap = appContext.getResourceMap(TelkkuView.class);
        }
        return resourceMap.getString("ChannelList.channel" + index + ".url");
    }
