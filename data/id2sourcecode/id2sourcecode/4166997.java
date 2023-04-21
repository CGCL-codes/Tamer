    public FSAChannel createDeviceChannel(String title, String link, String description) {
        if (getChannels().containsKey(title)) {
            return null;
        }
        try {
            FSAChannel channel = new FSAChannel();
            channel.setTitle(title);
            channel.setDescription(description);
            if (!link.contentEquals("")) {
                channel.setSite(new URL(link));
            }
            channel.setMaxNumberOfItems(15);
            channel.setUpdateFrequency(1);
            channel.setUpdateFrequency(1);
            channel.setTtl(2);
            getChannels().put(title, channel);
            return channel;
        } catch (MalformedURLException ex) {
            Logger.getLogger(RSS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
