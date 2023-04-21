    public Channel getChannel(String channelName) {
        if (ctree == null) {
            synchronized (this) {
                do {
                    try {
                        wait(250);
                    } catch (InterruptedException e) {
                    }
                } while (ctree == null);
            }
        }
        return channels.get(channelName);
    }
