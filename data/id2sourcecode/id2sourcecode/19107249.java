    private Channel getChannel(String string, Channel existing) {
        log.info("Getting channel: " + string);
        synchronized (builder) {
            Channel aChannel = null;
            try {
                builder.beginTransaction();
                if (existing == null) {
                    aChannel = (Channel) channelRegistry.addChannel(new URL(string), 30, false);
                } else {
                    aChannel = (Channel) channelRegistry.addChannel(existing, false, 30);
                }
                long chanid = aChannel.getId();
                log.info("Got channel: " + aChannel + "(id =" + chanid + ")");
                aChannel.addObserver(this);
                builder.endTransaction();
                channelRegistry.activateChannel(aChannel, 60);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ChannelBuilderException e1) {
                e1.printStackTrace();
            }
            return aChannel;
        }
    }
