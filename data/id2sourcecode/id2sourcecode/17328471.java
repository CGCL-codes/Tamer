    public AbstractIRCChannel getChannel(String name) {
        for (int i = 0; i < channels.size(); i++) {
            if (((AbstractIRCChannel) channels.get(i)).getName().equalsIgnoreCase(name)) {
                return (AbstractIRCChannel) channels.get(i);
            }
        }
        return null;
    }
