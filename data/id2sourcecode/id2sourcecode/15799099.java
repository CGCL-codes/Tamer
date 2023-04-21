    public String[] getChannelIDs() {
        synchronized (this) {
            return (String[]) channels.keySet().toArray(new String[channels.keySet().size()]);
        }
    }
