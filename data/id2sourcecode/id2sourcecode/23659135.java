    protected TVChannelsSet getChannelsSetByName(final String channelsSetName) {
        if (channelsSetName == null) {
            return Application.getInstance().getDataStorage().getInfo().channelsList;
        } else {
            for (int i = 0; i < Application.getInstance().getChannelsSetsList().size(); i++) {
                TVChannelsSet cs = (TVChannelsSet) Application.getInstance().getChannelsSetsList().get(i);
                if (channelsSetName.equals(cs.getName())) {
                    return cs;
                }
            }
            return null;
        }
    }
