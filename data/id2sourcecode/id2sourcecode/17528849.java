    public SGSChannelData getChannelData(String name) {
        DataManager dman = AppContext.getDataManager();
        return dman.getBinding(SGSChannelData.getBinding(name), SGSChannelData.class);
    }
