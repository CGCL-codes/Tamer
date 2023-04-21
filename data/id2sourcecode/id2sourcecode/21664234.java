    public static ChannelData getChannelData(String channelName) {
        try {
            DataManager dman = AppContext.getDataManager();
            return dman.getBinding(Constants.CHANNEL_DATA + channelName, ChannelData.class);
        } catch (NameNotBoundException ex) {
            return null;
        } catch (ObjectNotFoundException ex) {
            return null;
        }
    }
