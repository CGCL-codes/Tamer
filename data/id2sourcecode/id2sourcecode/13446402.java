    private Channel getChannel(int id) {
        synchronized (channels) {
            for (int i = 0; i < channels.size(); i++) {
                Channel c = (Channel) channels.elementAt(i);
                if (c.localID == id) return c;
            }
        }
        return null;
    }
