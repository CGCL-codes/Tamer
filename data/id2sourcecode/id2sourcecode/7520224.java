    public ChannelAppletLocation getChannelAppletLocation() {
        String scriptName = ChannelAppletAgiScript.class.getName();
        return new ChannelAppletLocation("/" + scriptName + "?appId=" + getId());
    }
