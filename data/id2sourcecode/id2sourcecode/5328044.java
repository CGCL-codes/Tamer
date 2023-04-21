    public void removeChannel(String name) {
        channelNames.remove(name);
        for (Stream s : streams) {
            SourceChannel toRemove = null;
            for (SourceChannel sc : s.getChannels()) {
                if (sc.getName().equals(name)) {
                    toRemove = sc;
                }
            }
            if (toRemove != null) {
                s.removeChannel(toRemove);
            }
        }
    }
