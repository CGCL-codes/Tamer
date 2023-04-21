    public AbstractIRCChannel getChannel(String name) {
        for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i).getName().equalsIgnoreCase(name)) {
                return channels.get(i);
            }
        }
        return null;
    }
