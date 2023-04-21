    public Channel[] getChannels() {
        synchronized (this) {
            Channel[] channelArray = new Channel[channels.size()];
            int i = 0;
            for (Enumeration<Channel> itr = channels.elements(); itr.hasMoreElements(); ++i) {
                Channel channel = itr.nextElement();
                channelArray[i] = channel;
            }
            return channelArray;
        }
    }
