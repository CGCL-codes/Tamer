    public void defaultPatch() {
        Dimmers dimmers = context.getShow().getDimmers();
        Channels channels = context.getShow().getChannels();
        for (int i = 0; i < dimmers.size(); i++) {
            if (i < channels.size()) {
                dimmers.get(i).setChannel(channels.get(i));
            } else {
                dimmers.get(i).setChannel(null);
            }
        }
        context.getShow().updateChannelInputs();
        fireTableDataChanged();
    }
