    public void copyDimmerNamesToChannels() {
        Dimmers dimmers = context.getShow().getDimmers();
        for (int i = 0; i < dimmers.size(); i++) {
            Dimmer dimmer = dimmers.get(i);
            Channel channel = dimmer.getChannel();
            if (channel != null) {
                channel.setName(dimmer.getName());
            }
        }
    }
